package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.models.WorkoutExercise
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.Duration
import com.example.workoutapp.domain.models.Set as WorkoutSet

class GetHistoryWorkoutUseCaseTest {
    // initialize variables used in test, but not before they are needed
    private lateinit var repository: HistoryWorkoutRepository
    private lateinit var useCase: GetHistoryWorkoutUseCase

    @Before // Runs before each test.
    fun setup() {
        repository = mockk()
        useCase = GetHistoryWorkoutUseCase(repository)
    }

    @Test
    fun `invoke returns flow of workouts from repository`() = runTest {
        // Given
        val expectedWorkouts = listOf(
            HistoryWorkout(
                id = "1",
                name = "Morning Workout",
                duration = Duration.ofMinutes(45),
                date = LocalDate.of(2024, 1, 15),
                exercises = listOf(
                    WorkoutExercise(
                        exerciseId = "ex1",
                        name = "Bench Press",
                        sets = listOf(
                            WorkoutSet(rep = 10, kg = 80.0, typeSet = 0),
                            WorkoutSet(rep = 8, kg = 85.0, typeSet = 0)
                        )
                    )
                ),
                note = "Felt strong today"
            ),
            HistoryWorkout(
                id = "2",
                name = "Evening Leg Day",
                duration = Duration.ofMinutes(60),
                date = LocalDate.of(2024, 1, 16),
                exercises = listOf(
                    WorkoutExercise(
                        exerciseId = "ex2",
                        name = "Squats",
                        sets = listOf(
                            WorkoutSet(rep = 12, kg = 100.0, typeSet = 0),
                            WorkoutSet(rep = 10, kg = 110.0, typeSet = 0),
                            WorkoutSet(rep = 8, kg = 120.0, typeSet = 0)
                        )
                    )
                ),
                note = ""
            )
        )
        every { repository.observeHistoryWorkouts() } returns flowOf(expectedWorkouts)

        // When
        val result = useCase.invoke().first() // first flow.

        // Then
        assertEquals(expectedWorkouts, result)
        assertEquals(2, result.size) // Should return two exercises
    }

    @Test
    fun `invoke returns empty list when no workouts exist`() = runTest {
        // Given
        every { repository.observeHistoryWorkouts() } returns flowOf(emptyList())

        // When
        val result = useCase.invoke().first()

        // Then
        assertEquals(emptyList<HistoryWorkout>(), result)
    }

    @Test
    fun `syncNow fetches workouts from repository`() = runTest {
        // Given
        val expectedWorkouts = listOf(
            HistoryWorkout(
                id = "1",
                name = "Morning Workout",
                duration = Duration.ofMinutes(45),
                date = LocalDate.of(2024, 1, 15),
                exercises = listOf(
                    WorkoutExercise(
                        exerciseId = "ex1",
                        name = "Bench Press",
                        sets = listOf(
                            WorkoutSet(rep = 10, kg = 80.0, typeSet = 0),
                            WorkoutSet(rep = 8, kg = 85.0, typeSet = 0)
                        )
                    )
                ),
                note = "Felt strong today"
            )
        )
        coEvery { repository.getHistoryWorkouts() } returns expectedWorkouts

        // When
        val result = useCase.syncNow()

        // Then
        assertEquals(expectedWorkouts, result)
        coVerify(exactly = 1) { repository.getHistoryWorkouts() }
    }

    @Test
    fun `syncNow returns empty list when sync has no data`() = runTest {
        // Given
        coEvery { repository.getHistoryWorkouts() } returns emptyList()

        // When
        val result = useCase.syncNow()

        // Then
        assertEquals(emptyList<HistoryWorkout>(), result)
    }
}