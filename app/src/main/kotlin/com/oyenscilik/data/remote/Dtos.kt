package com.oyenscilik.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?,
    val timestamp: String
)

@Serializable
data class LetterDto(
    val id: Int,
    val letter: String,
    val lowercase: String,
    val audioUrl: String?,
    val exampleWord: String,
    val exampleImageUrl: String?,
    val order: Int
)

@Serializable
data class NumberDto(
    val id: Int,
    val number: Int,
    val word: String,
    val audioUrl: String?,
    val imageUrl: String?,
    val order: Int
)

@Serializable
data class AnimalDto(
    val id: Int,
    val name: String,
    val nameEn: String,
    val description: String,
    val funFact: String,
    val imageUrl: String?,
    val audioUrl: String?,
    val order: Int
)

@Serializable
data class ProgressDto(
    val totalLettersLearned: Int,
    val totalNumbersLearned: Int,
    val totalAnimalsLearned: Int,
    val totalStars: Int,
    val currentStreak: Int,
    val level: Int
)
