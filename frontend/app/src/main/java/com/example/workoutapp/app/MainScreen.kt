// MainScreen.kt
package com.example.workoutapp.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.workoutapp.core.core_navigation.NavItem
import com.example.workoutapp.core.core_navigation.Routes
import com.example.workoutapp.features.excercices.ExercisesPage
import com.example.workoutapp.features.history.HistoryPage
import com.example.workoutapp.features.home.WorkoutPage
import com.example.workoutapp.features.active_workout.ActWork

@Composable
fun MainScreen(modifier: Modifier = Modifier, navController: NavHostController) {

    val navItemList = listOf(
        NavItem("History", Routes.HISTORY, Icons.Default.DateRange),
        NavItem("Workouts", Routes.WORKOUT, Icons.Default.PlayArrow),
        NavItem("Exercises", Routes.EXERCISES, Icons.Default.Person)
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    // Show or hide the bottombar.
    val showBottomBar = navItemList.any{ item ->
        currentDestination.isOnRoute(item.route)}


    Scaffold(
        bottomBar = {
            if (showBottomBar) { // check if condition is true (show/hide bottombar)
                NavigationBar {
                    navItemList.forEach { item ->
                        NavigationBarItem(
                            selected = currentDestination.isOnRoute(item.route),
                            onClick = {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.WORKOUT,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Routes.WORKOUT)   { WorkoutPage(Modifier, navController) }
            composable(Routes.EXERCISES) { ExercisesPage(Modifier, navController) }
            composable(Routes.HISTORY)   { HistoryPage(Modifier, navController) }
                                                            // "Test" to be workout name
            composable(Routes.WORKTEMP)  { ActWork("Test",Modifier, navController) }
        }
    }
}

private fun NavDestination?.isOnRoute(route: String): Boolean {
    if (this == null) return false
    return hierarchy.any { it.route == route }
}