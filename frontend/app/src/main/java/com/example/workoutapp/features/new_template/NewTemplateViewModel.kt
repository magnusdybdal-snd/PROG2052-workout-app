package com.example.workoutapp.features.new_template

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.data.api.dto.HistoryWorkoutDto
import com.example.workoutapp.data.api.dto.WorkoutTemplateDto
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.usecases.GetWorkoutTemplatesUseCase
import com.example.workoutapp.domain.usecases.PostHistoryWorkoutUseCase
import com.example.workoutapp.domain.usecases.PostWorkoutTemplateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI state holder: represents what's shown on the "New Template" screen.
data class NewTemplateUiState(
    val isLoading: Boolean = false,
    val templates: List<WorkoutTemplate> = emptyList(),
    val error: String? = null
)

// @HiltViewModel: tells Hilt this ViewModel can have dependencies injected.
// Hilt will generate all the factory code needed to create it.
@HiltViewModel
class NewTempViewModel @Inject constructor(  // @Inject = Hilt can construct this
    private val postWorkoutTemplateUseCase: PostWorkoutTemplateUseCase
) : ViewModel() {

    // The viewmodel can change this instance
    private val _uiState = MutableStateFlow(NewTemplateUiState())
    // This immutable instance is for the UI, read only
    val uiState: StateFlow<NewTemplateUiState> = _uiState

    fun postWorkout(workoutTemplateDto: WorkoutTemplateDto) {
        viewModelScope.launch {
            try {
                postWorkoutTemplateUseCase(workoutTemplateDto)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to save workout") }
            }
        }
    }
}