package com.oyenscilik.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyenscilik.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _isGuestLimitReached = MutableStateFlow(false)
    val isGuestLimitReached: StateFlow<Boolean> = _isGuestLimitReached.asStateFlow()

    private val _isGuestMode = MutableStateFlow(true)
    val isGuestMode: StateFlow<Boolean> = _isGuestMode.asStateFlow()

    init {
        checkGuestStatus()
    }

    fun checkGuestStatus() {
        viewModelScope.launch {
            _isGuestMode.value = preferencesManager.isGuestMode()
            _isGuestLimitReached.value = preferencesManager.hasReachedGuestLimit()
        }
    }

    fun onLessonCompleted() {
        viewModelScope.launch {
            if (_isGuestMode.value) {
                preferencesManager.incrementGuestLesson()
                checkGuestStatus()
            }
        }
    }
    
    fun onLoginSuccess() {
        viewModelScope.launch {
             // Mock login success
             preferencesManager.saveTokens("mock_access", "mock_refresh")
             preferencesManager.clearGuestMode()
             checkGuestStatus()
        }
    }
}
