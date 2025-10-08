package com.example.workoutapp.data.repositories

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.data.api.dto.HistoryWorkoutDto
import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.models.Set
import com.example.workoutapp.domain.models.WorkoutExercise
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

/**
 * Implementation for the HistoryWorkoutRepository.
 * Built by injecting the API service
 */
class HistoryWorkoutRepositoryImpl @Inject constructor(
    private val api: ApiService
): HistoryWorkoutRepository {

    /**
     * Converts the Data Transfer Object built from the API json into the
     * domain model HistoryWorkout (nested with WorkoutExercise and Set)
     */
    override suspend fun getHistoryWorkouts(): List<HistoryWorkout> {
        return api.getHistoryWorkouts().map { dto ->
            val localTime = LocalTime.parse(dto.duration)
            val duration = Duration.ofSeconds(localTime.toSecondOfDay().toLong())

            HistoryWorkout(
                historyWorkoutId = dto.historyWorkoutId,
                name = dto.name,
                date = LocalDate.parse(dto.date),
                duration = duration,
                note = dto.note,
                exercises = dto.exercises.map { workoutExerciseDto ->
                    WorkoutExercise(
                        exercise = Exercise(
                            name = workoutExerciseDto.exercise.name,
                            targetMuscles = workoutExerciseDto.exercise.targetMuscles,
                            bodyParts = workoutExerciseDto.exercise.bodyParts,
                            equipments = workoutExerciseDto.exercise.equipments,
                            secondaryMuscles = workoutExerciseDto.exercise.secondaryMuscles,
                            gifUrl = workoutExerciseDto.exercise.gifUrl,
                            instructions = workoutExerciseDto.exercise.instructions
                        ),
                        sets = workoutExerciseDto.sets.map { setDto ->
                            Set(
                                rep = setDto.rep,
                                kg = setDto.kg,
                                typeSet = setDto.typeSet
                            )
                        }
                    )
                }
            )
        }
    }

    override suspend fun postHistoryWorkout(historyWorkoutDto: HistoryWorkoutDto) {
        api.postHistoryWorkout(historyWorkoutDto)
    }
}