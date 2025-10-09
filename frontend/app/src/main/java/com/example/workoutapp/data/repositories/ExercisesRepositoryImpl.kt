package com.example.workoutapp.data.repositories

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.domain.repositories.ExercisesRepository
import com.example.workoutapp.domain.models.Exercise
import javax.inject.Inject

class ExercisesRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ExercisesRepository {

    // Gets the exercises via the api service and maps it to a domain model version of Exercise(list)
    override suspend fun getExercises(): List<Exercise> {
        return api.getExercises().map { dto ->
            Exercise(
                exerciseId = dto.exerciseId,
                name = dto.name,
                targetMuscles = dto.targetMuscles,
                bodyParts = dto.bodyParts,
                equipments = dto.equipments,
                secondaryMuscles = dto.secondaryMuscles,
                gifUrl = dto.gifUrl,
                instructions = dto.instructions
            )
        }
    }
}

