// MainScreen.kt
package com.example.workoutapp.app

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.workoutapp.core.core_navigation.NavItem
import com.example.workoutapp.core.core_navigation.Routes
import com.example.workoutapp.features.history_detail.HistoryDetailPage
import com.example.workoutapp.core.core_ui.theme.AppNavBar
import com.example.workoutapp.data.database.UserPreferences
import com.example.workoutapp.features.active_workout.ActiveWorkoutPage
import com.example.workoutapp.features.edit_template.EditTemplatePage
import com.example.workoutapp.features.exercises.ExercisesPage
import com.example.workoutapp.features.history.HistoryPage
import com.example.workoutapp.features.home.HomePage
import com.example.workoutapp.features.login.LoginPage
import com.example.workoutapp.features.new_template.NewTemplatePage

/**
 * Main screen
 * @param modifier
 * @param navController
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    preferences: UserPreferences = UserPreferences(LocalContext.current) // for checking if user logged in
) {

    val navItemList = listOf(
        NavItem("History", Routes.HISTORY, Icons.Default.DateRange),
        NavItem("Workouts", Routes.WORKOUT, Icons.Default.PlayArrow),
        NavItem("Exercises", Routes.EXERCISES, Icons.Default.Person)
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    // Show or hide the bottom-bar.
    val showBottomBar = navItemList.any{ item ->
        currentDestination.isOnRoute(item.route)}

    val token by preferences.token.collectAsState(initial = null)
    val startDestination = remember {
        if (token == null) Routes.LOGIN else Routes.WORKOUT
    }

    LaunchedEffect(token) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route

        // User logged out, navigate to login page
        if (token == null && currentRoute != Routes.LOGIN) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        // User just logged in, navigate to home
        } else if (token != null && currentRoute == Routes.LOGIN) {
            navController.navigate(Routes.WORKOUT) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        }
    }


    Scaffold(
        bottomBar = {
            val cs = MaterialTheme.colorScheme
            if (showBottomBar) { // check if condition is true (show/hide bottom-bar)
                NavigationBar(
                    containerColor = cs.surface,
                    contentColor = cs.onSurface
                ) {
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
                            label = { Text(item.label) },
                            colors = AppNavBar.itemColors()
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination, // First page, client would see
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Routes.LOGIN)     { LoginPage(Modifier,navController) }
            composable(Routes.WORKOUT)   { HomePage(Modifier, navController) }
            composable(Routes.EXERCISES) { ExercisesPage(Modifier, navController) }
            composable(Routes.HISTORY)   { HistoryPage(Modifier, navController) }
            composable(Routes.NEWTEMP)   { NewTemplatePage(Modifier, navController) }
            composable(
                route = Routes.EDITTEMP,
                arguments = listOf(navArgument("tempId") { type = NavType.StringType })
            ) { backStackEntry ->
                val templateId = backStackEntry.arguments?.getString("tempId") ?: "0"
                EditTemplatePage(templateId.toInt(), Modifier, navController)
            }
            composable(Routes.WORKTEMP) {
                ActiveWorkoutPage(
                    modifier = Modifier,
                    navController = navController
                )
            }

            composable(
                route = Routes.HISTORY_DETAIL,
                arguments = listOf(navArgument("workoutId") { type = NavType.StringType })
            ) { backStackEntry ->
                val arg = backStackEntry.arguments?.getString("workoutId") ?: ""
                val workoutId = Uri.decode(arg) // safe if you encoded
                HistoryDetailPage(
                    sessionId = workoutId,
                    navController = navController
                )
            }
        }
    }
}

private fun NavDestination?.isOnRoute(route: String): Boolean {
    if (this == null) return false
    return hierarchy.any { it.route == route }
}