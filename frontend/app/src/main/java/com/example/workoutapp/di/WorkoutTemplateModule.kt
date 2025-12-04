package com.example.workoutapp.di

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.data.database.dao.TemplateDao
import com.example.workoutapp.data.repositories.WorkoutTemplateRepositoryImpl
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import com.example.workoutapp.domain.usecases.GetWorkoutTemplatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing dependencies for the Workout Template feature.
 *
 * Wires together template-related dependencies including the repository
 * and use case for template CRUD operations.
 */
@Module
@InstallIn(SingletonComponent::class)
object WorkoutTemplateModule {

    @Provides
    @Singleton
    fun provideWorkoutTemplateRepository(
        api: ApiService,
        dao: TemplateDao
    ): WorkoutTemplateRepository = WorkoutTemplateRepositoryImpl(api, dao)

    @Provides
    @Singleton
    fun provideWorkoutTemplateUseCase(
        repo: WorkoutTemplateRepository
    ): GetWorkoutTemplatesUseCase = GetWorkoutTemplatesUseCase(repo)
}