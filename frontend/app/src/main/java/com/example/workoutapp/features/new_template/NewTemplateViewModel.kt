package com.example.workoutapp.features.new_template

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.data.api.dto.HistoryWorkoutDto
import com.example.workoutapp.data.api.dto.WorkoutTemplateDto
import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.models.NewTemplate
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.usecases.GetExercisesUseCase
import com.example.workoutapp.domain.usecases.GetWorkoutTemplatesUseCase
import com.example.workoutapp.domain.usecases.PostHistoryWorkoutUseCase
import com.example.workoutapp.domain.usecases.PostWorkoutTemplateUseCase
import com.example.workoutapp.features.exercises.ExercisesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI state holder: represents what's shown on the "New Template" screen.
data class NewTemplateUiState(
    val isLoading: Boolean = false,
    val templates: List<Exercise> = emptyList(),
    val error: String? = null
)

// @HiltViewModel: tells Hilt this ViewModel can have dependencies injected.
// Hilt will generate all the factory code needed to create it.
@HiltViewModel
class NewTempViewModel @Inject constructor(  // @Inject = Hilt can construct this
    private val getExercisesUseCase: GetExercisesUseCase,
    private val postWorkoutTemplateUseCase: PostWorkoutTemplateUseCase
) : ViewModel() {

    // The viewmodel can change this instance
    private val _uiState = MutableStateFlow(ExercisesUiState())
    // This immutable instance is for the UI, read only
    val uiState: StateFlow<ExercisesUiState> = _uiState

    init {
        loadExercises()
    }

    fun loadExercises() {
        viewModelScope.launch {
            _uiState.value = ExercisesUiState(isLoading = true)
            try {
                val data = getExercisesUseCase()
                // On success update the state with data in exercises
                Log.d("ExercisesViewModel", "Fetched ${data.size} exercises")
                _uiState.value = ExercisesUiState(exercises = data.sortedBy { it.name.lowercase() })
                // On failure update the state with an error message
            } catch (e: Exception) {
                _uiState.value = ExercisesUiState(error = e.message ?: "Unknown error")
            }
        }
    }

    fun postWorkout(newTemplate: NewTemplate) {
        viewModelScope.launch {
            try {
                postWorkoutTemplateUseCase(newTemplate)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to save workout") }
            }
        }
    }
}