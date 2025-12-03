// app/MainViewModel.kt
package com.example.workoutapp.app

import androidx.lifecycle.ViewModel
import com.example.workoutapp.domain.session_manager.ActiveWorkoutManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the main screen and app-wide state.
 *
 * Provides access to the ActiveWorkoutManager singleton for displaying
 * the floating action button (FAB) when a workout is in progress. This allows
 * the FAB to appear on any screen when the user has an active workout session.
 *
 * @property activeWorkoutManager Singleton managing active workout state
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    val activeWorkoutManager: ActiveWorkoutManager
) : ViewModel()