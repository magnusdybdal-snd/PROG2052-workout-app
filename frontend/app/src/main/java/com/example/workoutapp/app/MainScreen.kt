// MainScreen.kt
package com.example.workoutapp.app

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.example.workoutapp.core.utils.isTokenExpired
import com.example.workoutapp.data.database.UserPreferences
import com.example.workoutapp.features.active_workout.ActiveWorkoutPage
import com.example.workoutapp.features.edit_template.EditTemplatePage
import com.example.workoutapp.features.exercises.ExercisesPage
import com.example.workoutapp.features.history.HistoryPage
import com.example.workoutapp.features.home.HomePage
import com.example.workoutapp.features.login.LoginPage
import com.example.workoutapp.features.new_template.NewTemplatePage
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.text.font.FontWeight
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.domain.models.AuthState
import com.example.workoutapp.features.auth.AuthViewModel

/**
 * Main screen
 * @param modifier
 * @param navController
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    //
    // Check authenticatoin state from viewmodel
    //

    when (authState) {
        is AuthState.Loading -> {
            LoadingStateView()
            return
        }
        is AuthState.Unauthenticated -> {
            LoginPage(Modifier, navController)
            return
        }
        // Authenticated, continue below
        is AuthState.Authenticated -> Unit
    }

    // Main content if authenticated below:
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

    // Check if there is an active workout going on
    val activeSession by mainViewModel.activeWorkoutManager.activeSession.collectAsState()
    val hasActiveWorkout = activeSession != null
    val isOnWorkoutPage = currentDestination?.route == Routes.WORKTEMP

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
        },

        floatingActionButton = {
            val cs = MaterialTheme.colorScheme
            // Show floating action button if not on workout page and has an active workout
            if (hasActiveWorkout && !isOnWorkoutPage) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Routes.WORKTEMP) {
                            launchSingleTop = true
                        }
                    },
                    containerColor = cs.primary,
                    contentColor = cs.onPrimary
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),

                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Resume Workout"
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            // Workout name (header)
                            Text(
                                text = activeSession?.template?.name ?: "Workout",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            // Timer or "Resume Workout" (subtitle)
                            val subtitleText = activeSession?.let { session ->
                                if (session.isTimerRunning) {
                                    val minutes = session.timerSecondsRemaining / 60
                                    val seconds = session.timerSecondsRemaining % 60
                                    "Rest timer: ${String.format("%d:%02d", minutes, seconds)}"
                                } else {
                                    "Resume Workout"
                                }
                            } ?: "Resume Workout"

                            Text(
                                text = subtitleText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.WORKOUT, // First page user would see
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