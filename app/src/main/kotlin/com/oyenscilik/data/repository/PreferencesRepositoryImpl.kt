package com.oyenscilik.data.repository

import com.oyenscilik.data.local.PreferencesManager
import com.oyenscilik.domain.repository.PreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : PreferencesRepository {

    override suspend fun saveChildId(childId: String) {
        preferencesManager.saveChildId(childId)
    }

    override suspend fun getChildId(): String? {
        return preferencesManager.getChildId()
    }

    override suspend fun clear() {
        preferencesManager.clear()
    }

    // Guest mode
    override suspend fun getGuestLessonCount(): Int {
        return preferencesManager.getGuestLessonCount()
    }

    override suspend fun incrementGuestLesson() {
        preferencesManager.incrementGuestLesson()
    }

    override suspend fun clearGuestMode() {
        preferencesManager.clearGuestMode()
    }

    // Auth tokens
    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        preferencesManager.saveTokens(accessToken, refreshToken)
    }

    override suspend fun getAccessToken(): String? {
        return preferencesManager.getAccessToken()
    }

    override suspend fun getRefreshToken(): String? {
        return preferencesManager.getRefreshToken()
    }

    override suspend fun clearTokens() {
        preferencesManager.clearTokens()
    }
}
