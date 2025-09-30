package com.example.workoutapp.di

import com.example.workoutapp.app.WorkoutApp_HiltComponents
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkoutTemplateModule {

    @Provides
    @Singleton
}