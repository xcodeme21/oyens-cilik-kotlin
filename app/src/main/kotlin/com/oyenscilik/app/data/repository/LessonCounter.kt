package com.oyenscilik.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "lesson_counter")

/**
 * Tracks lessons completed and manages login requirement.
 * After 5 total lessons (from any category), login is required.
 */
@Singleton
class LessonCounter @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val LESSONS_COMPLETED = intPreferencesKey("lessons_completed")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val DEVICE_ID = stringPreferencesKey("device_id")
        const val FREE_LESSON_LIMIT = 5
    }
    
    private val dataStore = context.dataStore
    
    /**
     * Get current count of completed lessons
     */
    val lessonsCompleted: Flow<Int> = dataStore.data.map { prefs ->
        prefs[LESSONS_COMPLETED] ?: 0
    }
    
    /**
     * Check if user is logged in
     */
    val isLoggedIn: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[IS_LOGGED_IN] ?: false
    }
    
    /**
     * Check if login is required (5 lessons completed and not logged in)
     */
    val shouldShowLogin: Flow<Boolean> = dataStore.data.map { prefs ->
        val completed = prefs[LESSONS_COMPLETED] ?: 0
        val loggedIn = prefs[IS_LOGGED_IN] ?: false
        completed >= FREE_LESSON_LIMIT && !loggedIn
    }
    
    /**
     * Get device ID for anonymous progress tracking
     */
    suspend fun getDeviceId(): String {
        val prefs = dataStore.data.first()
        return prefs[DEVICE_ID] ?: run {
            val newId = UUID.randomUUID().toString()
            dataStore.edit { it[DEVICE_ID] = newId }
            newId
        }
    }
    
    /**
     * Increment lesson count after completing a lesson
     */
    suspend fun incrementLessonCount() {
        dataStore.edit { prefs ->
            val current = prefs[LESSONS_COMPLETED] ?: 0
            prefs[LESSONS_COMPLETED] = current + 1
        }
    }
    
    /**
     * Get current lesson count synchronously
     */
    suspend fun getCurrentCount(): Int {
        return dataStore.data.first()[LESSONS_COMPLETED] ?: 0
    }
    
    /**
     * Check if can access lesson (not reached limit or already logged in)
     */
    suspend fun canAccessLesson(): Boolean {
        val prefs = dataStore.data.first()
        val completed = prefs[LESSONS_COMPLETED] ?: 0
        val loggedIn = prefs[IS_LOGGED_IN] ?: false
        return loggedIn || completed < FREE_LESSON_LIMIT
    }
    
    /**
     * Mark user as logged in
     */
    suspend fun setLoggedIn(loggedIn: Boolean) {
        dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = loggedIn
        }
    }
    
    /**
     * Reset lesson count (used when migrating anonymous progress)
     */
    suspend fun resetLessonCount() {
        dataStore.edit { prefs ->
            prefs[LESSONS_COMPLETED] = 0
        }
    }
}
