package com.oyenscilik.app.data.api

import com.oyenscilik.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * API interface untuk Oyens Cilik backend
 * Semua response dibungkus dengan ApiResponse<T>
 */
interface OyensCilikApi {
    
    // ==================== AUTH ====================
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthResponse>>
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>
    
    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<ApiResponse<AuthResponse>>
    
    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<ApiResponse<Unit>>
    
    @POST("auth/google/mobile")
    suspend fun googleLogin(@Body request: GoogleLoginRequest): Response<ApiResponse<AuthResponse>>
    
    // ==================== USERS ====================
    
    @GET("users/me")
    suspend fun getProfile(@Header("Authorization") token: String): Response<ApiResponse<UserProfile>>
    
    @PUT("users/me")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<ApiResponse<UserProfile>>
    
    @GET("users/children")
    suspend fun getChildren(@Header("Authorization") token: String): Response<ApiResponse<List<ChildProfile>>>
    
    @POST("users/children")
    suspend fun createChild(
        @Header("Authorization") token: String,
        @Body request: CreateChildRequest
    ): Response<ApiResponse<ChildProfile>>
    
    @GET("users/children/{id}")
    suspend fun getChild(
        @Header("Authorization") token: String,
        @Path("id") childId: String
    ): Response<ApiResponse<ChildProfile>>
    
    // ==================== CONTENT ====================
    
    @GET("content/letters")
    suspend fun getLetters(): Response<ApiResponse<List<Letter>>>
    
    @GET("content/letters/{id}")
    suspend fun getLetter(@Path("id") letterId: Int): Response<ApiResponse<Letter>>
    
    @GET("content/numbers")
    suspend fun getNumbers(): Response<ApiResponse<List<NumberContent>>>
    
    @GET("content/numbers/{id}")
    suspend fun getNumber(@Path("id") numberId: Int): Response<ApiResponse<NumberContent>>
    
    @GET("content/animals")
    suspend fun getAnimals(@Query("difficulty") difficulty: String? = null): Response<ApiResponse<List<Animal>>>
    
    @GET("content/animals/{id}")
    suspend fun getAnimal(@Path("id") animalId: Int): Response<ApiResponse<Animal>>
    
    @GET("content/animals/quiz")
    suspend fun getAnimalQuiz(@Query("count") count: Int = 5): Response<ApiResponse<List<Animal>>>
    
    // ==================== PROGRESS ====================
    
    @POST("progress/{childId}")
    suspend fun recordProgress(
        @Header("Authorization") token: String,
        @Path("childId") childId: String,
        @Body request: RecordProgressRequest
    ): Response<ApiResponse<ProgressRecord>>
    
    @GET("progress/{childId}/summary")
    suspend fun getProgressSummary(
        @Header("Authorization") token: String,
        @Path("childId") childId: String
    ): Response<ApiResponse<ProgressSummary>>
    
    @GET("progress/leaderboard")
    suspend fun getLeaderboard(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 10
    ): Response<ApiResponse<List<LeaderboardEntry>>>
}
