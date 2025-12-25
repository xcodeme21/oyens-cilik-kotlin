package com.oyenscilik.domain.repository

import com.oyenscilik.domain.model.*

interface ContentRepository {
    suspend fun getLetters(): Result<List<Letter>>
    suspend fun getLetter(id: Int): Result<Letter>
    suspend fun getNumbers(): Result<List<NumberContent>>
    suspend fun getNumber(id: Int): Result<NumberContent>
    suspend fun getAnimals(): Result<List<Animal>>
    suspend fun getAnimal(id: Int): Result<Animal>
}
