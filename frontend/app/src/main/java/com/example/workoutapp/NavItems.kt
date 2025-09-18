package com.example.workoutapp

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data used in navigation-bar
 * @see MainScreen
 */
data class NavItem(
    val label : String,
    val route : String,
    val icon : ImageVector
)
