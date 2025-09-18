package com.example.workoutapp.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Displays Workout page
 */
@Composable
fun WorkTemp(workoutName: String, modifier: Modifier = Modifier){
    Column (
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ){
        OutlinedButton(
            onClick = { /*TODO*/ },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor =  Color(0xFFE8DEF8)
            ),
            modifier = Modifier
                .padding(vertical = 40.dp)
                .size(50.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "content description"
            )
        }
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .border(width = 2.dp, color = Color.Black)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(getCurrentTimeString(), fontSize = 20.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = { /*TODO*/},
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor =  Color(0xFF495D92)
                            ),
                        ) {
                            Text("Finish")
                        }
                    }
                }
            }
            Text(
                workoutName,
                fontSize = 30.sp,
                modifier = Modifier.padding(vertical = 10.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                for (i in 1..3) {
                    Text("My exercise $i", fontSize = 15.sp)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)

                    ) {
                        Text(
                            "SETS",
                            fontSize = 10.sp,
                            modifier = Modifier
                                .padding(end = 80.dp)
                        )
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ){
                            Text("KG", fontSize = 10.sp)
                            Text("REPS", fontSize = 10.sp)
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Done set"
                            )
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (i in 1..3) {
                            Box(

                            ) {
                                Row(/////////////////
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp)

                                ) {
                                    Text(
                                        "$i",
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(end = 80.dp)
                                    )
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp)
                                    ){
                                        Text("KG", fontSize = 10.sp)
                                        Text("REPS", fontSize = 10.sp)
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Done set"
                                        )
                                    }
                                }////////////////////////

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Set", fontSize = 10.sp)
                                        Button(
                                            onClick = { /*TODO*/},
                                            shape = RoundedCornerShape(5.dp),
                                            modifier = Modifier.background(Color.White)
                                        ) {
                                            Text("Start")
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getCurrentTimeString(): String {
    val currentTime = LocalTime.now() // current time
    val formatter = DateTimeFormatter.ofPattern("HH:mm") // 24-hour format
    return currentTime.format(formatter)
}