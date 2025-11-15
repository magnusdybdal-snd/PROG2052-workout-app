package com.example.workoutapp.features.exercises

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.R
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.ExerciseDetailPage
import com.example.workoutapp.core.core_ui.composable.ExerciseDisplayBox
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.core.core_ui.composable.modifiers.PageColumnModifier
import com.example.workoutapp.core.core_ui.composable.PageHeading
import com.example.workoutapp.domain.models.Exercise

/**
 * Displays Exercises page
 * @param modifier
 * @param navController
 * @param viewModel
 */
@Composable
fun ExercisesPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ExercisesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val cs = MaterialTheme.colorScheme

    var showOverlay by remember { mutableStateOf(false) }
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    var searchString by remember { mutableStateOf("") }

    when {
        state.isLoading -> LoadingStateView()
        state.error != null -> ErrorStateView(state.error)

        else -> {
            Column(
                modifier = PageColumnModifier(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PageHeading(
                    displayText = stringResource(R.string.exercises)
                )

                TextField(
                    value = searchString,
                    onValueChange = { searchString = it },
                    label = {
                        Text(
                            "Search exercise",
                            color = cs.onBackground
                        )
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )

                // Pre filter list
                val filteredList = remember(state.exercises, searchString) {
                    if (searchString.isBlank()) {
                        state.exercises
                    } else {
                        state.exercises.filter {
                            it.name.contains(searchString, ignoreCase = true)
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),   // ← THIS MAKES THE LIST SCROLL PROPERLY
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(filteredList, { it.exerciseId }) { exercise ->
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedExercise = exercise
                                    showOverlay = true
                                }
                        ) {
                            ExerciseDisplayBox(exercise)
                        }
                    }
                }
            }
        }
    }
    ExerciseDetailPage(
        showOverlay = showOverlay,
        onDismiss = {
            showOverlay = false },
        cs = cs,
        exercise = selectedExercise
    )
}
