package com.example.workoutapp.features.active_workout

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.R
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.ExerciseWorkoutHeaderRow
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.core.core_ui.composable.RoundBackButton
import com.example.workoutapp.core.core_ui.composable.RoundedButton
import com.example.workoutapp.core.core_ui.composable.TimerTextField
import com.example.workoutapp.core.core_ui.composable.WorkoutTextField
import com.example.workoutapp.core.core_ui.composable.modifiers.BorderBoxModifier
import com.example.workoutapp.core.core_ui.theme.AppCheckBox
import com.example.workoutapp.core.core_ui.theme.AppOutlinedTextField.outlinedFieldColors
import com.example.workoutapp.core.core_ui.theme.AppTextButton.textButtonColor

/**
 * Displays the active workout screen where users can track their sets in real-time.
 *
 * Features:
 * - Real-time timer countdown for rest periods
 * - Checkbox tracking for completed sets
 * - Editable weight and reps for each set
 * - Workout notes dialog on completion
 * - Auto-navigation back if no active session exists
 *
 * @param modifier Modifier to be applied to the root composable
 * @param navController Navigation controller for screen transitions
 * @param viewModel ViewModel managing workout state and operations
 */
@Composable
fun ActiveWorkoutPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ActWorkViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val cs = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current
    val activeSession by viewModel.activeSession.collectAsState()

    // Guard: Navigate back if no active workout session exists
    if (activeSession == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    when {
        state.isLoading -> LoadingStateView()
        state.error != null -> ErrorStateView(state.error)
        else -> {
            // Safe call: session could theoretically become null between check and usage
            activeSession?.let { session ->
                Scaffold(
                    bottomBar = {
                        // Show full-screen timer countdown when rest timer is active
                        if (session.isTimerRunning) {
                            NavigationBar {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color = cs.background),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val minutes = session.timerSecondsRemaining / 60
                                    val seconds = session.timerSecondsRemaining % 60
                                    Text(
                                        text = String.format("%d:%02d", minutes, seconds),
                                        fontSize = 70.sp,
                                        textAlign = TextAlign.Center,
                                        color = cs.onBackground
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    val bottomBarHeight = if (session.isTimerRunning) 120.dp else 0.dp


                    Column(
                        modifier = Modifier
                            .verticalScroll(state = rememberScrollState())
                            .padding(bottom = bottomBarHeight)
                            .imePadding()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        focusManager.clearFocus()
                                    }
                                )
                            }
                    ) {
                        RoundBackButton(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                        )
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        ) {
                            // Header row: Current time and Finish button
                            Row(
                                modifier = BorderBoxModifier(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(viewModel.getCurrentTimeString(), fontSize = 20.sp)

                                var showDialog by remember { mutableStateOf(false) }
                                var notes by remember { mutableStateOf(session.notes) }

                                RoundedButton(
                                    buttonText = stringResource(R.string.finish),
                                    onClick = {
                                        focusManager.clearFocus() // clear focus to ensure that active textfield is stored.
                                        showDialog = true
                                    },
                                )
                                // Finish workout dialog with optional notes
                                if (showDialog) {
                                    AlertDialog(
                                        containerColor = cs.tertiary,
                                        textContentColor = cs.onTertiary,
                                        titleContentColor = cs.onTertiary,
                                        onDismissRequest = { showDialog = false },
                                        title = { Text(text = "Add a note before finishing?") },
                                        text = {
                                            OutlinedTextField(
                                                colors = outlinedFieldColors(),
                                                value = notes,
                                                onValueChange = {
                                                    notes = it
                                                    viewModel.updateNotes(it)
                                                },
                                                label = { Text(text = stringResource(R.string.workout_notes)) },
                                            )
                                        },
                                        confirmButton = {
                                            TextButton(
                                                onClick = {
                                                    focusManager.clearFocus() // Clears one more time to be safe.
                                                    viewModel.completeWorkout()
                                                    showDialog = false
                                                    navController.popBackStack()
                                                },
                                                colors = textButtonColor()
                                            ) {
                                                Text(text = stringResource(R.string.finish_workout))
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(
                                                onClick = { showDialog = false },
                                                colors = textButtonColor()
                                            ) {
                                                Text(text = stringResource(R.string.cancel))
                                            }
                                        }
                                    )
                                }
                            }
                            // Workout template name
                            Text(
                                session.template.name,
                                fontSize = 30.sp,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                            // Rest timer duration selector
                            Box(
                                modifier = Modifier.width(120.dp)
                            ) {
                                TimerTextField(
                                    time = session.timerMinutes,
                                    onTimeChange = { viewModel.updateTimerMinutes(it) }
                                )
                            }
                            // Exercise list with sets, reps, weight, and completion checkboxes
                            Column(
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier.padding(top = 10.dp),
                            ) {
                                session.modifiedExercises.exercises.forEachIndexed { exerciseIndex, exSet ->
                                    // Exercise name header
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp)
                                    ) {
                                        Text(
                                            exSet.name,
                                            fontSize = 15.sp
                                        )
                                    }

                                    // Header Row
                                    ExerciseWorkoutHeaderRow()

                                    // Loop through each set
                                    exSet.sets.forEachIndexed { setIndex, set ->
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 8.dp)
                                                .height(75.dp)
                                        ) {
                                            // Set Number
                                            Text(
                                                "${setIndex + 1}",
                                                fontSize = 15.sp,
                                                modifier = Modifier.width(50.dp)
                                            )

                                            // KG TextField
                                            WorkoutTextField(
                                                label = stringResource(R.string.kg),
                                                exSet = exSet,
                                                type = "kg",
                                                set = set
                                            )

                                            // Reps TextField
                                            WorkoutTextField(
                                                label = stringResource(R.string.reps),
                                                exSet = exSet,
                                                type = "reps",
                                                set = set
                                            )

                                            Checkbox(
                                                colors = AppCheckBox.checkBoxColor(),
                                                checked = session.completedSets
                                                    .getOrNull(exerciseIndex)
                                                    ?.getOrNull(setIndex) ?: false,
                                                onCheckedChange = { isChecked ->
                                                    viewModel.updateCompletedSets(
                                                        exerciseIndex,
                                                        setIndex,
                                                        isChecked
                                                    )
                                                    if (isChecked) {
                                                        viewModel.startTimer()
                                                    }
                                                },
                                                modifier = Modifier.width(50.dp)
                                            )
                                        }
                                    }

                                    // 🔽 Add set button for THIS exercise
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        IconButton(
                                            onClick = {
                                                viewModel.addSetToExercise(exerciseIndex)
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = stringResource(R.string.add_set)
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
    }
}