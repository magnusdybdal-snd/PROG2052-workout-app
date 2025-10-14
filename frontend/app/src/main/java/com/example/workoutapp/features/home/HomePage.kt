package com.example.workoutapp.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.core.core_navigation.Routes
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.core.core_ui.composable.PageHeading
import com.example.workoutapp.core.core_ui.composable.StandardButton
import com.example.workoutapp.core.core_ui.composable.TemplateCategoryHeading
import com.example.workoutapp.core.core_ui.composable.TemplateDisplayContent
import com.example.workoutapp.core.core_ui.modifiers.PageColumnModifier
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

    when {
        state.isLoading -> LoadingStateView()
        state.error != null -> ErrorStateView(state.error)
        else -> {
            Column (
                modifier = PageColumnModifier()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PageHeading(
                    displayText = "Workouts"
                )
                StandardButton(
                    buttonText = "Start empty workout",
                    navController = navController,
                    route = Routes.NEWTEMP, // TODO this i sa temporary route!! -> replace when proper path is set.
                    fillScreen = true
                )
                Row (
                    modifier = Modifier.align(Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically
                ){
                   TemplateCategoryHeading(
                       displayText = "My Workouts"
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
                Column {
                    state.workoutTemplates.forEachIndexed { index, workoutTemplate: WorkoutTemplate ->
                        TemplateDisplayContent(
                            templateName = workoutTemplate.name,
                            navController = navController,
                            route = "worktemp/$index"
                        )
                    }
                }
                Column(
                    modifier.padding(top = 20.dp)
                ) {
                    TemplateCategoryHeading(
                        displayText = "Example workouts"
                    )
                    for (i in 1..4) {
                        TemplateDisplayContent(
                            templateName = "Example $i",
                            navController = navController,
                            route = "worktemp/${i - 1}"
                        )
                    }
                }
            }
        }
    }
}