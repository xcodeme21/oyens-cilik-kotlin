package com.oyenscilik.data.repository

import com.oyenscilik.data.mapper.*
import com.oyenscilik.data.remote.ApiService
import com.oyenscilik.domain.model.*
import com.oyenscilik.domain.repository.ContentRepository
import javax.inject.Inject

class ContentRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ContentRepository {

    override suspend fun getLetters(): Result<List<Letter>> = try {
        val response = api.getLetters()
        if (response.isSuccessful && response.body()?.success == true) {
            val letters = response.body()!!.data?.map { it.toDomain() } ?: emptyList()
            Result.Success(letters)
        } else {
            Result.Error("Failed to fetch letters")
        }
    } catch (e: Exception) {
        Result.Error(e.message ?: "Unknown error")
    }

    override suspend fun getLetter(id: Int): Result<Letter> = try {
        val response = api.getLetter(id)
        if (response.isSuccessful && response.body()?.success == true) {
            response.body()!!.data?.let {
                Result.Success(it.toDomain())
            } ?: Result.Error("Letter not found")
        } else {
            Result.Error("Failed to fetch letter")
        }
    } catch (e: Exception) {
        Result.Error(e.message ?: "Unknown error")
    }

    override suspend fun getNumbers(): Result<List<NumberContent>> = try {
        val response = api.getNumbers()
        if (response.isSuccessful && response.body()?.success == true) {
            val numbers = response.body()!!.data?.map { it.toDomain() } ?: emptyList()
            Result.Success(numbers)
        } else {
            Result.Error("Failed to fetch numbers")
        }
    } catch (e: Exception) {
        Result.Error(e.message ?: "Unknown error")
    }

    override suspend fun getAnimals(): Result<List<Animal>> = try {
        val response = api.getAnimals()
        if (response.isSuccessful && response.body()?.success == true) {
            val animals = response.body()!!.data?.map { it.toDomain() } ?: emptyList()
            Result.Success(animals)
        } else {
            Result.Error("Failed to fetch animals")
        }
    } catch (e: Exception) {
        Result.Error(e.message ?: "Unknown error")
    }

    override suspend fun getNumber(id: Int): Result<NumberContent> = try {
        val response = api.getNumber(id)
        if (response.isSuccessful && response.body()?.success == true) {
            response.body()!!.data?.let {
                Result.Success(it.toDomain())
            } ?: Result.Error("Number not found")
        } else {
            Result.Error("Failed to fetch number")
        }
    } catch (e: Exception) {
        Result.Error(e.message ?: "Unknown error")
    }

    override suspend fun getAnimal(id: Int): Result<Animal> = try {
        val response = api.getAnimal(id)
        if (response.isSuccessful && response.body()?.success == true) {
            response.body()!!.data?.let {
                Result.Success(it.toDomain())
            } ?: Result.Error("Animal not found")
        } else {
            Result.Error("Failed to fetch animal")
        }
    } catch (e: Exception) {
        Result.Error(e.message ?: "Unknown error")
    }
}
