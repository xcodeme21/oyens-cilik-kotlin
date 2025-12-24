package com.oyenscilik.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyenscilik.domain.model.Result
import com.oyenscilik.domain.repository.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val stars: Int,
    val level: Int,
    val avatarEmoji: String = "🐱",
    val isCurrentUser: Boolean = false
)

data class LeaderboardUiState(
    val isLoading: Boolean = false,
    val entries: List<LeaderboardEntry> = emptyList(),
    val currentUserRank: Int = 0,
    val error: String? = null
)

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    init {
        loadLeaderboard()
    }

    fun loadLeaderboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            when (val result = leaderboardRepository.getLeaderboard(10)) {
                is Result.Success -> {
                    val entries = result.data.mapIndexed { index, entry ->
                        LeaderboardEntry(
                            rank = index + 1,
                            name = entry.name,
                            stars = entry.totalStars,
                            level = entry.level,
                            avatarEmoji = entry.avatarUrl ?: "🐱",
                            isCurrentUser = false // TODO: Compare with current child ID
                        )
                    }
                    
                    _uiState.value = LeaderboardUiState(
                        isLoading = false,
                        entries = entries,
                        currentUserRank = entries.indexOfFirst { it.isCurrentUser } + 1
                    )
                }
                is Result.Error -> {
                    // Fallback to mock data if API fails
                    _uiState.value = LeaderboardUiState(
                        isLoading = false,
                        entries = getMockLeaderboard(),
                        error = result.message
                    )
                }
                is Result.Loading -> { /* ignore */ }
            }
        }
    }

    private fun getMockLeaderboard(): List<LeaderboardEntry> = listOf(
        LeaderboardEntry(1, "Anak Hebat", 520, 5, "🦁"),
        LeaderboardEntry(2, "Bintang Kecil", 485, 4, "🐰"),
        LeaderboardEntry(3, "Si Pintar", 450, 4, "🐼"),
        LeaderboardEntry(4, "Jagoan", 380, 4, "🦊"),
        LeaderboardEntry(5, "Kucing Pintar", 156, 3, "🐱", isCurrentUser = true),
        LeaderboardEntry(6, "Pelajar Giat", 140, 3, "🐶"),
        LeaderboardEntry(7, "Anak Rajin", 120, 2, "🐻"),
        LeaderboardEntry(8, "Adik Pintar", 95, 2, "🐹"),
        LeaderboardEntry(9, "Belajar Terus", 80, 1, "🐨"),
        LeaderboardEntry(10, "Pemula Hebat", 50, 1, "🐯")
    )

    fun refresh() {
        loadLeaderboard()
    }
}
