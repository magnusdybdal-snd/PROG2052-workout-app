package com.example.workoutapp.domain.session_manager

import com.example.workoutapp.domain.models.WorkoutTemplate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

data class ActiveWorkoutSession(
    val template: WorkoutTemplate,
    val completedSets: List<List<Boolean>>,
    val modifiedExercises: WorkoutTemplate,
    val timerMinutes: Int,
    val timerSecondsRemaining: Int,
    val isTimerRunning: Boolean,
    val startTime: LocalDateTime,
    val notes: String = ""
)

@Singleton
class ActiveWorkoutManager @Inject constructor() {

    private val _activeSession = MutableStateFlow<ActiveWorkoutSession?>(null)
    val activeSession: StateFlow<ActiveWorkoutSession?> = _activeSession.asStateFlow()

    /*
     * Checks if the user has an active workout
     */
    fun hasActiveWorkout(): Boolean {
        return _activeSession.value != null
    }

    fun startWorkout(template: WorkoutTemplate) {
        if (hasActiveWorkout()) {
            throw IllegalStateException("Cannot start workout - one is already in progress")
        }

        _activeSession.value = ActiveWorkoutSession(
            template = template,
            completedSets = template.exercises.map { exercise ->
                List(exercise.sets.size) { false }
            },
            modifiedExercises = template.copy(
                exercises = template.exercises.map { it.copy() }.toMutableList()
            ),
            timerMinutes = 3,
            timerSecondsRemaining = 0,
            isTimerRunning = false,
            startTime = LocalDateTime.now(),
            notes = ""
        )
    }

    fun updateSession(updater: (ActiveWorkoutSession) -> ActiveWorkoutSession) {
        _activeSession.value?.let { current ->
            _activeSession.value = updater(current)
        }
    }

    fun completeWorkout() {
        _activeSession.value = null
    }

    fun cancelWorkout() {
        _activeSession.value = null
    }
}