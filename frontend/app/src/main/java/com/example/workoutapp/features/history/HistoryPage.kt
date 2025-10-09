package com.example.workoutapp.features.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.LoadingStateView

/**
 * Displays History page
 */
@Composable
fun HistoryPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

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
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp)
                    //.padding(bottom = 80.dp) // padding to compensate for navbar - navigationBarsPadding()?
                    .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 80.dp)
                        .padding(bottom = 20.dp),
                    text = "History",
                    fontSize = 50.sp,
                    //  fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                )

                Column( // Boxes
                    modifier = Modifier
                        .widthIn(max = 700.dp)
                        .background(Color.White)
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,

                    //horizontalAlignment = Alignment.CenterHorizontally
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
                                color = Color.Black,
                            )
                            // Number of workouts that month
                            Text(
                                text = workoutsInMonth.size.toString() + " workouts",
                                fontSize = 14.sp,
                                color = Color.DarkGray,

                            )
                        }

                        // Looping over each workout within the month
                        workoutsInMonth.forEach {
                            Box( // vertical space between boxes
                                modifier = Modifier.padding(vertical = 6.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(width = 2.dp, color = Color.Black)
                                        .padding(vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        modifier = Modifier.padding(start = 30.dp)
                                    ) {
                                        Text( // Workout name
                                            text = it.name,
                                            fontSize = 20.sp
                                        )
                                        Row {
                                            Icon(
                                                imageVector = Icons.Default.Info,
                                                contentDescription = "",
                                            )
                                            Text(
                                                text = "%02d:%02d:%02d".format(
                                                    it.duration.toHours(),
                                                    it.duration.toMinutes() % 60,
                                                    it.duration.toSeconds() % 60
                                                ),
                                                modifier.padding(start = 6.dp)
                                            )
                                            Row(
                                                modifier = Modifier.padding(start = 10.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.AccountCircle,
                                                    contentDescription = "",
                                                )
                                                Text(// Volume
                                                    text = it.totalVolume.toString(),
                                                    modifier.padding(start = 6.dp)
                                                )

                                            }
                                        }

                                    } // end column 1 "workout text
                                    Text(
                                        modifier = Modifier.padding(end = 30.dp),
                                        text = it.date.toString(),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

