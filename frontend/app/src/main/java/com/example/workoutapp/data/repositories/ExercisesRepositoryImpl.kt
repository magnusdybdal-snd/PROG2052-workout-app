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

/**
 * Implementation of [ExercisesRepository] that manages the exercise library.
 *
 * Uses a local-first strategy with Room database as the source of truth. Exercises
 * are pre-loaded from assets on first app launch and cached in memory for fast access.
 * The cache is kept sorted alphabetically to optimize UI rendering performance.
 *
 * @property api API service for potential future synchronization with backend
 * @property dao Room DAO for local exercise data persistence
 * @property initializer Helper that loads exercises from assets on first launch
 * @property userPreferences DataStore for tracking initialization state
 */
class ExercisesRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: ExerciseDao,
    private val initializer: ExerciseInitializer,
    private val userPreferences: UserPreferences
) : ExercisesRepository {

    /**
     * In-memory cache of exercises, pre-loaded and sorted alphabetically.
     * Eliminates lag when rendering the exercise list (~1500 items).
     */
    private val cache = MutableStateFlow<List<Exercise>>(emptyList())

    init {
        // Eagerly load exercises on repository creation to eliminate UI lag
        CoroutineScope(Dispatchers.IO).launch {
            initializer.initializeIfNeeded()

            dao.getAllExercises()
                .map { list -> list.map { it.toDomain() } }
                .collect { mapped ->
                    cache.value = mapped.sortedBy { it.name.lowercase() }
                }
        }
    }

    /**
     * Provides reactive access to the exercise library.
     *
     * Returns the pre-loaded, alphabetically sorted cache for fast and offline access.
     * The StateFlow automatically updates observers when the cache changes.
     *
     * @return StateFlow emitting the complete list of exercises
     */
    override fun observeExercises(): StateFlow<List<Exercise>> = cache

    /**
     * Fetches all exercises from the local Room database.
     *
     * @return List of all exercises from local database
     */
    override suspend fun getExercises(): List<Exercise> {
        val localExercises = dao.getAllExercises().first().map { it.toDomain() }
        return localExercises
    }

    /**
     * Converts an [ExerciseEntity] from the database layer to a domain [Exercise] model.
     *
     * Applies title case formatting to all text fields for consistent UI display.
     *
     * @return Domain model exercise with formatted text
     */
    private fun ExerciseEntity.toDomain() = Exercise(
        exerciseId = exerciseId,
        name = name.toTitleCase(),
        targetMuscles = targetMuscles.map { it.toTitleCase() },
        bodyParts = bodyParts.map { it.toTitleCase() },
        equipments = equipments.map { it.toTitleCase() },
        secondaryMuscles = secondaryMuscles.map { it.toTitleCase() },
        gifUrl = gifUrl,
        instructions = instructions
    )

    /**
     * Converts a string to title case format (first letter of each word capitalized).
     *
     * Example: "chest press" → "Chest Press"
     *
     * @return Title-cased string
     */
    private fun String.toTitleCase(): String {
        return split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercase() }
            }
    }
}

