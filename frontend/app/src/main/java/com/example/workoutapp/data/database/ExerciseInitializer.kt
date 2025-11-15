package com.example.workoutapp.data.database

import android.content.Context
import android.util.Log
import com.example.workoutapp.data.database.dao.ExerciseDao
import com.example.workoutapp.data.database.entities.ExerciseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ExerciseInitializer @Inject constructor(
    private val context: Context,
    private val exerciseDao: ExerciseDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun initializeIfNeeded() = withContext(Dispatchers.IO) {
        val count = exerciseDao.getExercisesCount()

        if (count == 0) {
            Log.d("ExerciseInitializer", "Database empty, loading initial exercises")
            loadInitialExercises()
        } else {
            Log.d("ExerciseInitializer", "Database already has $count exercises")
        }
    }

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
                    targetMuscles = json.encodeToString(dto.targetMuscles),
                    bodyParts = json.encodeToString(dto.bodyParts),
                    equipments = json.encodeToString(dto.equipments),
                    secondaryMuscles = json.encodeToString(dto.secondaryMuscles),
                    gifUrl = dto.gifUrl,
                    instructions = json.encodeToString(dto.instructions)
                )
            }

            Log.d("ExerciseInitializer", "Loaded ${entities.size} exercises into ROOM")
        } catch (e: Exception) {
            Log.e("ExerciseInitializer", "Failed to load initial exercises from JSON", e)
        }
    }

    /**
     * Lightweight DTO matching the JSON structure.
     * Only used for initial loading, not exposed outside this class.
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