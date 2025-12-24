package com.oyenscilik.data.repository

import com.oyenscilik.data.local.PreferencesManager
import com.oyenscilik.data.mapper.toDomain
import com.oyenscilik.data.remote.ApiService
import com.oyenscilik.data.remote.dto.GoogleLoginRequest
import com.oyenscilik.data.remote.dto.LoginRequest
import com.oyenscilik.data.remote.dto.RefreshTokenRequest
import com.oyenscilik.data.remote.dto.RegisterRequest
import com.oyenscilik.domain.model.AuthResponse
import com.oyenscilik.domain.model.Result
import com.oyenscilik.domain.model.User
import com.oyenscilik.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): Result<AuthResponse> {
        return try {
            val response = apiService.register(
                RegisterRequest(email, password, name)
            )
            
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()?.data
                if (data != null) {
                    val authResponse = data.toDomain()
                    
                    // Save tokens
                    preferencesManager.saveTokens(
                        authResponse.accessToken,
                        authResponse.refreshToken
                    )
                    
                    // Clear guest mode
                    preferencesManager.clearGuestMode()
                    
                    Result.Success(authResponse)
                } else {
                    Result.Error("No data in response")
                }
            } else {
                Result.Error(response.body()?.message ?: "Registration failed", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()?.data
                if (data != null) {
                    val authResponse = data.toDomain()
                    
                    // Save tokens
                    preferencesManager.saveTokens(
                        authResponse.accessToken,
                        authResponse.refreshToken
                    )
                    
                    // Save child ID if exists
                    authResponse.child?.let {
                        preferencesManager.saveChildId(it.id)
                    }
                    
                    // Clear guest mode
                    preferencesManager.clearGuestMode()
                    
                    Result.Success(authResponse)
                } else {
                    Result.Error("No data in response")
                }
            } else {
                Result.Error(response.body()?.message ?: "Login failed", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun googleLogin(idToken: String): Result<AuthResponse> {
        return try {
            val response = apiService.googleLogin(GoogleLoginRequest(idToken))
            
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()?.data
                if (data != null) {
                    val authResponse = data.toDomain()
                    
                    // Save tokens
                    preferencesManager.saveTokens(
                        authResponse.accessToken,
                        authResponse.refreshToken
                    )
                    
                    // Save child ID if exists
                    authResponse.child?.let {
                        preferencesManager.saveChildId(it.id)
                    }
                    
                    // Clear guest mode
                    preferencesManager.clearGuestMode()
                    
                    Result.Success(authResponse)
                } else {
                    Result.Error("No data in response")
                }
            } else {
                Result.Error(response.body()?.message ?: "Google login failed", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthResponse> {
        return try {
            val response = apiService.refreshToken(RefreshTokenRequest(refreshToken))
            
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()?.data
                if (data != null) {
                    val authResponse = data.toDomain()
                    
                    // Save new tokens
                    preferencesManager.saveTokens(
                        authResponse.accessToken,
                        authResponse.refreshToken
                    )
                    
                    Result.Success(authResponse)
                } else {
                    Result.Error("No data in response")
                }
            } else {
                Result.Error(response.body()?.message ?: "Token refresh failed", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            apiService.logout()
            
            // Clear all local data
            preferencesManager.clearTokens()
            preferencesManager.clear()
            
            Result.Success(Unit)
        } catch (e: Exception) {
            // Clear local data even if API call fails
            preferencesManager.clearTokens()
            preferencesManager.clear()
            
            Result.Success(Unit)
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            val response = apiService.getCurrentUser()
            
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()?.data
                if (data != null) {
                    Result.Success(data.toDomain())
                } else {
                    Result.Error("No data in response")
                }
            } else {
                Result.Error(response.body()?.message ?: "Failed to get user", response.code())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }
}
