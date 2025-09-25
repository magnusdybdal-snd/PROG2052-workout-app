package com.example.workoutapp.core.core_navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data used in navigation-bar
 * @see com.example.workoutapp.app.MainScreen
 */
data class NavItem(
    val label : String,
    val route : String,
    val icon : ImageVector
)
