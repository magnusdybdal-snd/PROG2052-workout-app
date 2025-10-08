package com.example.workoutapp.features.history

import android.util.Log
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
                val grouped = data.groupBy { workout ->
                    val month = workout.date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    val year = workout.date.year
                    "$month $year"
                }
                Log.d("HistoryViewModel", "Fetched ${data.size} completed workouts")

                _uiState.value = HistoryUiState(
                    historyWorkouts = data.sortedBy { it.date },
                    groupedHistory = grouped)
            } catch (e: Exception) {
                _uiState.value = HistoryUiState(error = e.message ?: "Unknown error")
            }
        }
    }
}