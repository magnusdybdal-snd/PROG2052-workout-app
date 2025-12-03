package com.example.workoutapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.core.core_ui.theme.WorkoutAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for the Workout App.
 *
 * Single-activity architecture using Jetpack Compose and Navigation Compose.
 * The @AndroidEntryPoint annotation enables Hilt dependency injection in this
 * activity and all Composables within it.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkoutAppTheme {
                val navController = rememberNavController()
                MainScreen(navController = navController)
            }
        }
    }
}
