package com.example.workoutapp.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the Workout App.
 *
 * Entry point for Hilt dependency injection. The @HiltAndroidApp annotation
 * triggers Hilt's code generation and creates a dependency graph at the
 * application level.
 *
 * All dependencies marked as @Singleton are scoped to this application instance
 * and will live for the entire app lifecycle.
 */
@HiltAndroidApp
class WorkoutApp : Application()
