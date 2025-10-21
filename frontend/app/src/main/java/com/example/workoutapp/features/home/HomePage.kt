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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.R
import com.example.workoutapp.core.core_navigation.Routes
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.core.core_ui.composable.PageHeading
import com.example.workoutapp.core.core_ui.composable.StandardButton
import com.example.workoutapp.core.core_ui.composable.TemplateCategoryHeading
import com.example.workoutapp.core.core_ui.composable.TemplateDisplayContent
import com.example.workoutapp.core.core_ui.composable.modifiers.PageColumnModifier
import com.example.workoutapp.domain.models.WorkoutTemplate

/**
 * Displays Workout page
 * @param modifier
 * @param navController
 * @param viewModel
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
                    displayText = stringResource(R.string.workouts)
                )
                StandardButton(
                    buttonText = "Start empty workout",
                    navController = navController,
                    route = Routes.NEWTEMP, // TODO this i sa temporary route!! -> replace when proper path is set.
                    fillMaxWidth = true
                )
                Row (
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                   TemplateCategoryHeading(
                       displayText = "My " + stringResource(R.string.workouts)
                   )
                    IconButton (
                        onClick = {navController.navigate(Routes.NEWTEMP) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_workout)
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
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    TemplateCategoryHeading(
                        displayText = "Example " +
                                stringResource(R.string.workouts).lowercase()
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