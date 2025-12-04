package com.example.workoutapp.data.database

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore extension for creating the user preferences singleton.
 *
 * Uses Jetpack DataStore (key-value storage) for persistent, type-safe preferences.
 */
val Context.dataStore by preferencesDataStore("user_prefs")

/**
 * Manager for user authentication data persistence.
 *
 * Stores JWT token, user ID, and display name using Jetpack DataStore.
 * DataStore provides reactive Flow-based access and handles data persistence
 * on a background thread automatically.
 *
 * **Usage**: Injected as a singleton via Hilt in AuthViewModel and LoginViewModel.
 *
 * @property context Application context for accessing DataStore
 */
class UserPreferences(private val context: Context) {

    companion object {
        private val USER_ID = stringPreferencesKey("userId")
        private val TOKEN = stringPreferencesKey("token")
        private val NAME = stringPreferencesKey("name")
    }

    /**
     * Saves user authentication data after successful login.
     *
     * Persists JWT token, user ID, and display name to DataStore.
     * This data is used for API authentication and displaying user info in the UI.
     *
     * @param userId The user's unique identifier from the backend
     * @param token JWT token for authenticating API requests
     * @param name User's display name from Google OAuth
     */
    suspend fun saveAuthData(userId: String, token: String, name: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = userId
            prefs[TOKEN] = token
            prefs[NAME] = name
        }
    }

    /**
     * Observable flow of the user's unique identifier.
     *
     * Null when not logged in.
     */
    val userId: Flow<String?> = context.dataStore.data.map { it[USER_ID] }

    /**
     * Observable flow of the JWT authentication token.
     *
     * Used in API requests via Authorization header. Null when not logged in.
     */
    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN] }

    /**
     * Observable flow of the user's display name.
     *
     * Null when not logged in.
     */
    val name: Flow<String?> = context.dataStore.data.map { it[NAME] }

    /**
     * Clears all stored authentication data.
     *
     * Called on logout to remove JWT token and user info from DataStore.
     */
    suspend fun clearAuthData() {
        context.dataStore.edit { it.clear() }
    }
}