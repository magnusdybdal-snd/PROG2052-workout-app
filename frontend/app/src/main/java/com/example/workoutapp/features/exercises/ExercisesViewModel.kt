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

/**
 * UI state for the exercises screen.
 *
 * @property isLoading Whether exercises are currently being loaded
 * @property exercises List of exercises to display
 * @property error Error message if loading failed
 */
data class ExercisesUiState(
    val isLoading: Boolean = false,
    val exercises: List<Exercise> = emptyList(),
    val error: String? = null
)

/**
 * ViewModel for the exercises library screen.
 *
 * Manages the exercise list state by observing the exercise repository's
 * pre-loaded cache. The exercises are fetched once and kept in memory for
 * fast access throughout the app session.
 *
 * @property getExercisesUseCase Use case for retrieving exercises
 */
@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val getExercisesUseCase: GetExercisesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExercisesUiState())

    /**
     * Observable UI state for the exercises screen.
     */
    val uiState: StateFlow<ExercisesUiState> = _uiState.asStateFlow()

    init {
        loadExercises()
    }

    /**
     * Loads exercises from the use case and observes for updates.
     *
     * Sets the initial snapshot immediately, then collects updates as the
     * repository finishes loading from Room database.
     */
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
