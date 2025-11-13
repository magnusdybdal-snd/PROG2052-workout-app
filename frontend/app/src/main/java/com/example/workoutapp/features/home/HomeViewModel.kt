package com.example.workoutapp.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.session_manager.ActiveWorkoutManager
import com.example.workoutapp.domain.usecases.DeleteWorkoutTemplateUseCase
import com.example.workoutapp.domain.usecases.GetWorkoutTemplatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkoutTemplatesUiState(
    val isLoading: Boolean = false,
    val workoutTemplates: List<WorkoutTemplate> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class WorkoutTemplatesViewModel @Inject constructor(
    private val getWorkoutTemplatesUseCase: GetWorkoutTemplatesUseCase,
    private val deleteWorkoutTemplatesUseCase: DeleteWorkoutTemplateUseCase,
    val activeWorkoutManager: ActiveWorkoutManager
): ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutTemplatesUiState())
    val uiState: StateFlow<WorkoutTemplatesUiState> = _uiState.asStateFlow()

    init {
        observeWorkoutTemplates()
        syncFromApi()
    }

    /**
     * Observe templates reactively from local Room DB
     * This automatically updates UI when Room changes.
     */
    private fun observeWorkoutTemplates() {
        viewModelScope.launch {
            getWorkoutTemplatesUseCase()
                .onEach { templates ->
                    Log.d("TemplateViewmodel", "Received ${templates.size} templates from flow")

                    _uiState.value = WorkoutTemplatesUiState(
                        isLoading = false,
                        workoutTemplates = templates.sortedByDescending { it.createdAt },
                    )
                }
                .catch { e ->
                    _uiState.value = WorkoutTemplatesUiState(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
                .collect()

        }
    }

    private fun syncFromApi() {
        viewModelScope.launch {
            try {
                getWorkoutTemplatesUseCase.syncNow()
            } catch (e: Exception) {
                Log.e("TemplateViewModel", "Sync failed: ${e.message}")
            }
        }
    }

    fun deleteTemplate(workoutTemplate: WorkoutTemplate) {
        viewModelScope.launch {
            try {
                deleteWorkoutTemplatesUseCase(workoutTemplate)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to delete template") }
            }
        }
    }
}