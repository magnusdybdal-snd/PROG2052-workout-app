package com.example.workoutapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.workoutapp.data.database.dao.ExerciseDao
import com.example.workoutapp.data.database.dao.HistoryWorkoutDao
import com.example.workoutapp.data.database.dao.TemplateDao
import com.example.workoutapp.data.database.entities.ExerciseEntity
import com.example.workoutapp.data.database.entities.history.HistoryWorkoutEntity
import com.example.workoutapp.data.database.entities.history.SetEntity
import com.example.workoutapp.data.database.entities.history.WorkoutExerciseEntity
import com.example.workoutapp.data.database.entities.templates.TemplateEntity
import com.example.workoutapp.data.database.entities.templates.TemplateExerciseEntity
import com.example.workoutapp.data.database.entities.templates.TemplateSetEntity

/**
 * The main Room database for the Workout app.
 *
 * Serves as the single source of truth for all local data, including:
 * - **Exercise Library**: Pre-loaded ~1500 exercises from assets
 * - **Workout Templates**: User-created workout plans
 * - **Workout History**: Completed workouts with sets and notes
 *
 * The database uses a relational structure with separate tables for workouts/templates
 * and their nested exercises/sets, connected via foreign key relationships.
 *
 * **Synchronization**: Room is authoritative; repositories sync with backend API
 * for backup and multi-device support.
 *
 * **Current Version**: 15 (incremented on schema changes)
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
        TemplateSetEntity::class,

        // Exercise entities
        ExerciseEntity::class
       ],
    version = 15,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to workout history CRUD operations.
     *
     * @return DAO for managing completed workouts
     */
    abstract fun historyWorkoutDao(): HistoryWorkoutDao

    /**
     * Provides access to workout template CRUD operations.
     *
     * @return DAO for managing workout templates
     */
    abstract fun workoutTemplateDao(): TemplateDao

    /**
     * Provides access to exercise library operations.
     *
     * @return DAO for managing the exercise library
     */
    abstract fun ExerciseDao(): ExerciseDao
}