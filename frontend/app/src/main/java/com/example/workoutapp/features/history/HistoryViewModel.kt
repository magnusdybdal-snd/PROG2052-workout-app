package com.example.workoutapp.features.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.usecases.DeleteHistoryWorkoutUseCase
import com.example.workoutapp.domain.usecases.GetHistoryWorkoutUseCase
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
import java.time.format.TextStyle
import java.util.Locale

/**
 * UI state for the workout history screen.
 *
 * @property isLoading Whether history data is currently being loaded
 * @property historyWorkouts Complete list of completed workouts
 * @property groupedHistory Workouts grouped by month and year for sectioned display
 * @property error Error message if loading failed
 */
data class HistoryUiState(
    val isLoading: Boolean = false,
    val historyWorkouts: List<HistoryWorkout> = emptyList(),
    val groupedHistory: Map<String, List<HistoryWorkout>> = emptyMap(),
    val error: String? = null
)

/**
 * ViewModel for the workout history screen.
 *
 * Manages completed workouts, grouping them by month/year for chronological display.
 * Observes workouts reactively from Room and syncs with the backend API.
 *
 * @property getHistoryWorkoutUseCase Use case for retrieving workout history
 * @property deleteHistoryWorkoutUseCase Use case for deleting workouts from history
 */
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryWorkoutUseCase: GetHistoryWorkoutUseCase,
    private val deleteHistoryWorkoutUseCase: DeleteHistoryWorkoutUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState(isLoading = true))

    /**
     * Observable UI state for the history screen.
     */
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        observeHistoryWorkouts()
        syncFromApi()
    }

    /**
     * Observes workout history reactively from local Room database.
     *
     * Workouts are sorted by date (newest first) and grouped by month and year
     * for chronological sectioned display in the UI.
     */
    fun observeHistoryWorkouts() {
        viewModelScope.launch {
            getHistoryWorkoutUseCase()
                .onEach { workouts ->
                    Log.d("HistoryViewmodel", "Received ${workouts.size} workouts from flow")

                    val grouped = workouts
                        .sortedByDescending { it.date }
                        .groupBy { workout ->
                            val month = workout.date.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                            val year = workout.date.year
                            "$month $year"
                        }
                        .toSortedMap(compareByDescending { key ->
                            val (monthName, yearStr) = key.split(" ")
                            val monthValue = java.time.Month.valueOf(monthName.uppercase()).value
                            val year = yearStr.toInt()
                            year * 12 * monthValue
                        })

                    _uiState.value = HistoryUiState(
                        isLoading = false,
                        historyWorkouts = workouts,
                        groupedHistory = grouped
                    )
                }
                .catch { e ->
                    _uiState.value = HistoryUiState(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
                .collect()
        }
    }

    /**
     * Triggers a manual sync with the backend API.
     *
     * Fetches latest workout history from the backend and merges with local data.
     * Failures are logged but don't affect UI state.
     */
    private fun syncFromApi() {
        viewModelScope.launch {
            try {
                getHistoryWorkoutUseCase.syncNow()
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Sync failed: ${e.message}")
            }
        }
    }

    /**
     * Deletes a workout from history.
     *
     * Removes the workout from both local Room database and backend API.
     * Updates UI state with error message if deletion fails.
     *
     * @param historyWorkout The workout to delete from history
     */
    fun deleteSession(historyWorkout: HistoryWorkout) {
        viewModelScope.launch {
            try {
                deleteHistoryWorkoutUseCase(historyWorkout)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to delete historyWorkout") }
            }
        }
    }
}