package com.example.workoutapp.data.database

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 *  Used for storing jwt tokens, and user id.
 *  Uses key value store
* */

val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(private val context: Context) {
    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val TOKEN = stringPreferencesKey("token")
    }

    suspend fun saveAuthData(userId: String, token: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = userId
            prefs[TOKEN] = token
        }
    }

    val userId: Flow<String?> = context.dataStore.data.map { it[USER_ID] }
    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN] }

    suspend fun clearAuthData() {
        context.dataStore.edit { it.clear() }
    }
}