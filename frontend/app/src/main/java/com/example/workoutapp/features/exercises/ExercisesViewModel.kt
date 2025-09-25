package com.example.workoutapp.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.usecases.GetExercisesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExercisesViewModel(
    private val getExercisesUseCase: GetExercisesUseCase
) : ViewModel() {

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            val result = getExercisesUseCase()
            _exercises.value = result
        }
    }
}
