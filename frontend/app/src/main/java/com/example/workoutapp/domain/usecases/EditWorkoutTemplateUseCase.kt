package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import javax.inject.Inject

class EditWorkoutTemplateUseCase @Inject constructor(
    private val repository: WorkoutTemplateRepository
) {
    suspend operator fun invoke(workoutTemplate: WorkoutTemplate) {
        repository.editWorkoutTemplate(workoutTemplate)
    }
}
