package com.example.workoutapp.features.exercises

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.usecases.GetExercisesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI state holder: represents what's shown on the "Exercises" screen.
data class ExercisesUiState(
    val isLoading: Boolean = false,
    val exercises: List<Exercise> = emptyList(),
    val error: String? = null
)

// @HiltViewModel: tells Hilt this ViewModel can have dependencies injected.
// Hilt will generate all the factory code needed to create it.
@HiltViewModel
class ExercisesViewModel @Inject constructor(  // @Inject = Hilt can construct this
    private val getExercisesUseCase: GetExercisesUseCase
) : ViewModel() {

    // The viewmodel can change this instance
    private val _uiState = MutableStateFlow(ExercisesUiState())
    // This immutable instance is for the UI, read only
    val uiState: StateFlow<ExercisesUiState> = _uiState.asStateFlow()

    // Runs once when the class is instantiated
    init {
        loadExercises()
    }

    fun loadExercises() {
        val flow = getExercisesUseCase()

        // Set initial snapshot
        _uiState.value = ExercisesUiState(
            exercises = flow.value,
            isLoading = false
        )
        Log.d("ExerciseViewModel", "Initial ViewModel read: ${flow.value.size}")

        // Collect updates when DB finishes loading
        viewModelScope.launch {
            flow.collect { list ->
                _uiState.value = ExercisesUiState(
                    exercises = list,
                    isLoading = false
                )
            }
        }
    }
}
