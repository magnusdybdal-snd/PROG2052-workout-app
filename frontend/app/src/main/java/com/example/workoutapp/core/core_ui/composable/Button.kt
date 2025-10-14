package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.background
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
 * @param index of item
 * @return Button
 */
@Composable
fun RoundedButton(
    buttonText: String,
    cs: ColorScheme = MaterialTheme.colorScheme,
    navController : NavController,
    route : String

){
    return Button(
        onClick = { navController.navigate(route)},
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier.background(cs.background),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = cs.tertiary
        ),
    ) {
        Text(
            buttonText,
            color = cs.onTertiary
        )
    }

}