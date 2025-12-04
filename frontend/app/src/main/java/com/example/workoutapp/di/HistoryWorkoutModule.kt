package com.example.workoutapp.di

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.data.database.dao.HistoryWorkoutDao
import com.example.workoutapp.data.repositories.HistoryWorkoutRepositoryImpl
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import com.example.workoutapp.domain.usecases.GetHistoryWorkoutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing dependencies for the History Workout feature.
 *
 * Wires together history-related dependencies including the repository
 * and use case for managing completed workouts.
 */
@Module
@InstallIn(SingletonComponent::class)
object HistoryWorkoutModule {

    @Provides
    @Singleton
    fun provideHistoryWorkoutRepository(
        api: ApiService,
        dao: HistoryWorkoutDao
    ): HistoryWorkoutRepository = HistoryWorkoutRepositoryImpl(api, dao)

    @Provides
    @Singleton
    fun provideHistoryWorkoutUseCase(
        repo: HistoryWorkoutRepository
    ): GetHistoryWorkoutUseCase = GetHistoryWorkoutUseCase(repo)
}