package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
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
        .widthIn(max = 700.dp)
        .background(cs.background)
}