package com.oyenscilik.app.data.repository

import com.oyenscilik.app.data.api.OyensCilikApi
import com.oyenscilik.app.data.model.*
import com.oyenscilik.app.data.network.NetworkHandler
import com.oyenscilik.app.data.network.NetworkResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: OyensCilikApi,
    private val network: NetworkHandler,
    private val prefs: PreferencesRepository
) {
    suspend fun register(
        name: String, 
        email: String, 
        password: String, 
        phone: String? = null
    ): NetworkResult<AuthResponse> {
        return network.execute {
            api.register(RegisterRequest(name, email, password, phone))
        }
    }
    
    suspend fun login(email: String, password: String): NetworkResult<AuthResponse> {
        val result = network.execute { api.login(LoginRequest(email, password)) }
        
        // Auto save tokens on success
        if (result is NetworkResult.Success) {
            saveAuthData(result.data)
        }
        
        return result
    }
    
    suspend fun googleLogin(idToken: String): NetworkResult<AuthResponse> {
        val result = network.execute { api.googleLogin(GoogleLoginRequest(idToken)) }
        
        if (result is NetworkResult.Success) {
            saveAuthData(result.data)
        }
        
        return result
    }
    
    suspend fun refreshToken(): NetworkResult<AuthResponse> {
        val refreshToken = prefs.refreshToken.first() ?: return NetworkResult.Error(
            message = "Tidak ada refresh token",
            errorType = com.oyenscilik.app.data.network.ErrorType.UNAUTHORIZED
        )
        
        val result = network.execute { api.refreshToken(RefreshTokenRequest(refreshToken)) }
        
        if (result is NetworkResult.Success) {
            saveAuthData(result.data)
        }
        
        return result
    }
    
    suspend fun logout() {
        prefs.clearAll()
    }
    
    private suspend fun saveAuthData(auth: AuthResponse) {
        prefs.saveAccessToken(auth.accessToken)
        prefs.saveRefreshToken(auth.refreshToken)
        prefs.saveUserInfo(auth.user.id, auth.user.name)
    }
    
    suspend fun getAuthHeader(): String {
        val token = prefs.accessToken.first() ?: ""
        return "Bearer $token"
    }
}

@Singleton
class ContentRepository @Inject constructor(
    private val api: OyensCilikApi,
    private val network: NetworkHandler
) {
    // Letters - dengan retry untuk konten penting
    suspend fun getLetters(): NetworkResult<List<Letter>> = 
        network.execute(maxRetries = 2) { api.getLetters() }
    
    suspend fun getLetter(id: Int): NetworkResult<Letter> = 
        network.execute { api.getLetter(id) }
    
    // Numbers
    suspend fun getNumbers(): NetworkResult<List<NumberContent>> = 
        network.execute(maxRetries = 2) { api.getNumbers() }
    
    suspend fun getNumber(id: Int): NetworkResult<NumberContent> = 
        network.execute { api.getNumber(id) }
    
    // Animals
    suspend fun getAnimals(difficulty: String? = null): NetworkResult<List<Animal>> = 
        network.execute(maxRetries = 2) { api.getAnimals(difficulty) }
    
    suspend fun getAnimal(id: Int): NetworkResult<Animal> = 
        network.execute { api.getAnimal(id) }
    
    suspend fun getAnimalQuiz(count: Int = 5): NetworkResult<List<Animal>> = 
        network.execute { api.getAnimalQuiz(count) }
}

@Singleton
class ProgressRepository @Inject constructor(
    private val api: OyensCilikApi,
    private val network: NetworkHandler,
    private val authRepo: AuthRepository
) {
    suspend fun recordProgress(
        childId: String,
        contentType: String,
        contentId: Int,
        activityType: String,
        completed: Boolean? = null,
        score: Int? = null,
        timeSpentSeconds: Int? = null
    ): NetworkResult<ProgressRecord> {
        val token = authRepo.getAuthHeader()
        return network.execute {
            api.recordProgress(
                token, childId,
                RecordProgressRequest(contentType, contentId, activityType, completed, score, timeSpentSeconds)
            )
        }
    }
    
    suspend fun getProgressSummary(childId: String): NetworkResult<ProgressSummary> {
        val token = authRepo.getAuthHeader()
        return network.execute { api.getProgressSummary(token, childId) }
    }
    
    suspend fun getLeaderboard(limit: Int = 10): NetworkResult<List<LeaderboardEntry>> {
        val token = authRepo.getAuthHeader()
        return network.execute { api.getLeaderboard(token, limit) }
    }
}

@Singleton 
class UserRepository @Inject constructor(
    private val api: OyensCilikApi,
    private val network: NetworkHandler,
    private val authRepo: AuthRepository
) {
    suspend fun getProfile(): NetworkResult<UserProfile> {
        val token = authRepo.getAuthHeader()
        return network.execute { api.getProfile(token) }
    }
    
    suspend fun updateProfile(request: UpdateProfileRequest): NetworkResult<UserProfile> {
        val token = authRepo.getAuthHeader()
        return network.execute { api.updateProfile(token, request) }
    }
    
    suspend fun getChildren(): NetworkResult<List<ChildProfile>> {
        val token = authRepo.getAuthHeader()
        return network.execute { api.getChildren(token) }
    }
    
    suspend fun createChild(request: CreateChildRequest): NetworkResult<ChildProfile> {
        val token = authRepo.getAuthHeader()
        return network.execute { api.createChild(token, request) }
    }
    
    suspend fun getChild(childId: String): NetworkResult<ChildProfile> {
        val token = authRepo.getAuthHeader()
        return network.execute { api.getChild(token, childId) }
    }
}
