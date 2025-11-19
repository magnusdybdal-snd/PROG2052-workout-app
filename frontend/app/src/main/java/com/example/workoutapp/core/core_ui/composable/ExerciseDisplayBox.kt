package com.example.workoutapp.core.core_ui.composable

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.example.workoutapp.R
import com.example.workoutapp.core.core_ui.composable.modifiers.BorderBoxModifier
import com.example.workoutapp.domain.models.Exercise

/**
 * Displays exercise and some of its data
 * @param exercise single instance of Exercise
 */
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ExerciseDisplayBox(
    exercise: Exercise
) {
    Row(
        modifier = BorderBoxModifier(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val context = LocalContext.current
        val imageLoader = context.imageLoader

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(exercise.gifUrl)
                .crossfade(true)
                .build(),
            imageLoader = imageLoader,
            contentDescription = "Exercise demonstration",
            contentScale = ContentScale.Inside,
            modifier = Modifier.padding(vertical = 8.dp),
            error = painterResource(R.drawable.exampleworkoutimage), // If load fails
        )
        Text(
            modifier = Modifier.padding(6.dp),
            text = exercise.name,
            fontSize = 20.sp
        )
    }
}