package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BorderBox(): Modifier {
    val cs = MaterialTheme.colorScheme
    return Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp)   // Padding between boxes
        .heightIn(                  // Force to 60.dp height
            min= 60.dp,
            max = 60.dp
        )
        .border(
            width = 2.dp,
            color = cs.onBackground
        )
        .padding(horizontal = 10.dp) // Padding between box content and border
}