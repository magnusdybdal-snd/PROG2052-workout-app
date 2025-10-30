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

// UI state holder: represents what's shown on the "Active workout" screen.
data class EditTemplateUiState(
    val isLoading: Boolean = false,
    val templates: List<WorkoutTemplate> = emptyList(),
    val exercises: List<Exercise> = emptyList(), // add this
    val error: String? = null
)

// @HiltViewModel: tells Hilt this ViewModel can have dependencies injected.
// Hilt will generate all the factory code needed to create it.
@HiltViewModel
class EditTemplateViewModel @Inject constructor(  // @Inject = Hilt can construct this
    private val getWorkoutTemplatesUseCase: GetWorkoutTemplatesUseCase,
    private val editWorkoutTemplatesUseCase: EditWorkoutTemplateUseCase,
    private val getExercisesUseCase: GetExercisesUseCase
) : ViewModel() {

    // The viewmodel can change this instance
    private val _uiState = MutableStateFlow(EditTemplateUiState())
    // This immutable instance is for the UI, read only
    val uiState: StateFlow<EditTemplateUiState> = _uiState

    // Runs once when the class is instantiated
    init {
        loadWorkoutTemplates()
        loadExercises()
    }

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

    fun loadExercises() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val data = getExercisesUseCase()
                Log.d("EditTemplateViewModel", "Fetched ${data.size} exercises")
                _uiState.update { current ->
                    current.copy(
                        exercises = data.sortedBy { it.name.lowercase() },
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Unknown error") }
            }
        }
    }

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