package com.example.workoutapp.di

import com.example.workoutapp.domain.session_manager.ActiveWorkoutManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing the ActiveWorkoutManager singleton.
 *
 * The manager maintains state for the currently active workout session
 * and survives configuration changes.
 */
@Module
@InstallIn(SingletonComponent::class)
object ActiveWorkoutManagerModule {

    /**
     * Provides the singleton ActiveWorkoutManager instance.
     */
    @Provides
    @Singleton
    fun provideActiveWorkoutManager(): ActiveWorkoutManager {
        return ActiveWorkoutManager()
    }
}