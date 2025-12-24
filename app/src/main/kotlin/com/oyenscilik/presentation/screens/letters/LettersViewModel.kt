package com.oyenscilik.presentation.screens.letters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyenscilik.domain.model.Letter
import com.oyenscilik.domain.model.Result
import com.oyenscilik.domain.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LettersUiState(
    val letters: List<Letter> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LettersViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LettersUiState())
    val uiState: StateFlow<LettersUiState> = _uiState.asStateFlow()

    init {
        loadLetters()
    }

    private fun loadLetters() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = contentRepository.getLetters()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        letters = result.data,
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
}
