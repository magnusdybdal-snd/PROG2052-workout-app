package com.example.workoutapp.domain.session_manager

import com.example.workoutapp.domain.models.WorkoutTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    // Create a coroutine that survives as long as the scope (workoutmanager) exists
    // Make it a supervisor job, so that if one coroutine crashed the manager survives
    // All coroutines run on the main thread, wo they can safely update UI (flow) state
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    // Job is a handle to run coroutines. has methods like .isActive, .cancel(), .join()
    private var timerJob: Job? = null

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

        observeTimer()
    }

    private fun observeTimer() {
        scope.launch {  // Start a coroutine in our scope
            // Collect recieves each emission from a stateFlow
            activeSession.collect { session ->  // Subscribe to active session changes
                // This code runs every time active session changes
                if (session?.isTimerRunning == true && timerJob?.isActive != true) {
                    // Timer should be running, but is not -> Start it
                    startTimer()
                } else if (session?.isTimerRunning == false) {
                    // Timer should be stopped -> stop it
                    stopTimer()
                }
            }
        }
    }

    /**
     *
     */
    private fun startTimer() {
        timerJob?.cancel()  // Stop any existing timer
        timerJob = scope.launch {   // Start a new countdown coroutine and save it as a job so we can reference it later
            while (activeSession.value?.isTimerRunning == true) {   // Keep observing as long as the timer is running
                val session = activeSession.value ?: break  // Get current session, if null -> break
                if (session.timerSecondsRemaining <= 0) {
                    // If timer reaches 0, stop the timer and exit
                    updateSession { it.copy(isTimerRunning = false) }
                    break
                }
                delay(1000) // Wait one second NB: don't use Thread.sleep() - will freeze UI
                // Decrease timer by one second
                updateSession { it.copy(timerSecondsRemaining = it.timerSecondsRemaining - 1) }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    // updater is a function that takes a session and returns a modified session
    // Has to be called with a lambda like: activeWorkoutManager.updateSession( { it.copy(notes = "my notes" } )
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