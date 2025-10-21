package com.example.workoutapp.features.active_workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.data.api.dto.HistoryWorkoutDto
import com.example.workoutapp.domain.models.Session
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.usecases.GetWorkoutTemplatesUseCase
import com.example.workoutapp.domain.usecases.PostHistoryWorkoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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
    private val getWorkoutTemplatesUseCase: GetWorkoutTemplatesUseCase,
    private val postHistoryWorkoutUseCase: PostHistoryWorkoutUseCase
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
                // On success update the state with data in exercises
                getWorkoutTemplatesUseCase().collect { data ->
                    _uiState.value = ActiveWorkoutUiState(
                        templates = data.sortedBy { it.createdAt },
                        isLoading = false
                    )
                }
                // On failure update the state with an error message
            } catch (e: Exception) {
                _uiState.value = ActiveWorkoutUiState(error = e.message ?: "Unknown error")
            }
        }
    }

    fun postWorkout(session: Session) {
        viewModelScope.launch {
            try {
                postHistoryWorkoutUseCase(session)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to save workout") }
            }
        }
    }
    fun getCurrentTimeString(): String {
        val currentTime = LocalTime.now() // current time
        val formatter = DateTimeFormatter.ofPattern("HH:mm") // 24-hour format
        return currentTime.format(formatter)
    }
}