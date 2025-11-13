package com.example.workoutapp.features.active_workout

import android.util.Log.i
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
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.workoutapp.core.core_ui.composable.modifiers.TextFieldModifier
import com.example.workoutapp.core.core_ui.theme.AppCheckBox
import com.example.workoutapp.core.core_ui.theme.AppOutlinedTextField.outlinedFieldColors
import com.example.workoutapp.core.core_ui.theme.AppTextButton.textButtonColor
import com.example.workoutapp.domain.models.Session
import com.example.workoutapp.domain.models.SessionExercise
import com.example.workoutapp.domain.models.Set
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

/**
 * Displays Active Workout page
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
    val focusManager = LocalFocusManager.current

    when {
        state.isLoading -> LoadingStateView()
        state.error != null -> ErrorStateView(state.error)
        else -> {
            val template = state.templates[templateId]
            val completedSets = remember {
                mutableStateOf(
                    template.exercises.mapIndexed { _, exSet ->
                        MutableList(exSet.sets.size) { false }
                    }
                )
            }

            var time by remember { mutableIntStateOf(3) }
            var ticks by remember { mutableIntStateOf(time * 60) }
            var isTimerRunning by remember { mutableStateOf(false) }
            var restartKey by remember { mutableIntStateOf(0) }

            LaunchedEffect(isTimerRunning, time, restartKey) {
                if (isTimerRunning) {
                    ticks = time * 60 // reset countdown
                    while (ticks > 0) {
                        delay(1.seconds)
                        ticks--
                    }
                    isTimerRunning = false
                }
            }

            Scaffold(
                bottomBar = {
                    if (isTimerRunning) {
                        NavigationBar {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = cs.background),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${ticks / 60}:${String.format("%02d", ticks % 60)}",
                                    fontSize = 70.sp,
                                    textAlign = TextAlign.Center,
                                    color = cs.onBackground
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                val bottomBarHeight = if (isTimerRunning) 120.dp else 0.dp

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
                        Row(
                            modifier = BorderBoxModifier(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(viewModel.getCurrentTimeString(), fontSize = 20.sp)
                            var showDialog by remember { mutableStateOf(false) }
                            var notes by remember { mutableStateOf("") }

                            RoundedButton(
                                buttonText = stringResource(R.string.finish),
                                onClick = {
                                    focusManager.clearFocus() // clear focus to ensure that active textfield is stored.
                                    showDialog = true
                                },
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
                                            onValueChange = { notes = it },
                                            label = { Text(text = stringResource(R.string.workout_notes)) },
                                        )
                                    },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                focusManager.clearFocus() // Clears on more time to be safe.
                                                val finishedWorkout = Session(
                                                    sessionId = UUID.randomUUID().toString(),
                                                    name = template.name,
                                                    exercises = template.exercises.mapIndexedNotNull { exerciseIndex, exSet ->
                                                        val completedSetsForExercise =
                                                            exSet.sets.filterIndexed { setIndex, _ ->
                                                                completedSets.value[exerciseIndex][setIndex]
                                                            }

                                                        if (completedSetsForExercise.isNotEmpty()) {
                                                            SessionExercise(
                                                                exerciseId = exSet.exerciseId,
                                                                name = exSet.name,
                                                                sets = completedSetsForExercise.map { set ->
                                                                    Set(
                                                                        rep = set.rep,
                                                                        kg = set.kg,
                                                                        typeSet = set.typeSet
                                                                    )
                                                                }
                                                            )
                                                        } else {
                                                            null
                                                        }
                                                    },
                                                    duration = java.time.Duration.ofHours(1)
                                                        .plusMinutes(15)
                                                        .plusSeconds(45), // TODO THIS IS MOCK DATA
                                                    date = LocalDate.now(),
                                                    note = notes
                                                )

                                                viewModel.postWorkout(finishedWorkout)
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
                            state.templates[templateId].name,
                            fontSize = 30.sp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )

                        Box(
                            modifier = Modifier.width(120.dp)
                        ) {
                            TimerTextField(
                                time = time,
                                onTimeChange = { time = it }
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.padding(top = 10.dp),
                        ) {
                            state.templates[templateId].exercises.forEachIndexed { exerciseIndex, exSet ->
                                // Exercise name
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

                                        // Checkbox
                                        Checkbox(
                                            colors = AppCheckBox.checkBoxColor(),
                                            checked = completedSets.value[exerciseIndex][setIndex],
                                            onCheckedChange = { isChecked ->
                                                completedSets.value = completedSets.value.toMutableList().apply {
                                                    this[exerciseIndex] = this[exerciseIndex].toMutableList().apply {
                                                        this[setIndex] = isChecked
                                                    }
                                                }

                                                if (isChecked) {
                                                    isTimerRunning = true
                                                    restartKey++
                                                }
                                            },
                                            modifier = Modifier.width(50.dp)
                                        )
                                    }

                                }
                                // Add set button (OUTSIDE the set loop, INSIDE the exercise loop)
                                IconButton(
                                    onClick = {
                                        // Update completedSets to include the new set
                                        completedSets.value = completedSets.value.toMutableList().apply {
                                            this[exerciseIndex] = this[exerciseIndex].toMutableList().apply {
                                                add(false) // Add tracking for the new set
                                            }
                                        }
                                        // Add the actual set
                                        exSet.sets.add(
                                            Set(
                                                rep = 0,
                                                kg = 0.0,
                                                typeSet = 0,
                                            )
                                        )
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