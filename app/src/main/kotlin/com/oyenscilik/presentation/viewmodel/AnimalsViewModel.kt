package com.oyenscilik.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyenscilik.domain.model.Animal
import com.oyenscilik.domain.model.Result
import com.oyenscilik.domain.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnimalsUiState(
    val animals: List<Animal> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class AnimalsViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnimalsUiState())
    val uiState: StateFlow<AnimalsUiState> = _uiState.asStateFlow()

    init {
        loadAnimals()
    }

    private fun loadAnimals() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            when (val result = contentRepository.getAnimals()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        animals = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> { /* ignore */ }
            }
        }
    }
}
