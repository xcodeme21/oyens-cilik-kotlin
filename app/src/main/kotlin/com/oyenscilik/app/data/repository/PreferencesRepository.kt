package com.oyenscilik.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "oyens_cilik_prefs")

@Singleton
class PreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore
    
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val CURRENT_CHILD_ID = stringPreferencesKey("current_child_id")
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
    }
    
    // Access Token
    val accessToken: Flow<String?> = dataStore.data.map { prefs ->
        prefs[ACCESS_TOKEN]
    }
    
    suspend fun saveAccessToken(token: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = token
        }
    }
    
    // Refresh Token
    val refreshToken: Flow<String?> = dataStore.data.map { prefs ->
        prefs[REFRESH_TOKEN]
    }
    
    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN] = token
        }
    }
    
    // Current Child ID
    val currentChildId: Flow<String?> = dataStore.data.map { prefs ->
        prefs[CURRENT_CHILD_ID]
    }
    
    suspend fun saveCurrentChildId(childId: String) {
        dataStore.edit { prefs ->
            prefs[CURRENT_CHILD_ID] = childId
        }
    }
    
    // User Info
    val userId: Flow<String?> = dataStore.data.map { prefs ->
        prefs[USER_ID]
    }
    
    val userName: Flow<String?> = dataStore.data.map { prefs ->
        prefs[USER_NAME]
    }
    
    suspend fun saveUserInfo(id: String, name: String) {
        dataStore.edit { prefs ->
            prefs[USER_ID] = id
            prefs[USER_NAME] = name
        }
    }
    
    // Clear all data (logout)
    suspend fun clearAll() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
    
    // Check if logged in
    val isLoggedIn: Flow<Boolean> = dataStore.data.map { prefs ->
        !prefs[ACCESS_TOKEN].isNullOrEmpty()
    }
}
