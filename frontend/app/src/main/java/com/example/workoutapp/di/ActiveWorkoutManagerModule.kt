package com.example.workoutapp.di

import com.example.workoutapp.domain.session_manager.ActiveWorkoutManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ActiveWorkoutManagerModule {

    @Provides
    @Singleton
    fun provideActiveWorkoutManager(): ActiveWorkoutManager {
        return ActiveWorkoutManager()
    }
}