package com.oyenscilik.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "oyens_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val CHILD_ID_KEY = stringPreferencesKey("child_id")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val GUEST_LESSON_COUNT_KEY = intPreferencesKey("guest_lesson_count")
        private val IS_GUEST_MODE_KEY = stringPreferencesKey("is_guest_mode")
    }

    // Child ID
    suspend fun saveChildId(childId: String) {
        dataStore.edit { prefs ->
            prefs[CHILD_ID_KEY] = childId
        }
    }

    suspend fun getChildId(): String? {
        return dataStore.data.map { prefs ->
            prefs[CHILD_ID_KEY]
        }.first()
    }

    // Auth Tokens
    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = accessToken
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    suspend fun getAccessToken(): String? {
        return dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }.first()
    }

    suspend fun getRefreshToken(): String? {
        return dataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN_KEY]
        }.first()
    }

    suspend fun clearTokens() {
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }

    // Guest Mode
    suspend fun getGuestLessonCount(): Int {
        return dataStore.data.map { prefs ->
            prefs[GUEST_LESSON_COUNT_KEY] ?: 0
        }.first()
    }

    suspend fun incrementGuestLesson() {
        dataStore.edit { prefs ->
            val current = prefs[GUEST_LESSON_COUNT_KEY] ?: 0
            prefs[GUEST_LESSON_COUNT_KEY] = current + 1
        }
    }

    suspend fun clearGuestMode() {
        dataStore.edit { prefs ->
            prefs.remove(GUEST_LESSON_COUNT_KEY)
            prefs.remove(IS_GUEST_MODE_KEY)
        }
    }

    suspend fun isGuestMode(): Boolean {
        val token = getAccessToken()
        return token == null
    }

    suspend fun hasReachedGuestLimit(): Boolean {
        val count = getGuestLessonCount()
        return count >= 5
    }

    // Clear all data
    suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
