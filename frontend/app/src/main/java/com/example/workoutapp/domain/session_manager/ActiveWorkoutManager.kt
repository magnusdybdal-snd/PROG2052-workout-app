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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data class representing the state of an active workout session.
 *
 * Encapsulates all information about a workout in progress, including the original
 * template, set completion tracking, user modifications, and rest timer state.
 *
 * @property template The original workout template used to start this session
 * @property completedSets Nested list tracking completion status for each set in each exercise
 * @property modifiedExercises Mutable copy of the template allowing users to adjust sets/reps/weight during the workout
 * @property timerMinutes Initial timer duration in minutes for rest periods
 * @property timerSecondsRemaining Remaining seconds on the countdown timer
 * @property isTimerRunning Whether the rest timer is currently active
 * @property startTime Timestamp when the workout was started
 * @property notes Optional user notes about this workout session
 */
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

/**
 * Singleton manager for active workout sessions.
 *
 * Manages the lifecycle and state of a single active workout session, including:
 * - Starting and completing/canceling workouts
 * - Tracking set completion status
 * - Managing the rest period countdown timer
 * - Allowing mid-workout modifications to exercises (reps/weight adjustments)
 *
 * This manager uses a [CoroutineScope] to handle the countdown timer, ensuring it
 * continues running even during configuration changes. The state is exposed as
 * a [StateFlow] for reactive UI updates.
 *
 * **Note**: Only one workout can be active at a time. Attempting to start a second
 * workout will throw an exception.
 */
@Singleton
class ActiveWorkoutManager @Inject constructor() {

    private val _activeSession = MutableStateFlow<ActiveWorkoutSession?>(null)

    /**
     * Observable state of the current active workout session.
     * Emits null when no workout is in progress.
     */
    val activeSession: StateFlow<ActiveWorkoutSession?> = _activeSession.asStateFlow()

    /**
     * Coroutine scope for managing timer coroutines.
     * Uses SupervisorJob to ensure one coroutine failure doesn't affect others,
     * and runs on Main dispatcher for safe UI state updates.
     */
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    /**
     * Job handle for the countdown timer coroutine.
     * Allows cancellation and status checking of the timer.
     */
    private var timerJob: Job? = null

    /**
     * Job handle for the observer coroutine that monitors timer state.
     * Cancelled when workout completes to prevent memory leaks.
     */
    private var observerJob: Job? = null

    /**
     * Checks if a workout is currently in progress.
     *
     * @return true if there is an active workout session, false otherwise
     */
    fun hasActiveWorkout(): Boolean {
        return _activeSession.value != null
    }

    /**
     * Starts a new workout session from a template.
     *
     * Initializes the active session with:
     * - A copy of the template for modifications
     * - Completion tracking initialized to false for all sets
     * - Default timer settings
     * - Current timestamp as start time
     *
     * Also starts observing timer state changes to manage the countdown.
     * Cancels any previous observer to prevent multiple concurrent observers.
     *
     * @param template The workout template to base this session on
     * @throws IllegalStateException if a workout is already in progress
     */
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

        // Cancel previous observer before starting new one
        observerJob?.cancel()
        observerJob = observeTimer()
    }

    /**
     * Observes the active session state to automatically start/stop the countdown timer.
     *
     * Launches a coroutine that collects activeSession changes and starts the timer
     * when `isTimerRunning` becomes true, or stops it when false.
     *
     * @return Job handle for the observer coroutine, allowing cancellation
     */
    private fun observeTimer(): Job {
        return scope.launch {
            activeSession.collect { session ->
                if (session?.isTimerRunning == true && timerJob?.isActive != true) {
                    startTimer()
                } else if (session?.isTimerRunning == false) {
                    stopTimer()
                }
            }
        }
    }

    /**
     * Starts the countdown timer coroutine.
     *
     * Creates a new coroutine that decrements [ActiveWorkoutSession.timerSecondsRemaining]
     * every second until it reaches zero or the timer is stopped. Automatically
     * stops the timer when countdown completes.
     *
     * Cancels any existing timer before starting a new one to prevent duplicates.
     */
    private fun startTimer() {
        timerJob?.cancel()
        timerJob = scope.launch {
            while (activeSession.value?.isTimerRunning == true) {
                val session = activeSession.value ?: break
                if (session.timerSecondsRemaining <= 0) {
                    updateSession { it.copy(isTimerRunning = false) }
                    break
                }
                delay(1000)
                updateSession { it.copy(timerSecondsRemaining = it.timerSecondsRemaining - 1) }
            }
        }
    }

    /**
     * Stops the countdown timer coroutine and clears the job reference.
     */
    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    /**
     * Updates the active workout session state in a thread-safe manner.
     *
     * Applies the provided updater function to the current session state to produce
     * a new session state. The updater receives the current session and should return
     * an updated copy using the `copy()` method.
     *
     * Uses [MutableStateFlow.update] for atomic compare-and-swap, preventing
     * race conditions in multi-threaded scenarios.
     *
     * Example usage:
     * activeWorkoutManager.updateSession { it.copy(notes = "Felt strong today") }
     *
     * @param updater Function that transforms the current session into an updated session
     */
    fun updateSession(updater: (ActiveWorkoutSession) -> ActiveWorkoutSession) {
        _activeSession.update { current ->
            current?.let(updater)
        }
    }

    /**
     * Completes the current workout session.
     *
     * Clears the active session state and cancels the observer coroutine to prevent
     * memory leaks. Should be followed by saving the workout to history through
     * [com.example.workoutapp.domain.usecases.PostHistoryWorkoutUseCase].
     */
    fun completeWorkout() {
        observerJob?.cancel()
        observerJob = null
        _activeSession.value = null
    }

    /**
     * Cancels the current workout session without saving.
     *
     * Discards all workout progress, clears the active session state, and cancels
     * the observer coroutine to prevent memory leaks.
     */
    fun cancelWorkout() {
        observerJob?.cancel()
        observerJob = null
        _activeSession.value = null
    }
}