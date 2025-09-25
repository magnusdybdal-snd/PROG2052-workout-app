package com.example.workoutapp.features.excercices

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.workoutapp.R

/**
 * Displays Exercises page
 */
@Composable
fun ExercisesPage(modifier: Modifier = Modifier, navController: NavController){
    Column ( // Workout Header
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 700.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(top = 80.dp)
                .padding(bottom = 20.dp),
            text = "Exercises",
            fontSize = 50.sp,
            color = Color.Black,
            )
        Column ( // Workout-boxes
            modifier = modifier
                .fillMaxSize()
                .widthIn(max = 550.dp)
                .padding(horizontal = 20.dp)
                .padding(bottom = 100.dp) // Padding so nothing hides under bottombar
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (i in 1..29) { // TODO: update range when backend is connected
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(80.dp, 80.dp)
                        .padding(vertical = 6.dp)
                        .border(width = 2.dp, color = Color.Black),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image( // Workout image
                        painter = painterResource(id = R.drawable.exampleworkoutimage), // Example image, maybe default image.
                        contentDescription = null,
                        contentScale = ContentScale.Inside
                    )
                    Text( // Workout name
                        modifier = Modifier.padding(6.dp),
                        text = "Workout $i",
                        fontSize = 20.sp

                    )
                }
            }

        }
    }
}