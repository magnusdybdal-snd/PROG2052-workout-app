package com.example.workoutapp.data.repositories

import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
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

    // Cache to hold preloaded and sorted exercises to remove lag
    private val cache = MutableStateFlow<List<Exercise>>(emptyList())

    init {
        CoroutineScope(Dispatchers.IO).launch {
            initializer.initializeIfNeeded()

            // Eager loading. Preload exercises to stop lag (many elements)
            dao.getAllExercises()
                .map { list -> list.map { it.toDomain() } }
                .collect { mapped ->
                    cache.value = mapped.sortedBy {
                        it.name.lowercase()
                    }
                }
        }
    }

    /**
     * Returns exercises from local database cache for fast
     * and offline access.
     */
    override fun observeExercises(): StateFlow<List<Exercise>> = cache

    // Gets the exercises via the api service and maps it to a domain model version of Exercise(list)
    override suspend fun getExercises(): List<Exercise> {

        val localExercises = dao.getAllExercises().first().map { it.toDomain() }
        return localExercises
    }

    fun ExerciseEntity.toDomain() = Exercise(
        exerciseId = exerciseId,
        name = name.toTitleCase(),
        targetMuscles = targetMuscles.map { it.toTitleCase() },
        bodyParts = bodyParts.map { it.toTitleCase() },
        equipments = equipments.map { it.toTitleCase() },
        secondaryMuscles = secondaryMuscles.map { it.toTitleCase() },
        gifUrl = gifUrl,
        instructions = instructions
    )

    // Helper function to format strings with correct capitalisation
    fun String.toTitleCase(): String {
        return split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercase() }
            }
    }
}

