package com.example.workoutapp.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.session_manager.ActiveWorkoutManager
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
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

/**
 * UI state for the home/templates screen.
 *
 * @property isLoading Whether templates are currently being loaded
 * @property workoutTemplates User-created workout templates
 * @property exampleTemplates Pre-defined example templates
 * @property error Error message if loading failed
 */
data class WorkoutTemplatesUiState(
    val isLoading: Boolean = false,
    val workoutTemplates: List<WorkoutTemplate> = emptyList(),
    val exampleTemplates: List<WorkoutTemplate> = emptyList(),
    val error: String? = null
)

/**
 * ViewModel for the home screen displaying workout templates.
 *
 * Manages both user-created templates and example templates, observing them
 * reactively from Room and syncing with the backend API. Also provides access
 * to the [ActiveWorkoutManager] for starting workouts.
 *
 * @property getWorkoutTemplatesUseCase Use case for retrieving user templates
 * @property deleteWorkoutTemplatesUseCase Use case for deleting templates
 * @property repository Direct repository access for example templates
 * @property activeWorkoutManager Manager for active workout sessions
 */
@HiltViewModel
class WorkoutTemplatesViewModel @Inject constructor(
    private val getWorkoutTemplatesUseCase: GetWorkoutTemplatesUseCase,
    private val deleteWorkoutTemplatesUseCase: DeleteWorkoutTemplateUseCase,
    private val repository: WorkoutTemplateRepository,
    val activeWorkoutManager: ActiveWorkoutManager
): ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutTemplatesUiState())

    /**
     * Observable UI state for the templates screen.
     */
    val uiState: StateFlow<WorkoutTemplatesUiState> = _uiState.asStateFlow()

    init {
        observeWorkoutTemplates()
        observeExampleTemplates()
        syncFromApi()
    }

    /**
     * Observes user templates reactively from local Room database.
     *
     * Automatically updates UI when templates change in Room. Templates are
     * sorted by creation date (newest first).
     */
    private fun observeWorkoutTemplates() {
        viewModelScope.launch {
            getWorkoutTemplatesUseCase()
                .onEach { templates ->
                    Log.d("TemplateViewmodel", "Received ${templates.size} templates from flow")

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            workoutTemplates = templates.sortedByDescending { it.createdAt }
                        )
                    }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Unknown error"
                        )
                    }
                }
                .collect()

        }
    }

    /**
     * Triggers a manual sync with the backend API.
     *
     * Syncs both user templates and example templates from the backend.
     * Failures are logged but don't affect UI state.
     */
    private fun syncFromApi() {
        viewModelScope.launch {
            try {
                getWorkoutTemplatesUseCase.syncNow()
                repository.syncExampleTemplates()
            } catch (e: Exception) {
                Log.e("TemplateViewModel", "Sync failed: ${e.message}")
            }
        }
    }

    /**
     * Observes example templates from Room database and syncs with API.
     *
     * Example templates are pre-defined workouts that help users get started.
     */
    private fun observeExampleTemplates() {
        viewModelScope.launch {
            try {
                repository.getExampleTemplates()
                    .catch { e ->
                        Log.e("TemplateViewModel", "Error fetching example templates: ${e.message}")
                    }
                    .collect { examples ->
                        Log.d("TemplateViewModel", "Received ${examples.size} example templates from flow")
                        _uiState.update { it.copy(exampleTemplates = examples) }
                    }
            } catch (e: Exception) {
                Log.e("TemplateViewModel", "Failed to observe example templates: ${e.message}")
            }
        }
    }

    /**
     * Deletes a workout template.
     *
     * Removes the template from both local Room database and backend API.
     * Updates UI state with error message if deletion fails.
     *
     * @param workoutTemplate The template to delete
     */
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