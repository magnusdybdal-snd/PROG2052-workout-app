package com.example.workoutapp.features.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.HistoryDisplayBox
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.core.core_ui.modifiers.PageColumnModifier
import com.example.workoutapp.core.core_ui.composable.PageHeading
/**
 * Displays History page
 * @param modifier
 * @param navController
 * @param viewModel
 */
@Composable
fun HistoryPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val cs = MaterialTheme.colorScheme

    // Checks if user navigates back to history and reloads the composable (refreshes histories)
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(currentBackStackEntry) {
        if (currentBackStackEntry?.destination?.route == "history") {
            viewModel.loadHistory()
        }
    }

    when {
        state.isLoading -> LoadingStateView()
        state.error != null -> ErrorStateView(state.error)
        else -> {
            Column( // Workout Header
                modifier = PageColumnModifier(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PageHeading(
                    displayText = "History"
                )
                Column( // Boxes
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                ) {
                    // Loops trough every month that the map is grouped by
                    state.groupedHistory.forEach { (monthHeader, workoutsInMonth) ->
                        // Header text for each month
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Name of the month and year
                            Text(
                                text = monthHeader,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = cs.onBackground,
                            )
                            // Number of workouts that month
                            Text(
                                text = workoutsInMonth.size.toString() + " workouts",
                                fontSize = 14.sp,
                                color = cs.secondary,

                            )
                        }
                        // Looping over each workout within the month
                        workoutsInMonth.forEach {
                            HistoryDisplayBox(it)
                            }
                        }
                    }
                }
            }
        }
    }


