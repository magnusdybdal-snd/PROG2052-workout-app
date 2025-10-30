package com.example.workoutapp.features.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.models.Session
import com.example.workoutapp.domain.models.WorkoutTemplate
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

data class HistoryUiState(
    val isLoading: Boolean = false,
    // List of all workouts, not used by UI in MVP but could be useful later
    val historyWorkouts: List<HistoryWorkout> = emptyList(),
    val groupedHistory: Map<String, List<HistoryWorkout>> = emptyMap(),
    val error: String? = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryWorkoutUseCase: GetHistoryWorkoutUseCase,
    private val deleteHistoryWorkoutUseCase: DeleteHistoryWorkoutUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState(isLoading = true))
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        observeHistoryWorkouts()
        syncFromApi()
    }

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

    private fun syncFromApi() {
        viewModelScope.launch {
            try {
                getHistoryWorkoutUseCase.syncNow()
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Sync failed: ${e.message}")
            }
        }
    }

    fun deleteSession(historyWorkout: HistoryWorkout) {
        viewModelScope.launch {
            try {
                deleteHistoryWorkoutUseCase(historyWorkout)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to delete template") }
            }
        }
    }
}