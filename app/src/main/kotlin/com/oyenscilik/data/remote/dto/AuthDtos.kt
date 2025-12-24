package com.oyenscilik.data.remote.dto

import kotlinx.serialization.Serializable

// Auth DTOs
@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class GoogleLoginRequest(
    val idToken: String
)

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)

@Serializable
data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto,
    val child: ChildDto?
)

@Serializable
data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String?
)

// Note: ChildDto now extended in Dtos.kt with additional fields
@Serializable
data class ChildDto(
    val id: String,
    val name: String,
    val nickname: String?,
    val birthDate: String?,
    val gender: String?,
    val avatarUrl: String?,
    val level: Int,
    val totalStars: Int,
    val streak: Int,
    val lastActiveDate: String?,
    val totalLessonsCompleted: Int,
    val favoriteModule: String?
)

@Serializable
data class CreateChildRequest(
    val name: String,
    val nickname: String?,
    val birthDate: String?,
    val gender: String?,
    val avatarUrl: String?
)

// Profile DTOs
@Serializable
data class ProfileSummaryDto(
    val childId: String,
    val name: String,
    val nickname: String?,
    val avatarUrl: String?,
    val level: Int,
    val levelTitle: String,
    val totalStars: Int,
    val starsToNextLevel: Int,
    val totalLessonsCompleted: Int,
    val streak: Int,
    val daysActive: Int,
    val favoriteModule: String?,
    val recentActivity: List<ActivityHistoryDto>,
    val lettersProgress: ModuleProgressDto,
    val numbersProgress: ModuleProgressDto,
    val animalsProgress: ModuleProgressDto
)

@Serializable
data class ModuleProgressDto(
    val completed: Int,
    val total: Int
)

@Serializable
data class ActivityHistoryDto(
    val date: String,
    val lessonsCompleted: Int,
    val starsEarned: Int,
    val minutesPlayed: Int,
    val lettersLearned: Int,
    val numbersLearned: Int,
    val animalsLearned: Int
)

@Serializable
data class LevelInfoDto(
    val level: Int,
    val title: String,
    val totalStars: Int,
    val starsToNextLevel: Int
)

// Progress DTOs
@Serializable
data class RecordProgressRequest(
    val contentType: String,
    val contentId: Int,
    val activityType: String,
    val completed: Boolean?,
    val score: Int?,
    val timeSpentSeconds: Int?
)

@Serializable
data class ProgressSummaryDto(
    val totalLettersLearned: Int,
    val totalNumbersLearned: Int,
    val totalAnimalsLearned: Int,
    val totalStars: Int,
    val currentStreak: Int,
    val level: Int
)

// Leaderboard DTOs
@Serializable
data class LeaderboardEntryDto(
    val childId: String,
    val name: String,
    val avatarUrl: String?,
    val totalStars: Int,
    val level: Int
)
