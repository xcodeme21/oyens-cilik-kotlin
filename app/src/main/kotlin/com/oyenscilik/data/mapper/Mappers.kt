package com.oyenscilik.data.mapper

import com.oyenscilik.data.remote.*
import com.oyenscilik.data.remote.dto.*
import com.oyenscilik.domain.model.*

// Content Mappers
fun LetterDto.toDomain() = Letter(
    id = id,
    letter = letter,
    lowercase = lowercase,
    audioUrl = audioUrl,
    exampleWord = exampleWord,
    exampleImageUrl = exampleImageUrl,
    order = order
)

fun NumberDto.toDomain() = NumberContent(
    id = id,
    number = number,
    word = word,
    audioUrl = audioUrl,
    imageUrl = imageUrl,
    order = order
)

fun AnimalDto.toDomain() = Animal(
    id = id,
    name = name,
    nameEn = name_en,
    description = description,
    funFact = funFact,
    imageUrl = imageUrl,
    audioUrl = audioUrl,
    difficulty = difficulty
)

// Auth Mappers
fun UserDto.toDomain() = User(
    id = id,
    name = name,
    email = email,
    avatarUrl = avatarUrl
)

fun ChildDto.toDomain() = Child(
    id = id,
    name = name,
    nickname = nickname,
    birthDate = birthDate,
    gender = gender,
    avatarUrl = avatarUrl,
    level = level,
    totalStars = totalStars,
    streak = streak,
    lastActiveDate = lastActiveDate,
    totalLessonsCompleted = totalLessonsCompleted,
    favoriteModule = favoriteModule
)

fun AuthResponseDto.toDomain() = AuthResponse(
    accessToken = accessToken,
    refreshToken = refreshToken,
    user = user.toDomain(),
    child = child?.toDomain()
)

// Profile Mappers
fun ModuleProgressDto.toDomain() = ModuleProgress(
    completed = completed,
    total = total
)

fun ActivityHistoryDto.toDomain() = ActivityHistory(
    date = date,
    lessonsCompleted = lessonsCompleted,
    starsEarned = starsEarned,
    minutesPlayed = minutesPlayed,
    lettersLearned = lettersLearned,
    numbersLearned = numbersLearned,
    animalsLearned = animalsLearned
)

fun ProfileSummaryDto.toDomain() = ProfileSummary(
    childId = childId,
    name = name,
    nickname = nickname,
    avatarUrl = avatarUrl,
    level = level,
    levelTitle = levelTitle,
    totalStars = totalStars,
    starsToNextLevel = starsToNextLevel,
    totalLessonsCompleted = totalLessonsCompleted,
    streak = streak,
    daysActive = daysActive,
    favoriteModule = favoriteModule,
    recentActivity = recentActivity.map { it.toDomain() },
    lettersProgress = lettersProgress.toDomain(),
    numbersProgress = numbersProgress.toDomain(),
    animalsProgress = animalsProgress.toDomain()
)

fun LevelInfoDto.toDomain() = LevelInfo(
    level = level,
    title = title,
    totalStars = totalStars,
    starsToNextLevel = starsToNextLevel
)

// Progress Mappers
fun ProgressDto.toDomain() = Progress(
    totalLettersLearned = totalLettersLearned,
    totalNumbersLearned = totalNumbersLearned,
    totalAnimalsLearned = totalAnimalsLearned,
    totalStars = totalStars,
    currentStreak = currentStreak,
    level = level
)

fun ProgressSummaryDto.toDomain() = Progress(
    totalLettersLearned = totalLettersLearned,
    totalNumbersLearned = totalNumbersLearned,
    totalAnimalsLearned = totalAnimalsLearned,
    totalStars = totalStars,
    currentStreak = currentStreak,
    level = level
)

// Leaderboard Mappers
fun LeaderboardEntryDto.toDomain(rank: Int) = LeaderboardEntry(
    rank = rank,
    childId = childId,
    name = name,
    avatarUrl = avatarUrl,
    totalStars = totalStars,
    level = level,
    levelTitle = getLevelTitle(level)
)

// Helper functions
private fun getLevelTitle(level: Int): String {
    return when (level) {
        1 -> "Pemula"
        2 -> "Pelajar"
        3 -> "Mahir"
        4 -> "Ahli"
        5 -> "Master"
        else -> "Pemula"
    }
}

// Monthly Streak Mappers
fun MonthlyStreakDto.toDomain() =
        MonthlyStreak(
                childId = childId,
                month = month,
                currentStreak = currentStreak,
                longestStreakThisMonth = longestStreakThisMonth,
                totalActiveDays = totalActiveDays,
                targetDays = targetDays,
                completedDates = completedDates,
                status =
                        when (status.lowercase()) {
                            "active" -> StreakStatus.ACTIVE
                            "broken" -> StreakStatus.BROKEN
                            else -> StreakStatus.NEW
                        },
                achievementPercentage = achievementPercentage,
                isTargetMet = isTargetMet,
                reward = reward
        )

fun StreakCalendarDto.toDomain() =
        StreakCalendar(
                month = month,
                calendar =
                        calendar.mapValues { (_, dayData) ->
                            DayData(
                                    isActive = dayData.isActive,
                                    lessonCount = dayData.lessonCount,
                                    stars = dayData.stars
                            )
                        },
                stats =
                        CalendarStats(
                                totalDays = stats.totalDays,
                                activeDays = stats.activeDays,
                                totalLessons = stats.totalLessons,
                                totalStars = stats.totalStars
                        )
        )
