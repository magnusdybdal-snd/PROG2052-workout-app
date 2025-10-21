package com.example.workoutapp.data.repositories

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.data.api.dto.HistoryWorkoutDto
import com.example.workoutapp.data.api.dto.WorkoutTemplateDto
import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.models.NewTemplate
import com.example.workoutapp.domain.models.Set
import com.example.workoutapp.domain.models.TemplateExercise
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import javax.inject.Inject

/**
 * Implementation for the WorkoutTemplateRepository.
 * Built by injecting the API service
 */
class WorkoutTemplateRepositoryImpl @Inject constructor(
    private val api: ApiService
): WorkoutTemplateRepository {

    /**
     * Converts the Data Transfer Object built from the API json into the
     * domain model WorkoutTemplate (nested with TemplateExercise and Set)
     */
    override suspend fun getWorkoutTemplates(): List<WorkoutTemplate> {
        return api.getWorkoutTemplates().map { dto ->
            WorkoutTemplate(
                templateId = dto.templateId,
                name = dto.name,
                exercises = dto.exercises.map { templateExerciseDto ->
                    TemplateExercise(
                        exercise = Exercise(
                            exerciseId = templateExerciseDto.exercise.exerciseId,
                            name = templateExerciseDto.exercise.name,
                            targetMuscles = templateExerciseDto.exercise.targetMuscles,
                            bodyParts = templateExerciseDto.exercise.bodyParts,
                            equipments = templateExerciseDto.exercise.equipments,
                            secondaryMuscles = templateExerciseDto.exercise.secondaryMuscles,
                            gifUrl = templateExerciseDto.exercise.gifUrl,
                            instructions = templateExerciseDto.exercise.instructions
                        ),
                        sets = templateExerciseDto.sets.map { setDto ->
                            Set(
                                rep = setDto.rep,
                                kg = setDto.kg,
                                typeSet = setDto.typeSet
                            )
                        } as MutableList<Set>
                    )
                } as MutableList<TemplateExercise>
            )
        }
    }

    override suspend fun postWorkoutTemplate(newTemplate: NewTemplate) {
        api.postWorkoutTemplate(newTemplate)
    }
}