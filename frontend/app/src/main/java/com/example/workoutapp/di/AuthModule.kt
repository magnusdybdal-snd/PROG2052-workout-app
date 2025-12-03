package com.example.workoutapp.di

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.data.repositories.AuthRepositoryImpl
import com.example.workoutapp.domain.repositories.AuthRepository
import com.example.workoutapp.domain.usecases.LoginWithGoogleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

/**
 * Hilt module providing dependencies for user authentication.
 *
 * Wires together authentication-related dependencies including the
 * repository and use case for Google OAuth login.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    /**
     * Provides the authentication repository implementation.
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        api: ApiService
    ): AuthRepository = AuthRepositoryImpl(api)

    // Provide th use case
    @Provides
    @Singleton
    fun provideLoginWithGoogleUseCase(
        repository: AuthRepository
    ): LoginWithGoogleUseCase = LoginWithGoogleUseCase(repository)
}