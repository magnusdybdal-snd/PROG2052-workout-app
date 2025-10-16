package com.example.workoutapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.workoutapp.data.database.dao.HistoryWorkoutDao
import com.example.workoutapp.data.database.entities.HistoryWorkoutEntity
import com.example.workoutapp.data.database.entities.SetEntity
import com.example.workoutapp.data.database.entities.WorkoutExerciseEntity

/**
 * The main Room database for the Workout app.
 *
 * Contains all DAOs and serves as the single access point to persisted local data.
 */
@Database(
    entities = [
        HistoryWorkoutEntity::class,
        WorkoutExerciseEntity::class,
        SetEntity::class],
    version = 3,
    exportSchema = false
)
// Needs a converter as Room does not support Duration and LocalDate
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // Provides access to local CRUD operations for history workouts
    abstract fun historyWorkoutDao(): HistoryWorkoutDao
}