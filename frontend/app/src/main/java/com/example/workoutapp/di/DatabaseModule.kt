package com.example.workoutapp.di

import android.content.Context
import androidx.room.Room
import com.example.workoutapp.data.database.AppDatabase
import com.example.workoutapp.data.database.dao.HistoryWorkoutDao
import com.example.workoutapp.data.database.dao.TemplateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Hilt module that provides Room database instances and DAOs.
 *
 * The database is built once (singleton) and shared across the entire app.
 * DAOs are lightweight and can be provided directly from the database instance.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "workout_app_db"
        )
            // During development: if schema changes, drop all tables and rebuild.
            // In production, replace this with proper migrations.
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun provideHistoryWorkoutDao(db: AppDatabase): HistoryWorkoutDao =
        db.historyWorkoutDao()

    @Provides
    fun provideWorkoutTemplateDao(db: AppDatabase): TemplateDao =
        db.workoutTemplateDao()
}