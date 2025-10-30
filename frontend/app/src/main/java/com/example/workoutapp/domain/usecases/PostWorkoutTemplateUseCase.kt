package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.NewTemplate
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import javax.inject.Inject

class PostWorkoutTemplateUseCase @Inject constructor(
    private val repository: WorkoutTemplateRepository
) {
    suspend operator fun invoke(newTemplate: NewTemplate) {
        repository.postWorkoutTemplate(newTemplate)
    }
}