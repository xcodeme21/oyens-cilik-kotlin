package com.oyenscilik.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MonthlyStreakDto(
    val childId: String,
    val month: String,
    val currentStreak: Int,
    val longestStreakThisMonth: Int,
    val totalActiveDays: Int,
    val targetDays: Int,
    val completedDates: List<String>,
    val status: String,
    val achievementPercentage: Int,
    val isTargetMet: Boolean,
    val reward: String? = null
)

@Serializable
data class StreakCalendarDto(
    val month: String,
    val calendar: Map<String, DayDataDto>,
    val stats: CalendarStatsDto
)

@Serializable
data class DayDataDto(
    val isActive: Boolean,
    val lessonCount: Int,
    val stars: Int
)

@Serializable
data class CalendarStatsDto(
    val totalDays: Int,
    val activeDays: Int,
    val totalLessons: Int,
    val totalStars: Int
)
