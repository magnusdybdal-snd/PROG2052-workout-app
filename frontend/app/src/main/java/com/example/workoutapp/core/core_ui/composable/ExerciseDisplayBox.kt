package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.R
import com.example.workoutapp.domain.models.Exercise

@Composable
fun ExerciseDisplayBox(exercise: Exercise){
    Row(
        modifier = BorderBox(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image( // Picture of exercise
            painter = painterResource(id = R.drawable.exampleworkoutimage),
            contentDescription = "Exercise image",
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .padding(6.dp)
        )
        Text( // Exercise name
            modifier = Modifier.padding(6.dp),
            text = exercise.name,
            fontSize = 20.sp
        )
    }
}