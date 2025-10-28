package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.Session
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import jakarta.inject.Inject

class DeleteWorkoutTemplateUseCase @Inject constructor(
    private val repository: WorkoutTemplateRepository
) {
    suspend operator fun invoke(workoutTemplate: WorkoutTemplate) {
        repository.deleteWorkoutTemplate(workoutTemplate)
    }
}
