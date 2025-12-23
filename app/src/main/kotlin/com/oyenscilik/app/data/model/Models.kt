package com.oyenscilik.app.data.model

import com.google.gson.annotations.SerializedName

// ==================== API WRAPPER ====================

/**
 * Standardized API response wrapper
 * All API responses follow this format
 */
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T,
    val meta: ApiMeta? = null,
    val timestamp: String
)

data class ApiMeta(
    val page: Int?,
    val limit: Int?,
    val total: Int?,
    val totalPages: Int?
)

data class ApiErrorResponse(
    val success: Boolean,
    val message: String,
    val error: String,
    val statusCode: Int,
    val timestamp: String,
    val path: String?
)

// ==================== AUTH ====================

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val phone: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class GoogleLoginRequest(
    val idToken: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserProfile
)

// ==================== USER ====================

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phone: String?,
    val avatarUrl: String?,
    val authProvider: String,
    val children: List<ChildProfile>?,
    val createdAt: String
)

data class UpdateProfileRequest(
    val name: String?,
    val phone: String?,
    val avatarUrl: String?
)

data class ChildProfile(
    val id: String,
    val name: String,
    val nickname: String?,
    val birthDate: String?,
    val gender: String?,
    val avatarUrl: String?,
    val level: Int,
    val totalStars: Int,
    val streak: Int,
    val lastActiveDate: String?
)

data class CreateChildRequest(
    val name: String,
    val nickname: String?,
    val birthDate: String?,
    val gender: String?
)

// ==================== CONTENT ====================

data class Letter(
    val id: Int,
    val letter: String,
    val lowercase: String,
    val audioUrl: String?,
    val exampleWord: String,
    val exampleImageUrl: String?,
    val order: Int
)

data class NumberContent(
    val id: Int,
    val number: Int,
    val word: String,
    val audioUrl: String?,
    val imageUrl: String?,
    val order: Int
)

data class Animal(
    val id: Int,
    val name: String,
    @SerializedName("name_en")
    val nameEn: String,
    val description: String,
    val funFact: String,
    val imageUrl: String?,
    val audioUrl: String?,
    val difficulty: String
)

// ==================== PROGRESS ====================

data class RecordProgressRequest(
    val contentType: String,
    val contentId: Int,
    val activityType: String,
    val completed: Boolean?,
    val score: Int?,
    val timeSpentSeconds: Int?
)

data class ProgressRecord(
    val id: String,
    val childId: String,
    val contentType: String,
    val contentId: Int,
    val activityType: String,
    val completed: Boolean,
    val score: Int,
    val starsEarned: Int,
    val attempts: Int,
    val timeSpentSeconds: Int,
    val createdAt: String
)

data class ProgressSummary(
    val totalLettersLearned: Int,
    val totalNumbersLearned: Int,
    val totalAnimalsLearned: Int,
    val totalStars: Int,
    val currentStreak: Int,
    val level: Int
)

data class LeaderboardEntry(
    val childId: String,
    val totalStars: Int
)
