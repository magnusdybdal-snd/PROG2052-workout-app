package com.example.workoutapp.di

import com.example.workoutapp.data.api.KtorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton


const val server: String = "10.212.168.186:8080"
// Special emulator localhost port
const val emulatorHost = "10.0.2.2:8080"



// Module: Marks this object as a collection of providers (a factory class that tells Hilt how to make things)
@Module
// InstallIn(SingletonComponent::class) : "The objects provided here should live in the SingletonComponent.”
// That means: one instance is shared across the whole app.
@InstallIn(SingletonComponent::class)
object KtorClient{

    // Marks a function as a provider method.
    // When something in the app asks for a HttpClient, Hilt calls this function to get it.
    @Provides
    // Tells Hilt to reuse the same instance every time (not create a new one).
    // Perfect for HttpClient because it’s expensive to create.
    @Singleton
    // Actually builds the Ktor HttpClient. (see KtorClient.kt)
    fun provideKtorClient(): HttpClient = KtorClient.instance

    @Provides
    @Singleton
    // Instead of hardcoding the URL everywhere, we provide it once.
    // If you later change servers, only this provider changes.
    fun provideBaseUrl():String = "http://${server}/api/v1"

}