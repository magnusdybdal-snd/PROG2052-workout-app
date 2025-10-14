package com.example.workoutapp.core.core_ui.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Modifier for top-level columns in pages.
 * @param cs Color scheme of app
 * @return Standard top-level column modifier.
 */
@Composable
fun PageColumnModifier(
    cs: ColorScheme = MaterialTheme.colorScheme
): Modifier {
    return Modifier
        .fillMaxSize()
        .widthIn(max = 550.dp)
        .background(cs.background)
        .padding(horizontal = 20.dp)
}