package com.example.workoutapp.core.core_ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.workoutapp.domain.models.Exercise

@Composable
fun ExerciseDetailPage(
    showOverlay: Boolean,
    onDismiss: () -> Unit,
    cs: ColorScheme = MaterialTheme.colorScheme,
    exercise: Exercise?
) {
    AnimatedVisibility(
        visible = showOverlay,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .zIndex(1f)
        ) {
            // Scrim
            Surface(
                color = cs.scrim.copy(alpha = 0.45f),
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDismiss() }
            ) {}

            // Card
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 12.dp,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text( // exercise name
                        text = exercise!!.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                    Spacer(Modifier.height(6.dp))

                    ExerciseDetailTextField("Muscles", exercise.targetMuscles)
                    ExerciseDetailTextField("Secondary muscles", exercise.secondaryMuscles)
                    ExerciseDetailTextField("Bodyparts", exercise.bodyParts)
                    ExerciseDetailTextField("Equipment", exercise.equipments)
                    Column {
                        Text( // EQUIPMENT
                            text = "Instructions: ",
                            fontWeight = FontWeight.Bold
                        )
                        exercise.instructions.forEach { instruction ->
                            Row {
                                Text(instruction)

                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = onDismiss) {    // <-- use callback
                            Text(stringResource(android.R.string.cancel))
                        }
                    }
                }
            }
        }
    }
}
