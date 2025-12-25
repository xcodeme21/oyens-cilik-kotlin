package com.oyenscilik.data.remote

import com.oyenscilik.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Content endpoints
    @GET("content/letters")
    suspend fun getLetters(): Response<ApiResponse<List<LetterDto>>>

    @GET("content/letters/order/{id}")
    suspend fun getLetter(@Path("id") id: Int): Response<ApiResponse<LetterDto>>

    @GET("content/numbers")
    suspend fun getNumbers(): Response<ApiResponse<List<NumberDto>>>

    @GET("content/numbers/order/{id}")
    suspend fun getNumber(@Path("id") id: Int): Response<ApiResponse<NumberDto>>

    @GET("content/animals")
    suspend fun getAnimals(): Response<ApiResponse<List<AnimalDto>>>

    @GET("content/animals/order/{id}")
    suspend fun getAnimal(@Path("id") id: Int): Response<ApiResponse<AnimalDto>>
}
