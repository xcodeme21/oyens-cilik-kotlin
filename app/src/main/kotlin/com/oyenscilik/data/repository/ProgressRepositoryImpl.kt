package com.oyenscilik.data.repository

import com.oyenscilik.data.mapper.toDomain
import com.oyenscilik.data.remote.ApiService
import com.oyenscilik.data.remote.dto.RecordProgressRequest
import com.oyenscilik.domain.model.Progress
import com.oyenscilik.domain.model.ProgressRecord
import com.oyenscilik.domain.model.Result
import com.oyenscilik.domain.repository.ProgressRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProgressRepository {

    override suspend fun recordProgress(childId: String, progress: ProgressRecord): Result<Unit> {
        return try {
            val request = RecordProgressRequest(
                contentType = progress.contentType,
                contentId = progress.contentId,
                activityType = progress.activityType,
                completed = progress.completed,
                score = progress.score,
                timeSpentSeconds = progress.timeSpentSeconds
            )
            val response = apiService.recordProgress(childId, request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.Success(Unit)
            } else {
                Result.Error(response.body()?.message ?: "Failed to record progress")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getProgressSummary(childId: String): Result<Progress> {
        return try {
            val response = apiService.getProgressSummary(childId)
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data
                if (data != null) {
                    Result.Success(data.toDomain())
                } else {
                    Result.Error("No data in response")
                }
            } else {
                Result.Error(response.body()?.message ?: "Failed to get progress summary")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getContentProgress(childId: String, contentType: String): Result<List<Any>> {
        return try {
            val response = apiService.getProgress(childId)
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data
                if (data != null) {
                    Result.Success(data)
                } else {
                    Result.Error("No data in response")
                }
            } else {
                Result.Error(response.body()?.message ?: "Failed to get content progress")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }
}
