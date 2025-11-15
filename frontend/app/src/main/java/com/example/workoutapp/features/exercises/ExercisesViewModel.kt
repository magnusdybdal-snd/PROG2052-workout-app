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
        viewModelScope.launch {
            _uiState.value = ExercisesUiState(isLoading = true)

            try {
                getExercisesUseCase().collect { exercises ->
                    // On success update the state with data in exercises
                    Log.d("ExercisesViewModel", "Fetched ${exercises.size} exercises")
                    _uiState.value = ExercisesUiState(
                        exercises = exercises.sortedBy { it.name.lowercase() }
                    )
                }
                // On failure update the state with an error message
            } catch (e: Exception) {
                _uiState.value = ExercisesUiState(
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}
