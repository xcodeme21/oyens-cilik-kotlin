package com.oyenscilik.data.repository

import com.oyenscilik.data.mapper.toDomain
import com.oyenscilik.data.remote.ApiService
import com.oyenscilik.domain.model.LeaderboardEntry
import com.oyenscilik.domain.model.Result
import com.oyenscilik.domain.repository.LeaderboardRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaderboardRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : LeaderboardRepository {

    override suspend fun getLeaderboard(limit: Int): Result<List<LeaderboardEntry>> {
        return try {
            val response = apiService.getLeaderboard(limit)
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data
                if (data != null) {
                    Result.Success(data.mapIndexed { index, dto -> dto.toDomain(index + 1) })
                } else {
                    Result.Error("No data in response")
                }
            } else {
                Result.Error(response.body()?.message ?: "Failed to get leaderboard")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }
}
