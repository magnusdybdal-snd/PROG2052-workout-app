package com.example.workoutapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.workoutapp.data.database.dao.history.HistoryWorkoutDao
import com.example.workoutapp.data.database.dao.templates.TemplateDao
import com.example.workoutapp.data.database.entities.history.HistoryWorkoutEntity
import com.example.workoutapp.data.database.entities.history.SetEntity
import com.example.workoutapp.data.database.entities.history.WorkoutExerciseEntity
import com.example.workoutapp.data.database.entities.templates.TemplateEntity
import com.example.workoutapp.data.database.entities.templates.TemplateExerciseEntity
import com.example.workoutapp.data.database.entities.templates.TemplateSetEntity

/**
 * The main Room database for the Workout app.
 *
 * Contains all DAOs and serves as the single access point to persisted local data.
 */
@Database(
    entities = [
        // History entities
        HistoryWorkoutEntity::class,
        WorkoutExerciseEntity::class,
        SetEntity::class,

        // Template entities
        TemplateEntity::class,
        TemplateExerciseEntity::class,
        TemplateSetEntity::class
       ],
    version = 11,
    exportSchema = false
)
// Needs a converter as Room does not support Duration and LocalDate
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // Provides access to local CRUD operations for history workouts
    abstract fun historyWorkoutDao(): HistoryWorkoutDao
    abstract fun workoutTemplateDao(): TemplateDao
}