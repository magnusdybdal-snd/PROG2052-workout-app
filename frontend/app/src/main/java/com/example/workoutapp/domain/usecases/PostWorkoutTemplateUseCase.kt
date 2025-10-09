package com.example.workoutapp.domain.usecases

import com.example.workoutapp.data.api.dto.WorkoutTemplateDto
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import javax.inject.Inject

class PostWorkoutTemplateUseCase @Inject constructor(
    private val repository: WorkoutTemplateRepository
) {
    suspend operator fun invoke(workoutTemplateDto: WorkoutTemplateDto) {
        repository.postWorkoutTemplate(workoutTemplateDto)
    }
}