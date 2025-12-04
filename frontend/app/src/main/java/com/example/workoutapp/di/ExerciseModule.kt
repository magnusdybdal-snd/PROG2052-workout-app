package com.example.workoutapp.di

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.data.database.ExerciseInitializer
import com.example.workoutapp.data.database.UserPreferences
import com.example.workoutapp.data.database.dao.ExerciseDao
import com.example.workoutapp.data.repositories.ExercisesRepositoryImpl
import com.example.workoutapp.domain.repositories.ExercisesRepository
import com.example.workoutapp.domain.usecases.GetExercisesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

/**
 * Hilt module providing dependencies for the Exercise feature.
 *
 * Wires together the exercise feature's dependencies:
 * - [ApiService] - Backend API client
 * - [ExercisesRepository] - Data access abstraction
 * - [GetExercisesUseCase] - Business logic
 *
 * All dependencies are provided as singletons to ensure single instances
 * across the app and efficient resource usage.
 */
@Module
@InstallIn(SingletonComponent::class)
object ExerciseModule {

    /**
     * Provides the API service for backend communication.
     *
     * Dependencies ([HttpClient], base URL, [UserPreferences]) are automatically
     * injected by Hilt from other modules.
     *
     * @param client HTTP client from [KtorClient] module
     * @param baseUrl Base URL from [KtorClient] module
     * @param preferences User preferences for JWT token storage
     * @return Singleton ApiService instance
     */
    @Provides
    @Singleton
    fun provideApiService(
        client: HttpClient,
        baseUrl: String,
        preferences: UserPreferences
    ): ApiService = ApiService(client, baseUrl,preferences)

    /**
     * Provides the exercises repository implementation.
     *
     * Exposes the implementation as the interface type to decouple consumers
     * from the concrete implementation.
     *
     * @param api API service for remote data
     * @param dao Room DAO for local data
     * @param initializer Helper for pre-loading exercises
     * @param userPreferences For tracking initialization state
     * @return ExercisesRepository instance
     */
    @Provides
    @Singleton
    fun provideExercisesRepository(
        api: ApiService,
        dao: ExerciseDao,
        initializer: ExerciseInitializer,
        userPreferences: UserPreferences
    ): ExercisesRepository = ExercisesRepositoryImpl(api, dao, initializer, userPreferences)

    /**
     * Provides the use case for retrieving exercises.
     *
     * ViewModels can inject this use case to access exercise data without
     * depending directly on the repository.
     *
     * @param repo Exercises repository
     * @return GetExercisesUseCase instance
     */
    @Provides
    @Singleton
    fun provideExercisesUseCase(
        repo: ExercisesRepository
    ): GetExercisesUseCase = GetExercisesUseCase(repo)
}