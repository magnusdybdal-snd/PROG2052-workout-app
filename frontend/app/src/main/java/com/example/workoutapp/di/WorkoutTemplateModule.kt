package com.example.workoutapp.di

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.data.database.dao.templates.TemplateDao
import com.example.workoutapp.data.repositories.WorkoutTemplateRepositoryImpl
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import com.example.workoutapp.domain.usecases.GetWorkoutTemplatesUseCase
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