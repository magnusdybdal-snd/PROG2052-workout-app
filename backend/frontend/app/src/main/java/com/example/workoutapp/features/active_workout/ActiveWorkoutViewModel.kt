package com.example.workoutapp.features.active_workout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.usecases.GetWorkoutTemplatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI state holder: represents what's shown on the "Active workout" screen.
data class ActiveWorkoutUiState(
    val isLoading: Boolean = false,
    val templates: List<WorkoutTemplate> = emptyList(),
    val error: String? = null
)

// @HiltViewModel: tells Hilt this ViewModel can have dependencies injected.
// Hilt will generate all the factory code needed to create it.
@HiltViewModel
class ActWorkViewModel @Inject constructor(  // @Inject = Hilt can construct this
    private val getWorkoutTemplatesUseCase: GetWorkoutTemplatesUseCase
) : ViewModel() {

    // The viewmodel can change this instance
    private val _uiState = MutableStateFlow(ActiveWorkoutUiState())
    // This immutable instance is for the UI, read only
    val uiState: StateFlow<ActiveWorkoutUiState> = _uiState

    // Runs once when the class is instantiated
    init {
        loadWorkoutTemplates()
    }

    fun loadWorkoutTemplates() {
        viewModelScope.launch {
            _uiState.value = ActiveWorkoutUiState(isLoading = true)
            try {
                val data = getWorkoutTemplatesUseCase()
                // On success update the state with data in exercises
                //Log.d("ExercisesViewModel", "Fetched ${data.size} exercises")
                _uiState.value = ActiveWorkoutUiState(templates = data.sortedBy { it.name.lowercase() })
                // On failure update the state with an error message
            } catch (e: Exception) {
                _uiState.value = ActiveWorkoutUiState(error = e.message ?: "Unknown error")
            }
        }
    }
}