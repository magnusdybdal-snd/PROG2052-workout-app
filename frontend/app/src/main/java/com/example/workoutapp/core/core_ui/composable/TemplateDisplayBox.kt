package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.core.core_ui.composable.modifiers.BorderBoxModifier
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.features.home.WorkoutTemplatesViewModel

/**
 * Displays  content of template on HomePage
 * @param templateName Name of template
 * @param navController NavigationController
 * @param route route triggered by onClick function.
 */
@Composable
fun TemplateDisplayContent (
    template: WorkoutTemplate,
    navController: NavController,
    route: String,
    viewModel: WorkoutTemplatesViewModel = hiltViewModel()
) {
    Row(
        modifier = BorderBoxModifier(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = template.name,
            fontSize = 20.sp
        )
        Row {
            StandardButton(
                buttonText = "Start",
                navController = navController,
                route = route
            )
            IconButton(onClick = { viewModel.deleteTemplate(template) }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Extra"
                )
            }
        }
    }
}