package com.oyenscilik.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyenscilik.data.local.PreferencesManager
import com.oyenscilik.domain.model.Result
import com.oyenscilik.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WeeklyActivity(
    val dayOfWeek: String,
    val lessonsCompleted: Int,
    val starsEarned: Int,
    val isActive: Boolean
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val isUnlocked: Boolean,
    val progress: Float = 1f
)

data class ChildProfile(
    val id: String,
    val name: String,
    val age: Int,
    val avatarEmoji: String,
    val level: Int,
    val levelTitle: String,
    val totalStars: Int,
    val currentStreak: Int
)

data class ProfileUiState(
    val isLoading: Boolean = false,
    val childProfile: ChildProfile? = null,
    val weeklyActivity: List<WeeklyActivity> = emptyList(),
    val achievements: List<Achievement> = emptyList(),
    val favoriteModule: String = "",
    val totalTimePlayedMinutes: Int = 0,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val childId = preferencesManager.getChildId()
            if (childId == null) {
                // No child ID, show empty/guest state
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    childProfile = ChildProfile(
                        id = "guest",
                        name = "Anak Pintar",
                        age = 0,
                        avatarEmoji = "🐱",
                        level = 1,
                        levelTitle = "Pemula",
                        totalStars = 0,
                        currentStreak = 0
                    )
                )
                return@launch
            }
            
            when (val result = profileRepository.getProfile(childId)) {
                is Result.Success -> {
                    val data = result.data
                    _uiState.value = ProfileUiState(
                        isLoading = false,
                        childProfile = ChildProfile(
                            id = data.childId,
                            name = data.name,
                            age = 0, // Not in API yet
                            avatarEmoji = data.avatarUrl ?: "🐱",
                            level = data.level,
                            levelTitle = data.levelTitle,
                            totalStars = data.totalStars,
                            currentStreak = data.streak
                        ),
                        weeklyActivity = data.recentActivity.take(7).map { activity ->
                            WeeklyActivity(
                                dayOfWeek = activity.date.takeLast(2),
                                lessonsCompleted = activity.lessonsCompleted,
                                starsEarned = activity.starsEarned,
                                isActive = activity.lessonsCompleted > 0
                            )
                        },
                        achievements = generateAchievements(data.totalStars, data.streak, data.lettersProgress.completed),
                        favoriteModule = data.favoriteModule ?: "Huruf",
                        totalTimePlayedMinutes = data.recentActivity.sumOf { it.minutesPlayed }
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

    private fun generateAchievements(stars: Int, streak: Int, letters: Int): List<Achievement> {
        return listOf(
            Achievement("1", "Pemula", "Selesaikan pelajaran pertama", "🌟", true),
            Achievement("2", "Streak 7 Hari", "Belajar 7 hari berturut-turut", "🔥", streak >= 7),
            Achievement("3", "Ahli Huruf", "Pelajari semua huruf A-Z", "🔤", letters >= 26, letters.toFloat() / 26),
            Achievement("4", "Super Star", "Kumpulkan 500 bintang", "⭐", stars >= 500, (stars.toFloat() / 500).coerceAtMost(1f))
        )
    }

    fun updateChildName(newName: String) {
        viewModelScope.launch {
            _uiState.value.childProfile?.let { profile ->
                _uiState.value = _uiState.value.copy(
                    childProfile = profile.copy(name = newName)
                )
                // TODO: Sync with backend
            }
        }
    }
}
