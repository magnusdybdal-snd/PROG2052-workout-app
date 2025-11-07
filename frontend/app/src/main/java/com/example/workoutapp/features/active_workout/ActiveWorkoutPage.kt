package com.example.workoutapp.features.active_workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.R
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.core.core_ui.composable.RoundBackButton
import com.example.workoutapp.core.core_ui.composable.RoundedButton
import com.example.workoutapp.core.core_ui.composable.TimerTextField
import com.example.workoutapp.core.core_ui.composable.WorkoutTextField
import com.example.workoutapp.core.core_ui.composable.modifiers.BorderBoxModifier
import com.example.workoutapp.core.core_ui.composable.modifiers.TextFieldModifier
import com.example.workoutapp.core.core_ui.theme.AppCheckBox
import com.example.workoutapp.core.core_ui.theme.AppOutlinedTextField.outlinedFieldColors
import com.example.workoutapp.core.core_ui.theme.AppTextButton.textButtonColor
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

/**viewmodel
 * Displays Workout page
 */
@Composable
fun ActiveWorkoutPage(
    templateId: Int,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ActWorkViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val cs = MaterialTheme.colorScheme
    val activeSession by viewModel.activeSession.collectAsState()

    // If no active session, navigate back
    if (activeSession == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    val session = activeSession!!

    when {
        state.isLoading -> LoadingStateView()
        state.error != null -> ErrorStateView(state.error)
        else -> {

            // Timer countdown effect
            LaunchedEffect(session.isTimerRunning, session.timerSecondsRemaining) {
                if (session.isTimerRunning && session.timerSecondsRemaining > 0) {
                    delay(1.seconds)
                    viewModel.tickTimer()
                }
            }

//            val template = state.templates[templateId]
//            val completedSets = remember {
//                mutableStateOf(
//                    template.exercises.mapIndexed { _, exSet ->
//                        MutableList(exSet.sets.size) { false }
//                    }
//                )
//            }
//
//            var time by remember { mutableIntStateOf(3) }
//            var ticks by remember { mutableIntStateOf(time * 60) }
//            var isTimerRunning by remember { mutableStateOf(false) }
//            var restartKey by remember { mutableIntStateOf(0) }
//
//
//            LaunchedEffect(isTimerRunning, time, restartKey) {
//                if (isTimerRunning) {
//                    ticks = time * 60 // reset countdown
//                    while (ticks > 0) {
//                        delay(1.seconds)
//                        ticks--
//                    }
//                    isTimerRunning = false
//                }
//            }

            Scaffold (
                bottomBar = {
                    if (session.isTimerRunning) { // check if condition is true (show/hide bottombar)
                        NavigationBar{
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
                Column(
                    modifier = Modifier.verticalScroll(
                        state= rememberScrollState()
                    ),
                ) {
                   RoundBackButton(
                       navController = navController,
                       modifier = Modifier.padding(innerPadding)
                   )
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
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
                                onClick = { showDialog = true },
                            )

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
                        Text(
                            session.template.name,
                            fontSize = 30.sp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )

                        Box(
                            modifier = Modifier.width(120.dp)
                        ) {
                            TimerTextField(
                                time = session.timerMinutes,
                                onTimeChange = { viewModel.updateTimerMinutes(it) }
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .padding(top = 10.dp),
                        ) {
                            session.modifiedExercises.exercises.forEachIndexed { exerciseIndex, exSet ->
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)

                                ) {
                                    Text(
                                        exSet.name,
                                        fontSize = 15.sp
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp)

                                ) {
                                    val y = exSet.sets.size //icon
                                    val h = 75
                                    Column(
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = TextFieldModifier(height = h.dp * y)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.sets),
                                            fontSize = 10.sp,
                                            modifier = Modifier
                                        )
                                        for (i in 1..y) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .height(50.dp)
                                            ) {
                                                Text(
                                                    "$i",
                                                    fontSize = 15.sp
                                                )
                                            }
                                        }
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = TextFieldModifier(height = h.dp * y)
                                    ) {
                                        WorkoutTextField(
                                            label = stringResource(R.string.kg),
                                            exSet = exSet,
                                            type = "kg"
                                        )
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = TextFieldModifier(height = h.dp * y)
                                    ) {
                                        WorkoutTextField(
                                            label = stringResource(R.string.reps),
                                            exSet = exSet,
                                            type = "reps"
                                        )
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = TextFieldModifier(height = h.dp * y)

                                    ) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = stringResource(R.string.done_set),
                                            tint = cs.onBackground
                                        )

                                        exSet.sets.forEachIndexed { setIndex, _ ->
                                            Checkbox(
                                                colors = AppCheckBox.checkBoxColor(),
                                                checked = session.completedSets.getOrNull(exerciseIndex)
                                                    ?.getOrNull(setIndex) ?: false,
                                                onCheckedChange = { isChecked ->
                                                    viewModel.updateCompletedSets(
                                                        exerciseIndex,
                                                        setIndex,
                                                        isChecked
                                                    )

                                                    if(isChecked) {
                                                        viewModel.startTimer()
                                                    }
                                                }
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