package com.example.workoutapp.features.history

import android.util.Log
import androidx.compose.runtime.key
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.usecases.GetHistoryWorkoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val getHistoryWorkoutUseCase: GetHistoryWorkoutUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = HistoryUiState(isLoading = true)
            try {
                val data = getHistoryWorkoutUseCase()

                // Groups all workouts by month/year for sorting in history page
                val grouped = data
                    .sortedByDescending { it.date }
                    .groupBy { workout ->
                    val month = workout.date.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                    val year = workout.date.year
                    "$month $year"
                }
                    // Sorts the map so that the newest month appears first
                    .toSortedMap(compareByDescending { key ->
                        val parts = key.split(" ")
                        val monthName = parts[0]
                        val year = parts[1].toInt()
                        val monthValue = java.time.Month.valueOf(monthName.uppercase()).value
                        year * 12 + monthValue
                    })
                Log.d("HistoryViewModel", "Fetched ${data.size} completed workouts")

                _uiState.value = HistoryUiState(
                    historyWorkouts = data.sortedByDescending { it.date },
                    groupedHistory = grouped)
            } catch (e: Exception) {
                _uiState.value = HistoryUiState(error = e.message ?: "Unknown error")
            }
        }
    }
}