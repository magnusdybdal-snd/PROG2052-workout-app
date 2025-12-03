package com.example.workoutapp.features.history_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the workout history detail screen.
 *
 * Displays detailed information about a single completed workout session,
 * including exercises performed, sets completed, and workout notes. The workout
 * ID is retrieved from navigation arguments via SavedStateHandle.
 *
 * @property savedStateHandle Provides access to navigation arguments (workoutId)
 * @property repo Repository for retrieving workout history data
 */
@HiltViewModel
class HistoryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: HistoryWorkoutRepository
) : ViewModel() {

    private val id: String = checkNotNull(savedStateHandle["workoutId"])

    private val _ui = MutableStateFlow(DetailUiState(isLoading = true))

    /**
     * Observable UI state for the history detail screen.
     */
    val uiState: StateFlow<DetailUiState> = _ui

    init {
        viewModelScope.launch {
            runCatching { repo.getHistoryWorkoutBySessionId(id) }
                .onSuccess { session -> _ui.value = DetailUiState(session = session) }
                .onFailure { e -> _ui.value = DetailUiState(error = e.message) }
        }
    }
}

/**
 * UI state for the history detail screen.
 *
 * @property isLoading Whether the workout data is currently being loaded
 * @property error Error message if loading failed
 * @property session The completed workout session to display, null if loading or error
 */
data class DetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val session: HistoryWorkout? = null
)