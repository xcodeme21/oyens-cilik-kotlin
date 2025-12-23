package com.oyenscilik.app.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyenscilik.app.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val childName: String = "Anak Pintar",
    val childId: String? = null,
    val totalStars: Int = 0,
    val currentLevel: Int = 1,
    val levelTitle: String = "Pemula",
    val starsToNextLevel: Int = 50,
    val streak: Int = 0,
    val daysActive: Int = 0,
    val totalLessons: Int = 0,
    val lettersProgress: Pair<Int, Int> = 0 to 26,
    val numbersProgress: Pair<Int, Int> = 0 to 21,
    val animalsProgress: Pair<Int, Int> = 0 to 15,
    val weeklyActivity: List<Boolean> = listOf(false, false, false, false, false, false, false),
    val achievements: List<Achievement> = emptyList(),
    val error: String? = null
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val isUnlocked: Boolean
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // Get saved child info from preferences using Flow
                val childName = preferencesRepository.userName.first() ?: "Anak Pintar"
                val childId = preferencesRepository.currentChildId.first()
                
                // TODO: Fetch real data from API when available
                // For now, calculate based on local data or use defaults
                val totalStars = 75 // From API
                val (level, title, starsToNext) = calculateLevel(totalStars)
                
                val achievements = generateAchievements(totalStars, 5, 25)
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        childName = childName,
                        childId = childId,
                        totalStars = totalStars,
                        currentLevel = level,
                        levelTitle = title,
                        starsToNextLevel = starsToNext,
                        streak = 5,
                        daysActive = 12,
                        totalLessons = 25,
                        lettersProgress = 12 to 26,
                        numbersProgress = 8 to 21,
                        animalsProgress = 5 to 15,
                        weeklyActivity = listOf(true, true, true, false, false, false, false),
                        achievements = achievements,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    private fun calculateLevel(stars: Int): Triple<Int, String, Int> {
        return when {
            stars >= 500 -> Triple(5, "Master", 0)
            stars >= 300 -> Triple(4, "Ahli", 500 - stars)
            stars >= 150 -> Triple(3, "Mahir", 300 - stars)
            stars >= 50 -> Triple(2, "Pelajar", 150 - stars)
            else -> Triple(1, "Pemula", 50 - stars)
        }
    }

    private fun generateAchievements(stars: Int, streak: Int, lessons: Int): List<Achievement> {
        return listOf(
            Achievement(
                id = "first_star",
                title = "Bintang Pertama",
                description = "Dapatkan bintang pertama",
                emoji = "⭐",
                isUnlocked = stars >= 1
            ),
            Achievement(
                id = "star_collector",
                title = "Kolektor Bintang",
                description = "Kumpulkan 50 bintang",
                emoji = "🌟",
                isUnlocked = stars >= 50
            ),
            Achievement(
                id = "streak_master",
                title = "Rajin Belajar",
                description = "Streak 5 hari berturut",
                emoji = "🔥",
                isUnlocked = streak >= 5
            ),
            Achievement(
                id = "letter_master",
                title = "Ahli Huruf",
                description = "Selesaikan semua huruf",
                emoji = "🔤",
                isUnlocked = false // TODO: Check from progress
            ),
            Achievement(
                id = "number_master",
                title = "Ahli Angka",
                description = "Selesaikan semua angka",
                emoji = "🔢",
                isUnlocked = false
            ),
            Achievement(
                id = "animal_expert",
                title = "Pakar Hewan",
                description = "Selesaikan semua hewan",
                emoji = "🦁",
                isUnlocked = false
            ),
            Achievement(
                id = "super_learner",
                title = "Super Learner",
                description = "Selesaikan 50 pelajaran",
                emoji = "🎓",
                isUnlocked = lessons >= 50
            ),
            Achievement(
                id = "master_level",
                title = "Level Master",
                description = "Capai level 5",
                emoji = "👑",
                isUnlocked = stars >= 500
            )
        )
    }

    fun updateChildName(name: String) {
        viewModelScope.launch {
            preferencesRepository.saveUserInfo("", name)
            _uiState.update { it.copy(childName = name) }
        }
    }

    fun refresh() {
        loadProfile()
    }
}
