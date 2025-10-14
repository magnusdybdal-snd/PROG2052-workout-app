package com.example.workoutapp.features.active_workout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import java.time.ZoneId
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.core.core_ui.theme.AppCheckBox
import com.example.workoutapp.core.core_ui.theme.AppTextField
import com.example.workoutapp.domain.models.Session
import com.example.workoutapp.domain.models.SessionExercise
import com.example.workoutapp.domain.models.Set
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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


    when {
        state.isLoading -> LoadingStateView()
        state.error != null -> ErrorStateView(state.error)
        else -> {
            var isAnyChecked by remember { mutableStateOf(false) }

            var ticks by remember { mutableIntStateOf(60 * 3) }
            LaunchedEffect(Unit) {
                while (true) {
                    delay(1.seconds)
                    if (isAnyChecked) {
                        ticks--
                        if (ticks == 0) {
                            isAnyChecked = false
                            ticks = 60 * 3
                        }
                    }
                }
            }

            Scaffold(
                bottomBar = {
                    if (isAnyChecked) { // check if condition is true (show/hide bottombar)
                        NavigationBar {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${ticks / 60}:${ticks % 60}",
                                    fontSize = 70.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = modifier
                        .verticalScroll(rememberScrollState()), //sets
                ) {
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = cs.tertiary,
                            contentColor = cs.onTertiary
                        ),
                        modifier = Modifier
                            .padding(innerPadding) //, top = 20.dp, bottom = 40.dp
                            .size(50.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "content description"
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(width = 2.dp, color = cs.onBackground)
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(getCurrentTimeString(), fontSize = 20.sp)
                            var showDialog by remember { mutableStateOf(false) }
                            var notes by remember { mutableStateOf("") }
                            Button(
                                onClick = {
                                    showDialog = true
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = cs.tertiary
                                ),
                            ) {
                                Text("Finish", color = cs.onTertiary)
                            }

                            if (showDialog) {
                                AlertDialog(
                                    onDismissRequest = { showDialog = false },
                                    title = { Text("Add a note before finishing?") },
                                    text = {
                                        OutlinedTextField(
                                            value = notes,
                                            onValueChange = { notes = it },
                                            label = { Text("Workout notes") }
                                        )
                                    },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                val template = state.templates[templateId]
                                                val finishedWorkout = Session(
                                                    sessionId = "sess_003",
                                                    name = template.name,
                                                    exercises = template.exercises.map { exSet ->
                                                        SessionExercise(
                                                            exerciseId = exSet.exercise.exerciseId,
                                                            sets = exSet.sets.map { set ->
                                                                Set(
                                                                    rep = set.rep,
                                                                    kg = set.kg,
                                                                    typeSet = set.typeSet
                                                                )
                                                            }
                                                        )
                                                    },
                                                    duration = "00:30:00",
                                                    date = LocalDate.now().toString(),
                                                    note = notes
                                                )

                                                viewModel.postWorkout(finishedWorkout)
                                                showDialog = false
                                                navController.popBackStack()
                                            },
                                            //colors = ButtonColors
                                        ) {
                                            Text("Finish Workout")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(onClick = { showDialog = false }) {
                                            Text("Cancel")
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

                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .padding(top = 10.dp),
                        ) {
                            state.templates[templateId].exercises.forEach { exSet ->
                                Text(
                                    exSet.exercise.name,
                                    fontSize = 15.sp
                                )
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp)

                                ) {
                                    val y = exSet.sets.size
                                    val h = 75
                                    Column(
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .height(h.dp * y)
                                            .fillMaxHeight()
                                    ) {
                                        Text(
                                            "SETS",
                                            fontSize = 10.sp,
                                            modifier = Modifier
                                        )
                                        for (i in 1..y) {
                                            Text(
                                                "$i\n",
                                                fontSize = 15.sp,
                                            )
                                        }
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .height(h.dp * y)
                                            .fillMaxHeight()
                                    ) {
                                        Text("KG", fontSize = 10.sp)
                                        exSet.sets.forEach { set ->
                                            //val kg = remember { mutableStateOf() }
                                            TextField(
                                                value = set.kg.toString(),
                                                onValueChange = { set.kg = it.toIntOrNull() ?: 0 },
                                                shape = RoundedCornerShape(12.dp),
                                                colors = AppTextField.fieldColors()
                                                ,
                                                modifier = Modifier
                                                    .width(100.dp)
                                                    .height(50.dp)
                                            )
                                        }
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .height(h.dp * y)
                                            .fillMaxHeight()
                                    ) {
                                        Text("REPS", fontSize = 10.sp)
                                        exSet.sets.forEach { set ->
                                            TextField(
                                                value = set.rep.toString(),
                                                onValueChange = { set.rep = it.toIntOrNull() ?: 0 },
                                                shape = RoundedCornerShape(12.dp),
                                                colors = AppTextField.fieldColors(),
                                                modifier = Modifier
                                                    .width(100.dp)
                                                    .height(50.dp)
                                            )
                                        }
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .height(h.dp * y)
                                            .fillMaxHeight()
                                    ) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Done set",
                                            tint = cs.onBackground
                                        )
                                        repeat(y) {
                                            var checked by remember { mutableStateOf(false) }
                                            Checkbox(
                                                checked, {
                                                isAnyChecked = isAnyChecked || it
                                                checked = it
                                            },
                                                colors = AppCheckBox.checkBoxColor()
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

fun getCurrentTimeString(): String {
    val currentTime = LocalTime.now(ZoneId.systemDefault()) // current time
    val formatter = DateTimeFormatter.ofPattern("HH:mm") // 24-hour format
    return currentTime.format(formatter)
}