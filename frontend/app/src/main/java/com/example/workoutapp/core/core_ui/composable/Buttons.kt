package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Default WorkoutApp button
 * @param buttonText Text displayed on button
 * @param cs ColorScheme of app (not required)
 * @param navController used to navigate to next screen with onClick button parameter
 * @param route path navigated when onClick is triggered.
 * @return Button
 */
@Composable
fun StandardButton(
    buttonText: String,
    cs: ColorScheme = MaterialTheme.colorScheme,
    navController: NavController,
    route: String,
    fillScreen: Boolean = false
) {
    return Button(
        onClick = { navController.navigate(route) },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = cs.tertiary
        ),
        modifier = if (fillScreen) Modifier.fillMaxWidth() else Modifier.wrapContentWidth()
    ) {
        Text(
            buttonText,
            color = cs.onTertiary
        )
    }
}