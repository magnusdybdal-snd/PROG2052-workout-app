// app/MainViewModel.kt
package com.example.workoutapp.app

import androidx.lifecycle.ViewModel
import com.example.workoutapp.domain.session_manager.ActiveWorkoutManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val activeWorkoutManager: ActiveWorkoutManager
) : ViewModel()