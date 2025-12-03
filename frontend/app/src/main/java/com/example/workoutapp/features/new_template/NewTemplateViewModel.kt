package com.example.workoutapp.features.new_template

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.models.NewTemplate
import com.example.workoutapp.domain.usecases.GetExercisesUseCase
import com.example.workoutapp.domain.usecases.PostWorkoutTemplateUseCase
import com.example.workoutapp.features.exercises.ExercisesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * UI state for the new template screen.
 *
 * @property isLoading Whether data is currently being loaded
 * @property templates List of available exercises
 * @property error Error message if an operation failed
 */
data class NewTemplateUiState(
    val isLoading: Boolean = false,
    val exercises: List<Exercise> = emptyList(),
    val error: String? = null
)

/**
 * ViewModel for creating new workout templates.
 *
 * Manages the exercise list for selection and handles template creation.
 * Users select exercises from the library and configure sets to build a new template.
 *
 * @property getExercisesUseCase Use case for retrieving available exercises
 * @property postWorkoutTemplateUseCase Use case for saving new templates
 */
@HiltViewModel
class NewTempViewModel @Inject constructor(
    private val getExercisesUseCase: GetExercisesUseCase,
    private val postWorkoutTemplateUseCase: PostWorkoutTemplateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewTemplateUiState())

    /**
     * Observable UI state for the new template screen.
     */
    val uiState: StateFlow<NewTemplateUiState> = _uiState

    init {
        loadExercises()
    }

    /**
     * Loads the exercise library for exercise selection.
     *
     * Exercises are sorted alphabetically for easy browsing.
     */
    fun loadExercises() {
        viewModelScope.launch {
            _uiState.value = NewTemplateUiState(isLoading = true)

            try {
                getExercisesUseCase().collect { exercises ->
                    // On success update the state with data in exercises
                    Log.d("ExercisesViewModel", "Fetched ${exercises.size} exercises")
                    _uiState.value = NewTemplateUiState(exercises = exercises.sortedBy { it.name.lowercase() })
                }
            // On failure update the state with an error message
            } catch (e: Exception) {
                _uiState.value = NewTemplateUiState(error = e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Saves a newly created workout template.
     *
     * Persists the template to Room database and syncs with the backend API.
     * Updates UI state with error message if save fails.
     *
     * @param newTemplate The template to save
     */
    fun postWorkout(newTemplate: NewTemplate) {
        viewModelScope.launch {
            try {
                postWorkoutTemplateUseCase(newTemplate)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to save workout") }
            }
        }
    }

    /**
     * Returns the current time as a formatted string (HH:mm).
     *
     * @return Current time in 24-hour format
     */
    fun getCurrentTimeString(): String {
        val currentTime = LocalTime.now() // current time
        val formatter = DateTimeFormatter.ofPattern("HH:mm") // 24-hour format
        return currentTime.format(formatter)
    }
}

