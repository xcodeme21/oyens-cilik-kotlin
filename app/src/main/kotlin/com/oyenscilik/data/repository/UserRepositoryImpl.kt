package com.oyenscilik.data.repository

import com.oyenscilik.data.mapper.toDomain
import com.oyenscilik.data.remote.ApiService
import com.oyenscilik.data.remote.dto.CreateChildRequest
import com.oyenscilik.domain.model.*
import com.oyenscilik.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: ApiService
) : UserRepository {

    override suspend fun getChild(childId: String): Result<Child> = try {
        val response = api.getChild(childId)
        if (response.isSuccessful && response.body()?.success == true) {
            response.body()!!.data?.let {
                Result.Success(it.toDomain())
            } ?: Result.Error("Child not found")
        } else {
            Result.Error("Failed to fetch child")
        }
    } catch (e: Exception) {
        Result.Error(e.message ?: "Unknown error")
    }

    override suspend fun getProgress(childId: String): Result<Progress> = try {
        val response = api.getProgressSummary(childId)
        if (response.isSuccessful && response.body()?.success == true) {
            response.body()!!.data?.let {
                Result.Success(it.toDomain())
            } ?: Result.Error("Progress not found")
        } else {
            Result.Error("Failed to fetch progress")
        }
    } catch (e: Exception) {
        Result.Error(e.message ?: "Unknown error")
    }

    override suspend fun createChild(
        name: String,
        nickname: String?,
        birthDate: String?,
        gender: String?,
        avatarUrl: String?
    ): Result<Child> = try {
        val request = CreateChildRequest(name, nickname, birthDate, gender, avatarUrl)
        val response = api.createChild(request)
        if (response.isSuccessful && response.body()?.success == true) {
            response.body()!!.data?.let {
                Result.Success(it.toDomain())
            } ?: Result.Error("Failed to create child")
        } else {
            Result.Error(response.body()?.message ?: "Failed to create child")
        }
    } catch (e: Exception) {
        Result.Error(e.message ?: "Unknown error")
    }
}
