package com.example.workoutapp.features.edit_template

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.usecases.EditWorkoutTemplateUseCase
import com.example.workoutapp.domain.usecases.GetExercisesUseCase
import com.example.workoutapp.domain.usecases.GetWorkoutTemplatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the template editing screen.
 *
 * @property isLoading Whether data is currently being loaded
 * @property templates List of workout templates available for editing
 * @property exercises Complete exercise library for adding exercises to templates
 * @property error Error message if an operation failed
 */
data class EditTemplateUiState(
    val isLoading: Boolean = false,
    val templates: List<WorkoutTemplate> = emptyList(),
    val exercises: List<Exercise> = emptyList(),
    val error: String? = null
)

/**
 * ViewModel for editing existing workout templates.
 *
 * Manages state for the template editor, loading both the user's templates
 * and the complete exercise library. Templates are converted to mutable state
 * lists to support real-time UI updates during editing (adding/removing exercises
 * and sets).
 *
 * @property getWorkoutTemplatesUseCase Use case for retrieving user templates
 * @property editWorkoutTemplatesUseCase Use case for persisting template changes
 * @property getExercisesUseCase Use case for retrieving the exercise library
 */
@HiltViewModel
class EditTemplateViewModel @Inject constructor(
    private val getWorkoutTemplatesUseCase: GetWorkoutTemplatesUseCase,
    private val editWorkoutTemplatesUseCase: EditWorkoutTemplateUseCase,
    private val getExercisesUseCase: GetExercisesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditTemplateUiState())

    /**
     * Observable UI state for the template editor screen.
     */
    val uiState: StateFlow<EditTemplateUiState> = _uiState

    init {
        loadWorkoutTemplates()
        loadExercises()
    }

    /**
     * Loads user workout templates for editing.
     *
     * Templates are converted to mutable state lists to enable reactive UI updates
     * when the user modifies exercises or sets during editing. Templates are sorted
     * by creation date (newest first).
     *
     * **Important**: The conversion to `toMutableStateList()` is necessary for Compose
     * to observe individual set modifications within the template editor UI.
     */
    fun loadWorkoutTemplates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                getWorkoutTemplatesUseCase().collect { data ->
                    val statefulTemplates = data.map { template ->
                        template.copy(
                            exercises = template.exercises.map { exercise ->
                                exercise.copy(
                                    sets = exercise.sets.toMutableStateList()
                                )
                            }.toMutableList()
                        )
                    }

                    _uiState.value = EditTemplateUiState(
                        templates = statefulTemplates.sortedByDescending { it.createdAt },
                        exercises = _uiState.value.exercises,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Unknown error") }
            }
        }
    }

    /**
     * Loads the exercise library for adding exercises to templates.
     *
     * Exercises are sorted alphabetically for easy browsing in the exercise picker.
     */
    fun loadExercises() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                getExercisesUseCase().collect { exercises ->
                    Log.d("EditTemplateViewModel", "Fetched ${exercises.size} exercises")
                    _uiState.update { current ->
                        current.copy(
                            exercises = exercises.sortedBy { it.name.lowercase() },
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Unknown error") }
            }
        }
    }

    /**
     * Saves changes to a workout template.
     *
     * Persists the modified template to Room database and syncs with the backend API.
     * Updates UI state with error message if save fails.
     *
     * @param workoutTemplate The modified template to save
     */
    fun editTemplate(workoutTemplate: WorkoutTemplate) {
        viewModelScope.launch {
            try {
                editWorkoutTemplatesUseCase(workoutTemplate)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to edit template") }
            }
        }
    }
}