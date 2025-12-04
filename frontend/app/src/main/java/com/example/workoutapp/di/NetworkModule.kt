package com.example.workoutapp.di

import android.content.Context
import com.example.workoutapp.data.api.KtorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton


/**
 * Server IP address for the backend API.
 * Update this to point to your deployed backend or use [emulatorHost] for local development.
 */
const val server: String = "10.212.168.186"

/**
 * Special localhost address for Android emulator to reach the host machine.
 * Use this when running the backend locally on your development machine.
 */
const val emulatorHost = "10.0.2.2:8080"

/**
 * Hilt module providing network-related dependencies.
 *
 * Provides singleton instances of:
 * - [HttpClient] - Ktor HTTP client for API requests
 * - [String] (base URL) - Backend API base URL
 *
 * The HttpClient is configured with JSON serialization, authentication interceptors,
 * and other settings defined in [com.example.workoutapp.data.api.KtorClient].
 */
@Module
@InstallIn(SingletonComponent::class)
object KtorClient{

    /**
     * Provides the configured Ktor HTTP client instance.
     *
     * The client is created once and reused throughout the app lifecycle.
     * Configuration includes JSON serialization, JWT token authentication,
     * and logging.
     *
     * @param context Application context for accessing resources
     * @return Configured HttpClient singleton
     */
    @Provides
    @Singleton
    fun provideKtorClient(
        @ApplicationContext context: Context
    ): HttpClient = KtorClient.create(context)

    /**
     * Provides the backend API base URL.
     *
     * Centralizes URL configuration so it only needs to be changed in one place.
     *
     * @return Base URL string for all API endpoints
     */
    @Provides
    @Singleton
    fun provideBaseUrl():String = "https://${server}/api/v1"

}