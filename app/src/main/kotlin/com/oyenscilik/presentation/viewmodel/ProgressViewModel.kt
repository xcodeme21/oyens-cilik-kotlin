package com.oyenscilik.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyenscilik.data.local.PreferencesManager
import com.oyenscilik.domain.model.Result
import com.oyenscilik.domain.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ModuleProgress(
    val moduleName: String,
    val completedItems: Int,
    val totalItems: Int,
    val starsEarned: Int
) {
    val progressPercent: Float get() = if (totalItems > 0) completedItems.toFloat() / totalItems else 0f
}

data class DailyChallenge(
    val id: String,
    val title: String,
    val description: String,
    val targetCount: Int,
    val currentCount: Int,
    val rewardStars: Int,
    val isCompleted: Boolean = false
)

data class ProgressUiState(
    val isLoading: Boolean = false,
    val totalStars: Int = 0,
    val currentLevel: Int = 1,
    val levelTitle: String = "Pemula",
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalLessonsCompleted: Int = 0,
    val moduleProgress: List<ModuleProgress> = emptyList(),
    val dailyChallenges: List<DailyChallenge> = emptyList(),
    val starsToNextLevel: Int = 100,
    val currentLevelProgress: Float = 0f,
    val error: String? = null
)

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        loadProgress()
    }

    fun loadProgress() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val childId = preferencesManager.getChildId()
            if (childId == null) {
                // Guest mode - show default progress
                _uiState.value = ProgressUiState(
                    isLoading = false,
                    totalStars = 0,
                    currentLevel = 1,
                    levelTitle = "Pemula",
                    moduleProgress = listOf(
                        ModuleProgress("Huruf", 0, 26, 0),
                        ModuleProgress("Angka", 0, 21, 0),
                        ModuleProgress("Hewan", 0, 20, 0)
                    ),
                    dailyChallenges = getDefaultChallenges()
                )
                return@launch
            }
            
            when (val result = progressRepository.getProgressSummary(childId)) {
                is Result.Success -> {
                    val data = result.data
                    val levelInfo = calculateLevel(data.totalStars)
                    
                    _uiState.value = ProgressUiState(
                        isLoading = false,
                        totalStars = data.totalStars,
                        currentLevel = levelInfo.first,
                        levelTitle = levelInfo.second,
                        currentStreak = data.currentStreak,
                        longestStreak = maxOf(data.currentStreak, 14),
                        totalLessonsCompleted = data.totalLettersLearned + data.totalNumbersLearned + data.totalAnimalsLearned,
                        moduleProgress = listOf(
                            ModuleProgress("Huruf", data.totalLettersLearned, 26, data.totalLettersLearned * 3),
                            ModuleProgress("Angka", data.totalNumbersLearned, 21, data.totalNumbersLearned * 3),
                            ModuleProgress("Hewan", data.totalAnimalsLearned, 20, data.totalAnimalsLearned * 3)
                        ),
                        dailyChallenges = getDefaultChallenges(),
                        starsToNextLevel = levelInfo.third,
                        currentLevelProgress = levelInfo.fourth
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

    private fun calculateLevel(totalStars: Int): Quadruple<Int, String, Int, Float> {
        val levels = listOf(
            Triple(1, "Pemula", 0),
            Triple(2, "Penjelajah", 50),
            Triple(3, "Mahir", 100),
            Triple(4, "Ahli", 200),
            Triple(5, "Master", 500)
        )
        
        var currentLevel = levels[0]
        var nextLevel = levels[1]
        
        for (i in levels.indices) {
            if (totalStars >= levels[i].third) {
                currentLevel = levels[i]
                nextLevel = if (i + 1 < levels.size) levels[i + 1] else levels[i]
            }
        }
        
        val starsToNext = nextLevel.third - totalStars
        val progress = if (nextLevel.third > currentLevel.third) {
            (totalStars - currentLevel.third).toFloat() / (nextLevel.third - currentLevel.third)
        } else 1f
        
        return Quadruple(currentLevel.first, currentLevel.second, maxOf(0, starsToNext), progress.coerceIn(0f, 1f))
    }

    private fun getDefaultChallenges(): List<DailyChallenge> = listOf(
        DailyChallenge("1", "Belajar 3 Huruf", "Pelajari 3 huruf baru hari ini", 3, 0, 10),
        DailyChallenge("2", "Latihan Angka", "Latihan berhitung 5 kali", 5, 0, 15),
        DailyChallenge("3", "Kenali Hewan", "Pelajari 2 hewan baru", 2, 0, 10)
    )

    fun recordLessonCompleted(moduleName: String, starsEarned: Int) {
        viewModelScope.launch {
            val currentState = _uiState.value
            _uiState.value = currentState.copy(
                totalStars = currentState.totalStars + starsEarned,
                totalLessonsCompleted = currentState.totalLessonsCompleted + 1
            )
            // Refresh from API
            loadProgress()
        }
    }
}

// Helper class since Kotlin doesn't have Quadruple
data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
