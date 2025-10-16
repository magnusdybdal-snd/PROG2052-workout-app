package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorStateView(message: String?) {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = cs.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: $message",
            style = MaterialTheme.typography.bodyLarge,
            color = cs.error,
            textAlign = TextAlign.Center
        )
    }
}
