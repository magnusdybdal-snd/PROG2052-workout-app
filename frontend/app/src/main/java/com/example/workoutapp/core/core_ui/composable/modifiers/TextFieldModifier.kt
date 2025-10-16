package com.example.workoutapp.core.core_ui.composable.modifiers

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldModifier(
    height: Dp
): Modifier {
    return  Modifier
        .height(height)
        .fillMaxHeight()
}