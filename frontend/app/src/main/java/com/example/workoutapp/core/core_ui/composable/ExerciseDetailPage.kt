package com.example.workoutapp.core.core_ui.composable

import com.example.workoutapp.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import java.lang.System.exit

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
        Surface(
            color = cs.background,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxSize()
            ) {


                // --- Header ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Back button on the left
                    RoundBackButton(
                        onClick = onDismiss,
                        cs = cs,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                    )
                    PageHeading(
                        displayText = "Exercise"
                    )
                }
                Column(
                    modifier = Modifier.verticalScroll(state = rememberScrollState())
                ) {
                    // Title in center
                    Text(
                        text = exercise!!.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp,
                        lineHeight = 30.sp,
                        color = cs.onBackground,
                    )

                    // --- Details ---
                    ExerciseDetailTextField("Muscles", exercise.targetMuscles)
                    ExerciseDetailTextField("Secondary muscles", exercise.secondaryMuscles)
                    ExerciseDetailTextField("Bodyparts", exercise.bodyParts)
                    ExerciseDetailTextField("Equipment", exercise.equipments)

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Instructions:",
                        fontWeight = FontWeight.Bold,
                        color = cs.onBackground
                    )

                    exercise.instructions.forEach { instruction ->
                        Text(
                            text = instruction,
                            color = cs.onBackground,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}