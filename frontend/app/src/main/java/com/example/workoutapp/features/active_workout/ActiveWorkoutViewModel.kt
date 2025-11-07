package com.example.workoutapp.features.active_workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.data.api.dto.HistoryWorkoutDto
import com.example.workoutapp.domain.models.Session
import com.example.workoutapp.domain.models.SessionExercise
import com.example.workoutapp.domain.models.Set
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.session_manager.ActiveWorkoutManager
import com.example.workoutapp.domain.usecases.GetWorkoutTemplatesUseCase
import com.example.workoutapp.domain.usecases.PostHistoryWorkoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import kotlin.reflect.typeOf

// UI state holder: represents what's shown on the "Active workout" screen.
data class ActiveWorkoutUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

// @HiltViewModel: tells Hilt this ViewModel can have dependencies injected.
// Hilt will generate all the factory code needed to create it.
@HiltViewModel
class ActWorkViewModel @Inject constructor(  // @Inject = Hilt can construct this
    private val postHistoryWorkoutUseCase: PostHistoryWorkoutUseCase,
    val activeWorkoutManager: ActiveWorkoutManager
) : ViewModel() {

    // Expose active session from ActiveWorkoutManager
    val activeSession = activeWorkoutManager.activeSession

    // The viewmodel can change this instance
    private val _uiState = MutableStateFlow(ActiveWorkoutUiState())
    // This immutable instance is for the UI, read only
    val uiState: StateFlow<ActiveWorkoutUiState> = _uiState

    private var timerJob: Job? = null // Track timer coroutine

    init {
        observeTimerState()
    }

    private fun observeTimerState() {
        viewModelScope.launch {
            activeSession.collect { session ->
                android.util.Log.d("TimerDebug", "Session changed: isRunning=${session?.isTimerRunning}, seconds=${session?.timerSecondsRemaining}")
                if (session?.isTimerRunning == true) {
                    android.util.Log.d("TimerDebug", "Starting timer countdown")
                    startTimerCountdown()
                } else {
                    android.util.Log.d("TimerDebug", "Stopping timer countdown")
                    stopTimerCountdown()
                }
            }
        }
    }

    private fun startTimerCountdown() {
        android.util.Log.d("TimerDebug", "startTimerCountdown called, existing job: ${timerJob != null}")
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            android.util.Log.d("TimerDebug", "Timer coroutine started")

            while (true) {
                val session = activeSession.value
                if (session == null || !session.isTimerRunning || session.timerSecondsRemaining <= 0) {
                    android.util.Log.d("TimerDebug", "Timer stopping: session=$session, isRunning=${session?.isTimerRunning}, seconds=${session?.timerSecondsRemaining}")

                    if (session?.timerSecondsRemaining == 0) {
                        activeWorkoutManager.updateSession {
                            it.copy(isTimerRunning = false)
                        }
                    }
                    break
                }
                delay(1000)
                android.util.Log.d("TimerDebug", "Tick: ${session.timerSecondsRemaining}")

                tickTimer()
            }
            android.util.Log.d("TimerDebug", "Timer coroutine finished")
        }
    }

    private fun stopTimerCountdown() {
        timerJob?.cancel()
        timerJob = null
    }

    fun updateCompletedSets(exerciseIndex: Int, setindex: Int, isCompleted: Boolean) {
        activeWorkoutManager.updateSession { session ->
            val updatedSets = session.completedSets.toMutableList().apply {
                this[exerciseIndex] = this[exerciseIndex].toMutableList().apply {
                    this[setindex] = isCompleted
                }
            }
            session.copy(completedSets = updatedSets)
        }
    }

    fun updateTimerMinutes(minutes: Int) {
        activeWorkoutManager.updateSession { it.copy(timerMinutes = minutes) }
    }

    fun startTimer() {
        activeWorkoutManager.updateSession {
            it.copy(
                isTimerRunning = true,
                timerSecondsRemaining = it.timerMinutes * 60
            )
        }
    }

    fun tickTimer() {
        activeWorkoutManager.updateSession { session ->
            if (session.timerSecondsRemaining > 0) {
                session.copy(timerSecondsRemaining = session.timerSecondsRemaining - 1)
            } else {
                session.copy(isTimerRunning = false)
            }
        }
    }

    fun updateNotes(notes: String) {
        activeWorkoutManager.updateSession { it.copy(notes = notes) }
    }

    fun updateExercise(template: WorkoutTemplate) {
        activeWorkoutManager.updateSession { it.copy(modifiedExercises = template) }
    }

    fun completeWorkout() {
        val session = activeWorkoutManager.activeSession.value ?: return

        val duration = Duration.between(session.startTime, LocalDateTime.now())

        val finishedWorkout = Session(
            sessionId = UUID.randomUUID().toString(),
            name = session.template.name,
            exercises = session.modifiedExercises.exercises.mapIndexedNotNull { exIndex, exSet ->
                val completedSetsForExercise = exSet.sets.filterIndexed { setIndex, _ ->
                    exIndex < session.completedSets.size &&
                    setIndex < session.completedSets[exIndex].size &&
                    session.completedSets[exIndex][setIndex]
                }

                if (completedSetsForExercise.isNotEmpty()) {
                    SessionExercise(
                        exerciseId = exSet.exerciseId,
                        name = exSet.name,
                        sets = completedSetsForExercise.map { set ->
                            Set(
                                rep = set.rep,
                                kg = set.kg,
                                typeSet = set.typeSet
                            )
                        }
                    )
                } else null
            },
            duration = duration,
            date = LocalDate.now(),
            note = session.notes
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                postHistoryWorkoutUseCase(finishedWorkout)
                stopTimerCountdown()
                activeWorkoutManager.completeWorkout()
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Failed to save workout") }
            }
        }
    }

    fun cancelWorkout() {
        stopTimerCountdown()
        activeWorkoutManager.cancelWorkout()
    }

    fun getCurrentTimeString(): String {
        val currentTime = LocalTime.now() // current time
        val formatter = DateTimeFormatter.ofPattern("HH:mm") // 24-hour format
        return currentTime.format(formatter)
    }

    // Clean up when viewmodel is destroyed
    override fun onCleared() {
        super.onCleared()
        stopTimerCountdown()
    }
}