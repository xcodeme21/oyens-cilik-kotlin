package com.oyenscilik.domain.model

// Content Models
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
    val nameEn: String,
    val description: String,
    val funFact: String,
    val imageUrl: String?,
    val audioUrl: String?,
    val difficulty: String
)

// Auth Models
data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String?
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: User,
    val child: Child?
)

data class Child(
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

// Profile Models
data class ProfileSummary(
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
    val recentActivity: List<ActivityHistory>,
    val lettersProgress: ModuleProgress,
    val numbersProgress: ModuleProgress,
    val animalsProgress: ModuleProgress
)

data class ModuleProgress(
    val completed: Int,
    val total: Int
) {
    val percentage: Int get() = if (total > 0) (completed * 100 / total) else 0
}

data class ActivityHistory(
    val date: String,
    val lessonsCompleted: Int,
    val starsEarned: Int,
    val minutesPlayed: Int,
    val lettersLearned: Int,
    val numbersLearned: Int,
    val animalsLearned: Int
)

data class LevelInfo(
    val level: Int,
    val title: String,
    val totalStars: Int,
    val starsToNextLevel: Int
)

// Progress Models
data class Progress(
    val totalLettersLearned: Int,
    val totalNumbersLearned: Int,
    val totalAnimalsLearned: Int,
    val totalStars: Int,
    val currentStreak: Int,
    val level: Int
)

data class ProgressRecord(
    val contentType: String, // "letter", "number", "animal"
    val contentId: Int,
    val activityType: String, // "learn", "quiz", "practice"
    val completed: Boolean,
    val score: Int?, // 0-100
    val timeSpentSeconds: Int?
)

// Leaderboard Models
data class LeaderboardEntry(
    val rank: Int,
    val childId: String,
    val name: String,
    val avatarUrl: String?,
    val totalStars: Int,
    val level: Int,
    val levelTitle: String
)

// Quiz Models
data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val imageUrl: String?,
    val audioUrl: String?
)

data class QuizResult(
    val totalQuestions: Int,
    val correctAnswers: Int,
    val score: Int, // 0-100
    val starsEarned: Int,
    val timeSpent: Int
) {
    val percentage: Int get() = if (totalQuestions > 0) (correctAnswers * 100 / totalQuestions) else 0
}

// Common Result wrapper
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val code: Int? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
