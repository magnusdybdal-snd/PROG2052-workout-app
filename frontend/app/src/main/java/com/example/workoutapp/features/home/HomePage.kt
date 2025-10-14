package com.example.workoutapp.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.core.core_navigation.Routes
import com.example.workoutapp.core.core_ui.composable.BorderBox
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.domain.models.WorkoutTemplate

/**
 * Displays Workout page
 */
@Composable
fun HomePage(modifier: Modifier = Modifier,
             navController: NavController,
             viewModel: WorkoutTemplatesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val cs = MaterialTheme.colorScheme

    when {
        state.isLoading -> LoadingStateView()
        state.error != null -> ErrorStateView(state.error)
        else -> {
            Column (
                modifier = modifier
                    .fillMaxSize()
                    .widthIn(max = 550.dp)
                    .background(cs.background)
                    .padding(top = 40.dp)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Workouts",
                    fontSize = 50.sp,
                    color = cs.onBackground,
                )
                Button(
                    onClick = {/*TODO*/ },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = cs.tertiary
                    ),
                    modifier = Modifier
                        .background(cs.background)
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 15.dp)
                ) {
                    Text(
                        text = "Start empty workout",
                        color = cs.onTertiary
                    )
                }
                Row (
                    modifier = Modifier.align(Alignment.Start)
                ){
                    Text(
                        text = "My workout",
                        fontSize = 30.sp,
                    )
                    IconButton (
                        onClick = {navController.navigate(Routes.NEWTEMP) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add workout"
                        )
                    }
                    IconButton (
                        onClick = {/*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search workout"
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    state.workoutTemplates.forEachIndexed { index, workoutTemplate: WorkoutTemplate ->
                            Row(
                                modifier = BorderBox(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = workoutTemplate.name,
                                    fontSize = 20.sp
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Button(
                                        onClick = { navController.navigate("worktemp/$index")},
                                        shape = RoundedCornerShape(5.dp),
                                        modifier = Modifier.background(cs.background),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = cs.tertiary
                                        ),
                                    ) {
                                        Text("Start", color = cs.onTertiary)
                                    }
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "Extra"
                                        )
                                    }
                                }
                            }
                    }
                }
                Text(
                    text = "Example workout",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    for (i in 1..4) {
                            Row(
                                modifier = BorderBox(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Example $i", fontSize = 20.sp) // TODO get example name from database
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Button(
                                        onClick = { navController.navigate("worktemp/${i-1}")},
                                        shape = RoundedCornerShape(5.dp),
                                        modifier = Modifier.background(cs.background),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = cs.tertiary
                                        ),
                                    ) {
                                        Text("Start", color = cs.onTertiary)
                                    }
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "Extra"
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