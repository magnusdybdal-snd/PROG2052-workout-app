package com.example.workoutapp.di

import android.content.Context
import com.example.workoutapp.data.database.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing DataStore-based user preferences.
 *
 * UserPreferences handles persistent storage for JWT tokens and
 * app initialization state.
 */
@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {
    /**
     * Provides the UserPreferences instance backed by DataStore.
     */
    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
}