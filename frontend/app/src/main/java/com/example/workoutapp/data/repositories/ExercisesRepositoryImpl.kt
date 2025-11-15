package com.example.workoutapp.data.repositories

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.data.database.ExerciseInitializer
import com.example.workoutapp.data.database.UserPreferences
import com.example.workoutapp.data.database.dao.ExerciseDao
import com.example.workoutapp.data.database.entities.ExerciseEntity
import com.example.workoutapp.domain.repositories.ExercisesRepository
import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.features.exercises.ExercisesPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ExercisesRepositoryImpl @Inject constructor(
    private val api: ApiService,                // For sync later
    private val dao: ExerciseDao,
    private val initializer: ExerciseInitializer,
    private val userPreferences: UserPreferences // For sync later
) : ExercisesRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            initializer.initializeIfNeeded()
        }
    }

    /**
     * Returns exercises from local database for fast
     * and offline access.
     */
    override suspend fun observeExercises(): Flow<List<Exercise>> {
        return dao.getAllExercises().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // Gets the exercises via the api service and maps it to a domain model version of Exercise(list)
    override suspend fun getExercises(): List<Exercise> {

        val localExercises = dao.getAllExercises().first().map { it.toDomain() }
        return localExercises
    }

    fun ExerciseEntity.toDomain() = Exercise(
        exerciseId = exerciseId,
        name = name,
        targetMuscles = targetMuscles,
        bodyParts = bodyParts,
        equipments = equipments,
        secondaryMuscles = secondaryMuscles,
        gifUrl = gifUrl,
        instructions = instructions
    )
}

