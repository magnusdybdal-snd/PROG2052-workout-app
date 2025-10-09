package com.example.workoutapp.di

import android.content.Context
import androidx.room.Room
import com.example.workoutapp.data.database.AppDatabase
import com.example.workoutapp.data.database.dao.HistoryWorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "workout_app_db"
        ).build()

    @Provides
    fun provideHistoryWorkoutDao(db: AppDatabase): HistoryWorkoutDao =
        db.historyWorkoutDao()
}