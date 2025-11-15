package com.example.workoutapp.features.active_workout

import android.R.attr.name
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.models.NewTemplateExercise
import com.example.workoutapp.domain.models.Session
import com.example.workoutapp.domain.models.SessionExercise
import com.example.workoutapp.domain.models.Set
import com.example.workoutapp.domain.models.TemplateExercise
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.session_manager.ActiveWorkoutManager
import com.example.workoutapp.domain.usecases.GetExercisesUseCase
import com.example.workoutapp.domain.usecases.PostHistoryWorkoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

/**
 * UI state holder for the Active Workout screen
 *
 * @property isLoading Indicates if a network/database operation is in progress
 * @property error Error message to display to the user, null if no error
 */
data class ActiveWorkoutUiState(
    val isLoading: Boolean = false,
    val exercises: List<Exercise> = emptyList(),
    val error: String? = null
)

/**
 * ViewModel for managing active workout state and operations.
 *
 * Coordinates between the UI and the ActiveWorkoutManager singleton,
 * handling user interactions like completing sets, starting timers,
 * and saving finished workouts to history.
 *
 * @property postHistoryWorkoutUseCase Use case for saving completed workouts
 * @property activeWorkoutManager Singleton that manages the active workout session state
 */
@HiltViewModel
class ActWorkViewModel @Inject constructor(
    private val postHistoryWorkoutUseCase: PostHistoryWorkoutUseCase,
    val activeWorkoutManager: ActiveWorkoutManager,
    private val getExercisesUseCase: GetExercisesUseCase
) : ViewModel() {

    // Expose active session from ActiveWorkoutManager
    val activeSession = activeWorkoutManager.activeSession

    // The viewmodel can change this instance
    private val _uiState = MutableStateFlow(ActiveWorkoutUiState())
    // This immutable instance is for the UI, read only
    val uiState: StateFlow<ActiveWorkoutUiState> = _uiState

    init {
        viewModelScope.launch {
            val result = getExercisesUseCase()
            _uiState.update { it.copy(exercises = result) }
        }
    }

    /**
     * Marks a set as completed (checkbox checked/unchecked)
     *
     * EXAMPLE STRUCTURE OF session.completedSets:
     *
     *  [
     *      [false, false, true], <- Exercise 0: 3 sets
     *      [false, false],       <- Exercise 1: 2 sets
     *      [true, false, false], <- Exercise 2: 3 sets
     *  ]
     *
     * @param exerciseIndex Which exercise (0, 1, 2...)
     * @param setindex Which set within that exercise (0, 1, 2...)
     * @param isCompleted true if set is completed (checked), false otherwise
     */
    fun updateCompletedSets(exerciseIndex: Int, setindex: Int, isCompleted: Boolean) {
        // Updates the current session
        activeWorkoutManager.updateSession { session ->
            // Takes the current outer list of exercises (with lists of sets) and makes it mutable
            val updatedSets = session.completedSets.toMutableList().apply {
                // Takes the list of sets from that exercise and makes it mutable
                this[exerciseIndex] = this[exerciseIndex].toMutableList().apply {
                    // Applies the boolean flag to the specific set that is completed
                    this[setindex] = isCompleted
                }
            }
            // Return the new session with updated completed sets
            session.copy(completedSets = updatedSets)
        }
    }

    /**
     * Adds a new set to the specified exercise in the active workout.
     *
     * Creates a new set with default values (0 reps, 0 kg) and adds tracking
     * for the new set in completedSets.
     *
     * @param exerciseIndex The index of the exercise to add a set to
     */
    fun addSetToExercise(exerciseIndex: Int) {
        activeWorkoutManager.updateSession { session ->
            // Guard: invalid index -> just return current session
            if (exerciseIndex !in session.modifiedExercises.exercises.indices) {
                return@updateSession session
            }

            // New set with default values
            val newSet = Set(
                rep = 0,
                kg = 0.0,
                typeSet = 0
            )

            // 1) Update modifiedExercises
            val exercisesList = session.modifiedExercises.exercises.toMutableList()
            val exercise = exercisesList[exerciseIndex]

            val updatedSetsForExercise = exercise.sets.toMutableList().apply {
                add(newSet)
            }

            exercisesList[exerciseIndex] = exercise.copy(sets = updatedSetsForExercise)

            val updatedModifiedExercises = session.modifiedExercises.copy(
                exercises = exercisesList
            )

            // 2) Update completedSets
            val completedSetsList = session.completedSets.toMutableList()

            // Make sure there is a row for this exercise
            if (exerciseIndex >= completedSetsList.size) {
                // No row yet -> create one with same size as updated sets, all false
                completedSetsList.add(
                    MutableList(updatedSetsForExercise.size) { false }
                )
            } else {
                // Row exists -> just add a new "false" for the new set
                val row = completedSetsList[exerciseIndex].toMutableList().apply {
                    add(false)
                }
                completedSetsList[exerciseIndex] = row
            }

            session.copy(
                modifiedExercises = updatedModifiedExercises,
                completedSets = completedSetsList
            )
        }
    }

    /**
     * Updates how many minutes the rest timer should last
     *
     * @param minutes The duration in minutes for the rest timer
     */
    fun updateTimerMinutes(minutes: Int) {
        activeWorkoutManager.updateSession { it.copy(timerMinutes = minutes) }
    }

    /**
     * Starts the rest timer countdown.
     *
     * Converts timerMinutes to seconds and begins countdown.
     * Typically triggered automatically when a set is marked as completed.
     * The actual countdown logic runs in ActiveWorkoutManager.
     */
    fun startTimer() {
        activeWorkoutManager.updateSession {
            it.copy(
                isTimerRunning = true,
                timerSecondsRemaining = it.timerMinutes * 60
            )
        }
    }

    /**
     * Updates the workout notes/comments
     *
     * @param notes The note text to save with the workout session
     */
    fun updateNotes(notes: String) {
        activeWorkoutManager.updateSession { it.copy(notes = notes) }
    }

    /**
     * Updates the workout template during an active session.
     *
     * Can be used to add/remove exercises or modify sets mid-workout.
     * Currently not implemented in the UI - prepared for future functionality.
     *
     * @param template The modified workout template to apply
     */
    fun updateExercise(template: WorkoutTemplate) {
        activeWorkoutManager.updateSession { it.copy(modifiedExercises = template) }
    }

    /**
     * Completes the active workout and saves it to history.
     *
     * This function:
     * 1. Calculates workout duration from start to finish time
     * 2. Filters out incomplete sets (only saves checked sets)
     * 3. Excludes exercises with no completed sets
     * 4. Saves the session to database/API via use case
     * 5. Clears the active workout session
     *
     * The save operation runs in a coroutine to avoid blocking the UI.
     * Updates _uiState to show loading state and handle errors.
     */
    fun completeWorkout() {
        // Get the currently active workout, if none active, return early
        val session = activeWorkoutManager.activeSession.value ?: return
        // Calculate how long the workout took
        val duration = Duration.between(session.startTime, LocalDateTime.now())

        // Build the session object with unique UUID
        val finishedWorkout = Session(
            sessionId = UUID.randomUUID().toString(),
            name = session.template.name,
            exercises = session.modifiedExercises.exercises.mapIndexedNotNull { exIndex, exSet ->
                // Filter to only completed sets ( we do not want to log incompleted sets )
                val completedSetsForExercise = exSet.sets.filterIndexed { setIndex, _ ->
                    exIndex < session.completedSets.size &&
                    setIndex < session.completedSets[exIndex].size &&
                    session.completedSets[exIndex][setIndex]
                }

                // Only include the exercise if at least one set was completed
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
                    // If no set was completed for exercise, null (mapIndexedNotNull will skip)
                } else null
            },
            duration = duration,
            date = LocalDate.now(),
            note = session.notes
        )

        // ViewmodelScope is its own scope with its own lifecycle
        // Run on coroutine, won't block UI
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Save workout to database / API
                postHistoryWorkoutUseCase(finishedWorkout)
                // Clear the active workout if success
                activeWorkoutManager.completeWorkout()
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to save workout"
                    )
                }
            }
        }
    }

    /**
     * Discards the active workout without saving to history.
     *
     * Prepared for future "Cancel Workout" or "Discard Progress" functionality.
     * Currently not implemented in the UI.
     * TODO: Implement UI confirmation dialog before discarding
     */
    fun cancelWorkout() {
        activeWorkoutManager.cancelWorkout()
    }

    /**
     * Returns the current time formatted as HH:mm (24-hour format).
     *
     * Used to display the current time on the active workout screen,
     * helping users track when they started/resumed their workout.
     *
     * @return Current time string (e.g., "14:35" or "09:20")
     */
    fun getCurrentTimeString(): String {
        val currentTime = LocalTime.now() // current time
        val formatter = DateTimeFormatter.ofPattern("HH:mm") // 24-hour format
        return currentTime.format(formatter)
    }

    fun addExerciseToActiveSession(exercise: Exercise) {
        activeWorkoutManager.updateSession { session ->
            // Build the new exercise in the same type as modifiedExercises.exercises
            val newExercise = TemplateExercise(
                exerciseId = exercise.exerciseId,
                name = exercise.name,
                sets = mutableListOf(
                    Set(
                        rep = 0,
                        kg = 0.0,
                        typeSet = 0,
                    )
                )
            )

            // 1) Add exercise to modifiedExercises
            val updatedExercises = session.modifiedExercises.exercises
                .toMutableList()
                .apply { add(newExercise) }

            // 2) Add a completedSets row for this new exercise (all false)
            val newCompletedRow = MutableList(newExercise.sets.size) { false }
            val updatedCompletedSets = session.completedSets
                .toMutableList()
                .apply { add(newCompletedRow) }

            // 3) Return updated session
            session.copy(
                modifiedExercises = session.modifiedExercises.copy(
                    exercises = updatedExercises
                ),
                completedSets = updatedCompletedSets
            )
        }
    }
}