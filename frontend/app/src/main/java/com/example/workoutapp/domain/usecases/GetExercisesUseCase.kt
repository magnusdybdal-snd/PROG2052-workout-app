package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.repositories.ExercisesRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Use case for retrieving exercises from the library.
 *
 * Returns a reactive StateFlow that allows the UI to automatically update
 * when the exercise library changes
 *
 * @property repository The exercises repository for data access
 */
class GetExercisesUseCase @Inject constructor(
    private val repository: ExercisesRepository
) {
    /**
     * Observes the exercise library as a reactive StateFlow.
     *
     * @return StateFlow emitting the current list of all exercises
     */
    operator fun invoke(): StateFlow<List<Exercise>> {
        return repository.observeExercises()
    }
}
