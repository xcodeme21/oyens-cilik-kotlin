package com.oyenscilik.presentation.screens.numbers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyenscilik.domain.model.NumberContent
import com.oyenscilik.domain.model.Result
import com.oyenscilik.domain.repository.ContentRepository
import com.oyenscilik.utils.TextToSpeechHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NumberDetailUiState(
    val number: NumberContent? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPlaying: Boolean = false
)

@HiltViewModel
class NumberDetailViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    private val ttsHelper: TextToSpeechHelper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(NumberDetailUiState())
    val uiState: StateFlow<NumberDetailUiState> = _uiState.asStateFlow()

    init {
        // Observe numberId changes from navigation
        viewModelScope.launch {
            savedStateHandle.getStateFlow("numberId", "0").collect { newNumberId ->
                val id = newNumberId.toIntOrNull() ?: 0
                loadNumber(id)
            }
        }
        
        // Observe TTS speaking state
        viewModelScope.launch {
            ttsHelper.isSpeaking.collect { speaking ->
                _uiState.value = _uiState.value.copy(isPlaying = speaking)
            }
        }
    }

    private fun loadNumber(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = contentRepository.getNumber(id)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        number = result.data,
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
        val number = _uiState.value.number ?: return
        
        if (_uiState.value.isPlaying) {
            ttsHelper.stop()
        } else {
            // Speak naturally: "Angka 5. Dibaca Lima"
            val speech = "Angka ${number.number}. Dibaca ${number.word}"
            ttsHelper.speak(speech)
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        ttsHelper.stop()
    }
}
