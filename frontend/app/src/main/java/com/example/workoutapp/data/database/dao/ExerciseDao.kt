package com.example.workoutapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workoutapp.data.database.entities.ExerciseEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the exercises table.
 *
 * Provides methods to query, insert, and manage exercises in the local Room database.
 * The exercise library is pre-loaded from assets on first app launch and remains
 * mostly static (~1500 exercises).
 */
@Dao
interface ExerciseDao {

    /**
     * Retrieves all exercises as a reactive Flow, sorted alphabetically by name.
     *
     * @return Flow emitting the complete list of exercises whenever the table changes
     */
    @Query("SELECT * FROM exercises ORDER BY name ASC")
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    /**
     * Fetches a single exercise by its unique ID.
     *
     * @param exerciseId The unique identifier of the exercise
     * @return The exercise entity, or null if not found
     */
    @Query("SELECT * FROM exercises WHERE exerciseId = :exerciseId LIMIT 1")
    fun getExerciseById(exerciseId: String): ExerciseEntity?

    /**
     * Searches exercises by name using a case-insensitive partial match.
     *
     * @param query The search query (matched against exercise names)
     * @return Flow emitting matching exercises sorted alphabetically
     */
    @Query("SELECT * FROM exercises WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchExercises(query: String): Flow<List<ExerciseEntity>>

    /**
     * Inserts or updates a list of exercises in the database.
     *
     * Uses REPLACE conflict strategy to overwrite existing exercises with the same ID.
     *
     * @param exercises The list of exercises to insert or update
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    /**
     * Counts the total number of exercises in the database.
     *
     * Useful for verifying successful initialization of the exercise library.
     *
     * @return The total count of exercises
     */
    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun getExercisesCount(): Int

    /**
     * Deletes all exercises from the database.
     *
     * **Warning**: This will remove the entire exercise library. Use with caution.
     */
    @Query("DELETE FROM exercises")
    suspend fun deleteAll()
}