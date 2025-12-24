package com.oyenscilik.domain.repository

import com.oyenscilik.domain.model.*

interface ContentRepository {
    suspend fun getLetters(): Result<List<Letter>>
    suspend fun getLetter(id: Int): Result<Letter>
    suspend fun getNumbers(): Result<List<NumberContent>>
    suspend fun getAnimals(): Result<List<Animal>>
}

interface AuthRepository {
    suspend fun register(email: String, password: String, name: String): Result<AuthResponse>
    suspend fun login(email: String, password: String): Result<AuthResponse>
    suspend fun googleLogin(idToken: String): Result<AuthResponse>
    suspend fun refreshToken(refreshToken: String): Result<AuthResponse>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentUser(): Result<User>
}

interface ProfileRepository {
    suspend fun getProfile(childId: String): Result<ProfileSummary>
    suspend fun getActivityCalendar(
        childId: String,
        startDate: String,
        endDate: String
    ): Result<List<ActivityHistory>>
    suspend fun getLevelInfo(childId: String): Result<LevelInfo>
suspend fun getMonthlyStreak(childId: String, month: String? = null): Result<MonthlyStreak>

suspend fun getStreakCalendar(childId: String, month: String? = null): Result<StreakCalendar>

}

interface ProgressRepository {
    suspend fun recordProgress(childId: String, progress: ProgressRecord): Result<Unit>
    suspend fun getProgressSummary(childId: String): Result<Progress>
    suspend fun getContentProgress(childId: String, contentType: String): Result<List<Any>>
}

interface LeaderboardRepository {
    suspend fun getLeaderboard(limit: Int): Result<List<LeaderboardEntry>>
}

interface UserRepository {
    suspend fun getChild(childId: String): Result<Child>
    suspend fun getProgress(childId: String): Result<Progress>
    suspend fun createChild(
        name: String,
        nickname: String?,
        birthDate: String?,
        gender: String?,
        avatarUrl: String?
    ): Result<Child>
}

interface PreferencesRepository {
    suspend fun saveChildId(childId: String)
    suspend fun getChildId(): String?
    suspend fun clear()
    
    // Guest mode
    suspend fun getGuestLessonCount(): Int
    suspend fun incrementGuestLesson()
    suspend fun clearGuestMode()
    
    // Auth tokens
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clearTokens()
}
