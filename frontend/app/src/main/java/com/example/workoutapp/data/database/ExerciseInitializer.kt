package com.example.workoutapp.data.database

import android.content.Context
import android.util.Log
import com.example.workoutapp.data.database.dao.ExerciseDao
import com.example.workoutapp.data.database.entities.ExerciseEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Initializer for pre-loading the exercise library into Room database.
 *
 * On first app launch, loads ~1500 exercises from a bundled JSON file
 * (assets/exercises.json) into the local database. Subsequent launches
 * skip initialization if exercises are already present.
 *
 * **Usage**: Called automatically by ExercisesRepositoryImpl on app startup.
 *
 * @property context Application context for accessing asset files
 * @property exerciseDao DAO for inserting exercises into Room database
 */
class ExerciseInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val exerciseDao: ExerciseDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Initializes the exercise library if the database is empty.
     *
     * Checks the exercise count in the database. If zero, loads all exercises
     * from the bundled JSON file. If exercises already exist, skips initialization.
     *
     * Runs on IO dispatcher to avoid blocking the main thread during file I/O.
     */
    suspend fun initializeIfNeeded() = withContext(Dispatchers.IO) {
        val count = exerciseDao.getExercisesCount()

        if (count == 0) {
            Log.d("ExerciseInitializer", "Database empty, loading initial exercises")
            loadInitialExercises()
        } else {
            Log.d("ExerciseInitializer", "Database already has $count exercises")
        }
    }

    /**
     * Loads exercises from the bundled JSON file into Room database.
     *
     * Reads exercises.json from app assets, deserializes to DTOs, converts to
     * Room entities, and performs bulk insert into the database.
     *
     * Errors are logged but not propagated - the app can function with an
     * empty exercise library (though user experience would be degraded).
     */
    private suspend fun loadInitialExercises() {
        try {
            val jsonString = context.assets.open("exercises.json").bufferedReader().use {
                it.readText()
            }

            val exerciseDtos = json.decodeFromString<List<ExerciseJsonDto>>(jsonString)

            val entities = exerciseDtos.map { dto ->
                ExerciseEntity(
                    exerciseId = dto.exerciseId,
                    name = dto.name,
                    targetMuscles = dto.targetMuscles,
                    bodyParts = dto.bodyParts,
                    equipments = dto.equipments,
                    secondaryMuscles = dto.secondaryMuscles,
                    gifUrl = dto.gifUrl,
                    instructions = dto.instructions
                )
            }

            exerciseDao.insertExercises(entities)

            Log.d("ExerciseInitializer", "Loaded ${entities.size} exercises into ROOM")
        } catch (e: Exception) {
            Log.e("ExerciseInitializer", "Failed to load initial exercises from JSON", e)
        }
    }

    /**
     * Lightweight DTO matching the JSON structure in assets/exercises.json.
     *
     * Only used for initial deserialization during database seeding.
     * Not exposed outside this class.
     *
     * @property exerciseId Unique identifier for the exercise
     * @property name Display name of the exercise
     * @property targetMuscles Primary muscle groups targeted
     * @property bodyParts Body regions involved
     * @property equipments Required equipment
     * @property secondaryMuscles Additional muscles engaged
     * @property gifUrl URL to animated GIF (nullable in JSON)
     * @property instructions Step-by-step instructions
     */
    @Serializable
    private data class ExerciseJsonDto(
        val exerciseId: String,
        val name: String,
        val targetMuscles: List<String>,
        val bodyParts: List<String>,
        val equipments: List<String>,
        val secondaryMuscles: List<String>,
        val gifUrl: String?,
        val instructions: List<String>
    )
}