package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.models.WorkoutExercise
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.LocalDate
import com.example.workoutapp.domain.models.Set as WorkoutSet

class DeleteHistoryWorkoutUseCaseTest{

    private lateinit var repository: HistoryWorkoutRepository
    private lateinit var useCase: DeleteHistoryWorkoutUseCase

   @Before
   fun setup(){
       repository = mockk()
       useCase = DeleteHistoryWorkoutUseCase(repository)
   }

    @Test
    fun `delete removed workout from repository`() = runTest{
        //Given
        val workoutToDelete = HistoryWorkout(
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

        // Mock the repository assures that the test don't delete real data.
        coEvery { repository.deleteHistoryWorkout(workoutToDelete) } returns Unit

        // When
        useCase.invoke(workoutToDelete)

        // Then
        // Only delete exactly one workout
        coVerify(exactly = 1) { repository.deleteHistoryWorkout(workoutToDelete) }




    }


}