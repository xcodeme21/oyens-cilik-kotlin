package com.oyenscilik.data.remote

import com.oyenscilik.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Content endpoints
    @GET("content/letters")
    suspend fun getLetters(): Response<ApiResponse<List<LetterDto>>>

    @GET("content/letters/{id}")
    suspend fun getLetter(@Path("id") id: Int): Response<ApiResponse<LetterDto>>

    @GET("content/numbers")
    suspend fun getNumbers(): Response<ApiResponse<List<NumberDto>>>

    @GET("content/animals")
    suspend fun getAnimals(): Response<ApiResponse<List<AnimalDto>>>
    
    // Auth endpoints
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthResponseDto>>
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponseDto>>
    
    @POST("auth/google/mobile")
    suspend fun googleLogin(@Body request: GoogleLoginRequest): Response<ApiResponse<AuthResponseDto>>
    
    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<ApiResponse<AuthResponseDto>>
    
    @POST("auth/logout")
    suspend fun logout(): Response<ApiResponse<Unit>>
    
    @GET("auth/me")
    suspend fun getCurrentUser(): Response<ApiResponse<UserDto>>
    
    // User/Child endpoints
    @GET("children/{childId}")
    suspend fun getChild(@Path("childId") childId: String): Response<ApiResponse<ChildDto>>
    
    @POST("users/children")
    suspend fun createChild(@Body request: CreateChildRequest): Response<ApiResponse<ChildDto>>
    
    // Profile endpoints
    @GET("profile/{childId}")
    suspend fun getProfile(@Path("childId") childId: String): Response<ApiResponse<ProfileSummaryDto>>
    
    @GET("profile/{childId}/calendar")
    suspend fun getActivityCalendar(
        @Path("childId") childId: String,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): Response<ApiResponse<List<ActivityHistoryDto>>>
    
    @GET("profile/{childId}/level")
    suspend fun getLevelInfo(@Path("childId") childId: String): Response<ApiResponse<LevelInfoDto>>
    
    // Progress endpoints
    @POST("progress/{childId}")
    suspend fun recordProgress(
        @Path("childId") childId: String,
        @Body request: RecordProgressRequest
    ): Response<ApiResponse<Unit>>
    
    @GET("progress/{childId}")
    suspend fun getProgress(@Path("childId") childId: String): Response<ApiResponse<List<Any>>>
    
    @GET("progress/{childId}/summary")
    suspend fun getProgressSummary(@Path("childId") childId: String): Response<ApiResponse<ProgressSummaryDto>>
    
    // Leaderboard endpoint
    @GET("progress/leaderboard")
    suspend fun getLeaderboard(@Query("limit") limit: Int): Response<ApiResponse<List<LeaderboardEntryDto>>>
}
