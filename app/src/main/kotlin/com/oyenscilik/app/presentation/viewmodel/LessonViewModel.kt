package com.oyenscilik.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyenscilik.app.data.repository.LessonCounter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing lesson progress and login state
 */
@HiltViewModel
class LessonViewModel @Inject constructor(
    private val lessonCounter: LessonCounter
) : ViewModel() {
    
    val lessonsCompleted: Flow<Int> = lessonCounter.lessonsCompleted
    val isLoggedIn: Flow<Boolean> = lessonCounter.isLoggedIn
    val shouldShowLogin: Flow<Boolean> = lessonCounter.shouldShowLogin
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    /**
     * Record that a lesson has been completed
     */
    fun recordLessonCompleted(contentType: String, contentId: Int) {
        viewModelScope.launch {
            try {
                lessonCounter.incrementLessonCount()
                // TODO: Also send to API for anonymous progress tracking
            } catch (e: Exception) {
                _errorMessage.value = "Gagal menyimpan progress: ${e.message}"
            }
        }
    }
    
    /**
     * Check if user can access the next lesson
     */
    suspend fun canAccessNextLesson(): Boolean {
        return lessonCounter.canAccessLesson()
    }
    
    /**
     * Get current lesson count
     */
    suspend fun getCurrentLessonCount(): Int {
        return lessonCounter.getCurrentCount()
    }
    
    /**
     * Mark user as logged in
     */
    fun setLoggedIn(loggedIn: Boolean) {
        viewModelScope.launch {
            lessonCounter.setLoggedIn(loggedIn)
        }
    }
    
    /**
     * Clear error message
     */
    fun clearError() {
        _errorMessage.value = null
    }
}
