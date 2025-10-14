package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
 * @param fillMaxWidth fillMaxWidth() will be enabled
 * @return Button
 */
@Composable
fun StandardButton(
    buttonText: String,
    cs: ColorScheme = MaterialTheme.colorScheme,
    navController: NavController,
    route: String,
    fillMaxWidth: Boolean = false
) {
    return Button(
        onClick = { navController.navigate(route) },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = cs.tertiary
        ),
        modifier = if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier.wrapContentWidth()
    ) {
        Text(
            buttonText,
            color = cs.onTertiary
        )
    }
}

@Composable
fun RoundBackButton(
    navController: NavController,
    cs: ColorScheme = MaterialTheme.colorScheme,
    modifier: Modifier = Modifier // Not used but needed in ActiveWorkoutPage to suppress Scaffold error
) {
    OutlinedButton(
        onClick = { navController.popBackStack() },
        shape = CircleShape,
        contentPadding = PaddingValues(all = 0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = cs.tertiary,
            contentColor = cs.onTertiary
        ),
        modifier = Modifier
            .padding(
                top = 20.dp,
                bottom = 40.dp,
                start = 20.dp
            )
            .size(50.dp)
    ) {
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "go back",
            tint = cs.onTertiary
        )
    }
}
@Composable
fun RoundedButton(
    buttonText: String,
    onClick: () -> Unit,
    cs: ColorScheme = MaterialTheme.colorScheme
){
    return Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = cs.tertiary
        ),
    ) {
        Text(
            text = buttonText,
            color = cs.onTertiary
        )
    }
}