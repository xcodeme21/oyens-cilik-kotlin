package com.oyenscilik.presentation.screens.letters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyenscilik.domain.model.Letter
import com.oyenscilik.domain.model.Result
import com.oyenscilik.domain.repository.ContentRepository
import com.oyenscilik.utils.TextToSpeechHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LetterDetailUiState(
    val letter: Letter? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPlaying: Boolean = false
)

@HiltViewModel
class LetterDetailViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    private val ttsHelper: TextToSpeechHelper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(LetterDetailUiState())
    val uiState: StateFlow<LetterDetailUiState> = _uiState.asStateFlow()

    init {
        // Observe letterId changes from navigation
        viewModelScope.launch {
            savedStateHandle.getStateFlow("letterId", "1").collect { newLetterId ->
                val id = newLetterId.toIntOrNull() ?: 1
                loadLetter(id)
            }
        }
        
        // Observe TTS speaking state
        viewModelScope.launch {
            ttsHelper.isSpeaking.collect { speaking ->
                _uiState.value = _uiState.value.copy(isPlaying = speaking)
            }
        }
    }

    private fun loadLetter(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = contentRepository.getLetter(id)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        letter = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun toggleAudio() {
        val letter = _uiState.value.letter ?: return
        
        if (_uiState.value.isPlaying) {
            ttsHelper.stop()
        } else {
            // Speak naturally: "Huruf A. Contohnya Apel"
            val speech = "Huruf ${letter.letter}. Contohnya ${letter.exampleWord}"
            ttsHelper.speak(speech)
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        ttsHelper.stop()
    }
}
