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

@HiltViewModel
class HistoryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: HistoryWorkoutRepository
) : ViewModel() {

    private val id: String = checkNotNull(savedStateHandle["workoutId"])

    private val _ui = MutableStateFlow(DetailUiState(isLoading = true))
    val uiState: StateFlow<DetailUiState> = _ui

    init {
        viewModelScope.launch {
            runCatching { repo.getHistoryWorkoutBySessionId(id) }
                .onSuccess { session -> _ui.value = DetailUiState(session = session) }
                .onFailure { e -> _ui.value = DetailUiState(error = e.message) }
        }
    }
}

// Put this in a shared UI-state file if you prefer
data class DetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val session: HistoryWorkout? = null // replace with your actual model type
)