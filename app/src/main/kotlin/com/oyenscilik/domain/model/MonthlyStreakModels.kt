package com.oyenscilik.domain.model

// Add to existing Models.kt file

/**
 * Monthly Streak Data
 */
data class MonthlyStreak(
    val childId: String,
    val month: String, // Format: YYYY-MM
    val currentStreak: Int,
    val longestStreakThisMonth: Int,
    val totalActiveDays: Int,
    val targetDays: Int,
    val completedDates: List<String>,
    val status: StreakStatus,
    val achievementPercentage: Int,
    val isTargetMet: Boolean,
    val reward: String?
)

enum class StreakStatus {
    ACTIVE, BROKEN, NEW
}

/**
 * Streak Calendar with detailed day data
 */
data class StreakCalendar(
    val month: String,
    val calendar: Map<String, DayData>,
    val stats: CalendarStats
)

data class DayData(
    val isActive: Boolean,
    val lessonCount: Int,
    val stars: Int
)

data class CalendarStats(
    val totalDays: Int,
    val activeDays: Int,
    val totalLessons: Int,
    val totalStars: Int
)
