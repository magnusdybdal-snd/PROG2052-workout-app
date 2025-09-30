package com.example.workoutapp.data.repositories

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.models.Set
import com.example.workoutapp.domain.models.TemplateExercise
import com.example.workoutapp.domain.models.WorkoutTemplate
import javax.inject.Inject

class WorkoutTemplateRepositoryImpl @Inject constructor(
    private val api: ApiService
): WorkoutTemplateRepository {

    override suspend fun getWorkoutTemplates(): List<WorkoutTemplate> {
        return api.getWorkoutTemplates().map { dto ->
            WorkoutTemplate(
                templateId = dto.templateId,
                name = dto.name,
                exercises = dto.exercises.map { templateExerciseDto ->
                    TemplateExercise(
                        exercise = Exercise(
                            name = templateExerciseDto.exercise.name,
                            targetMuscles = templateExerciseDto.exercise.targetMuscles,
                            bodyParts = templateExerciseDto.exercise.bodyParts,
                            equipments = templateExerciseDto.exercise.equipments,
                            secondaryMuscles = templateExerciseDto.exercise.secondaryMuscles,
                            gifUrl = templateExerciseDto.exercise.gifUrl,
                            instructions = templateExerciseDto.exercise.instructions
                        ),
                        sets = templateExerciseDto.set.map { setDto ->
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
}