package com.example.workoutapp.di

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.data.database.UserPreferences
import com.example.workoutapp.data.repositories.ExercisesRepositoryImpl
import com.example.workoutapp.domain.repositories.ExercisesRepository
import com.example.workoutapp.domain.usecases.GetExercisesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

// This Hilt module provides all dependencies needed for the "Exercise" feature.
// It tells Hilt how to create ApiService, ExerciseRepository, and GetExercisesUseCase.
@Module
@InstallIn(SingletonComponent::class)
object ExerciseModule {

    // Provides the ApiService, which talks to the backend.
    // Hilt automatically injects HttpClient and baseUrl (from NetworkModule).
    @Provides
    @Singleton  // Only one API service will be created over the app
    fun provideApiService(
        client: HttpClient,
        baseUrl: String,
        preferences: UserPreferences
    ): ApiService = ApiService(client, baseUrl,preferences)

    // Provides the repository implementation, but exposes it as the interface type.
    // This decouples the rest of the app from the concrete implementation.
    @Provides
    @Singleton
    fun provideExercisesRepository(
        api: ApiService
    ): ExercisesRepository = ExercisesRepositoryImpl(api)

    // Provides the use case, which is just a thin wrapper around the repository.
    // Now any ViewModel can inject GetExercisesUseCase directly.
    @Provides
    @Singleton
    fun provideExercisesUseCase(
        repo: ExercisesRepository
    ): GetExercisesUseCase = GetExercisesUseCase(repo)
}