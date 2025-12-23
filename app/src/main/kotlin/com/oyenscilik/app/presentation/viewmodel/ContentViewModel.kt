package com.oyenscilik.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyenscilik.app.data.model.*
import com.oyenscilik.app.data.network.NetworkResult
import com.oyenscilik.app.data.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContentUiState<T>(
    val isLoading: Boolean = true,
    val data: List<T> = emptyList(),
    val error: String? = null
)

data class ContentDetailState<T>(
    val isLoading: Boolean = true,
    val data: T? = null,
    val error: String? = null
)

/**
 * ViewModel for managing content (letters, numbers, animals) from API
 */
@HiltViewModel
class ContentViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {

    // Letters
    private val _lettersState = MutableStateFlow(ContentUiState<Letter>())
    val lettersState: StateFlow<ContentUiState<Letter>> = _lettersState.asStateFlow()

    private val _letterDetailState = MutableStateFlow(ContentDetailState<Letter>())
    val letterDetailState: StateFlow<ContentDetailState<Letter>> = _letterDetailState.asStateFlow()

    // Numbers
    private val _numbersState = MutableStateFlow(ContentUiState<NumberContent>())
    val numbersState: StateFlow<ContentUiState<NumberContent>> = _numbersState.asStateFlow()

    private val _numberDetailState = MutableStateFlow(ContentDetailState<NumberContent>())
    val numberDetailState: StateFlow<ContentDetailState<NumberContent>> = _numberDetailState.asStateFlow()

    // Animals
    private val _animalsState = MutableStateFlow(ContentUiState<Animal>())
    val animalsState: StateFlow<ContentUiState<Animal>> = _animalsState.asStateFlow()

    private val _animalDetailState = MutableStateFlow(ContentDetailState<Animal>())
    val animalDetailState: StateFlow<ContentDetailState<Animal>> = _animalDetailState.asStateFlow()

    init {
        // Preload all content for smooth navigation
        loadLetters()
        loadNumbers()
        loadAnimals()
    }

    // ===== Letters =====
    fun loadLetters() {
        viewModelScope.launch {
            _lettersState.value = _lettersState.value.copy(isLoading = true, error = null)
            when (val result = contentRepository.getLetters()) {
                is NetworkResult.Success -> {
                    _lettersState.value = ContentUiState(
                        isLoading = false,
                        data = result.data,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _lettersState.value = ContentUiState(
                        isLoading = false,
                        data = emptyList(),
                        error = result.message
                    )
                }
            }
        }
    }

    fun loadLetter(id: Int) {
        viewModelScope.launch {
            _letterDetailState.value = _letterDetailState.value.copy(isLoading = true, error = null)
            when (val result = contentRepository.getLetter(id)) {
                is NetworkResult.Success -> {
                    _letterDetailState.value = ContentDetailState(
                        isLoading = false,
                        data = result.data,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _letterDetailState.value = ContentDetailState(
                        isLoading = false,
                        data = null,
                        error = result.message
                    )
                }
            }
        }
    }

    // ===== Numbers =====
    fun loadNumbers() {
        viewModelScope.launch {
            _numbersState.value = _numbersState.value.copy(isLoading = true, error = null)
            when (val result = contentRepository.getNumbers()) {
                is NetworkResult.Success -> {
                    _numbersState.value = ContentUiState(
                        isLoading = false,
                        data = result.data,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _numbersState.value = ContentUiState(
                        isLoading = false,
                        data = emptyList(),
                        error = result.message
                    )
                }
            }
        }
    }

    fun loadNumber(id: Int) {
        viewModelScope.launch {
            _numberDetailState.value = _numberDetailState.value.copy(isLoading = true, error = null)
            when (val result = contentRepository.getNumber(id)) {
                is NetworkResult.Success -> {
                    _numberDetailState.value = ContentDetailState(
                        isLoading = false,
                        data = result.data,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _numberDetailState.value = ContentDetailState(
                        isLoading = false,
                        data = null,
                        error = result.message
                    )
                }
            }
        }
    }

    // ===== Animals =====
    fun loadAnimals(difficulty: String? = null) {
        viewModelScope.launch {
            _animalsState.value = _animalsState.value.copy(isLoading = true, error = null)
            when (val result = contentRepository.getAnimals(difficulty)) {
                is NetworkResult.Success -> {
                    _animalsState.value = ContentUiState(
                        isLoading = false,
                        data = result.data,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _animalsState.value = ContentUiState(
                        isLoading = false,
                        data = emptyList(),
                        error = result.message
                    )
                }
            }
        }
    }

    fun loadAnimal(id: Int) {
        viewModelScope.launch {
            _animalDetailState.value = _animalDetailState.value.copy(isLoading = true, error = null)
            when (val result = contentRepository.getAnimal(id)) {
                is NetworkResult.Success -> {
                    _animalDetailState.value = ContentDetailState(
                        isLoading = false,
                        data = result.data,
                        error = null
                    )
                }
                is NetworkResult.Error -> {
                    _animalDetailState.value = ContentDetailState(
                        isLoading = false,
                        data = null,
                        error = result.message
                    )
                }
            }
        }
    }

    // ===== Utility =====
    fun getLetterById(id: Int): Letter? {
        return _lettersState.value.data.find { it.id == id }
    }

    fun getNumberById(id: Int): NumberContent? {
        return _numbersState.value.data.find { it.id == id }
    }

    fun getAnimalById(id: Int): Animal? {
        return _animalsState.value.data.find { it.id == id }
    }

    fun retry() {
        loadLetters()
        loadNumbers()
        loadAnimals()
    }
}
