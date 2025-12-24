package com.oyenscilik.presentation.screens.animals

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyenscilik.domain.model.Animal
import com.oyenscilik.domain.model.Result
import com.oyenscilik.domain.repository.ContentRepository
import com.oyenscilik.utils.TextToSpeechHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnimalDetailUiState(
    val animal: Animal? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPlaying: Boolean = false
)

@HiltViewModel
class AnimalDetailViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    private val ttsHelper: TextToSpeechHelper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnimalDetailUiState())
    val uiState: StateFlow<AnimalDetailUiState> = _uiState.asStateFlow()

    init {
        // Observe animalId changes from navigation
        viewModelScope.launch {
            savedStateHandle.getStateFlow("animalId", "1").collect { newAnimalId ->
                val id = newAnimalId.toIntOrNull() ?: 1
                loadAnimal(id)
            }
        }
        
        // Observe TTS speaking state
        viewModelScope.launch {
            ttsHelper.isSpeaking.collect { speaking ->
                _uiState.value = _uiState.value.copy(isPlaying = speaking)
            }
        }
    }

    private fun loadAnimal(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = contentRepository.getAnimal(id)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        animal = result.data,
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
        val animal = _uiState.value.animal ?: return
        
        if (_uiState.value.isPlaying) {
            ttsHelper.stop()
        } else {
            // Speak naturally with all 3 texts: "Nama hewan: Kucing. Dalam bahasa Inggris: Cat. Kucing adalah..."
            val speech = "Nama hewan: ${animal.name}. Dalam bahasa Inggris: ${animal.nameEn}. ${animal.description}"
            ttsHelper.speak(speech)
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        ttsHelper.stop()
    }
}
