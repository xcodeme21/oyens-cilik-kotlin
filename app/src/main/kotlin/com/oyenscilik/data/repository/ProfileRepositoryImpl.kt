package com.oyenscilik.data.repository

import com.oyenscilik.data.local.PreferencesManager
import com.oyenscilik.data.mapper.toDomain
import com.oyenscilik.data.remote.ApiService
import com.oyenscilik.domain.model.*
import com.oyenscilik.domain.repository.ProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager
) : ProfileRepository {

    override suspend fun getProfile(childId: String): Result<ProfileSummary> {
        return try {
            val response = apiService.getProfile(childId)
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data
                if (data != null) {
                    Result.Success(data.toDomain())
                } else {
                    Result.Error("No data in response")
                }
            } else {
                Result.Error(response.body()?.message ?: "Failed to get profile")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getActivityCalendar(
        childId: String,
        startDate: String,
        endDate: String
    ): Result<List<ActivityHistory>> {
        return try {
            val response = apiService.getActivityCalendar(childId, startDate, endDate)
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data
                if (data != null) {
                    Result.Success(data.map { it.toDomain() })
                } else {
                    Result.Error("No data in response")
                }
            } else {
                Result.Error(response.body()?.message ?: "Failed to get calendar")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getLevelInfo(childId: String): Result<LevelInfo> {
        return try {
            val response = apiService.getLevelInfo(childId)
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data
                if (data != null) {
                    Result.Success(data.toDomain())
                } else {
                    Result.Error("No data in response")
                }
            } else {
                Result.Error(response.body()?.message ?: "Failed to get level info")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getMonthlyStreak(childId: String, month: String?): Result<MonthlyStreak> {
        return try {
            val response = apiService.getMonthlyStreak(childId, month)
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data
                if (data != null) {
                    Result.Success(data.toDomain())
                } else {
                    Result.Error("No data in response")
                }
            } else {
                Result.Error(response.body()?.message ?: "Failed to get monthly streak")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getStreakCalendar(childId: String, month: String?): Result<StreakCalendar> {
        return try {
            val response = apiService.getStreakCalendar(childId, month)
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data
                if (data != null) {
                    Result.Success(data.toDomain())
                } else {
                    Result.Error("No data in response")
                }
            } else {
                Result.Error(response.body()?.message ?: "Failed to get streak calendar")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }
}
