package com.example.workoutapp.features.active_workout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.seconds

/**
 * Displays Workout page
 */
@Composable
fun ActiveWorkoutPage(workoutName: String, modifier: Modifier = Modifier, navController: NavController){
    var isAnyChecked by remember { mutableStateOf(false) }

    var ticks by remember { mutableIntStateOf(5) }
    LaunchedEffect(Unit) {
        while(true) {
            delay(1.seconds)
            if(isAnyChecked) {
                ticks--
                if(ticks == 0) {
                    isAnyChecked = false
                    ticks = 5
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (isAnyChecked) { // check if condition is true (show/hide bottombar)
                NavigationBar {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${ticks / 60}:${ticks % 60}",
                            fontSize = 70.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column (
            modifier = modifier
                .verticalScroll(rememberScrollState()),
        ){
            OutlinedButton(
                onClick = { navController.popBackStack() },
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor =  Color(0xFFE8DEF8)
                ),
                modifier = Modifier
                    .padding(innerPadding) //, top = 20.dp, bottom = 40.dp
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 2.dp, color = Color.Black)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(getCurrentTimeString(), fontSize = 20.sp)
                    Button(
                        onClick = { /*TODO*/},
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor =  Color(0xFF127067)
                        ),
                    ) {
                        Text("Finish", color = Color.White)
                    }
                }
                Text(
                    workoutName,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .padding(top = 10.dp),
                ) {
                    val x = 3
                    for (i in 1..x) {
                        Text("My exercise $i", fontSize = 15.sp) // TODO get exercise name from workout
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)

                        ) {
                            val y = 3
                            val h = 75
                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .height(h.dp * y)
                                    .fillMaxHeight()
                            ) {
                                Text(
                                    "SETS",
                                    fontSize = 10.sp,
                                    modifier = Modifier
                                )
                                for (i in 1..y) {
                                    Text(
                                        "$i\n",
                                        fontSize = 15.sp,
                                    )
                                }
                            }
                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .height(h.dp * y)
                                    .fillMaxHeight()
                            ) {
                                Text("KG", fontSize = 10.sp)
                                repeat(y) {
                                    // TODO get kg and reps from exercise
                                    val kg = remember { mutableStateOf("50") }
                                    TextField(
                                        value = kg.value,
                                        onValueChange = { kg.value = it },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = TextFieldDefaults.colors(
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            disabledIndicatorColor = Color.Transparent
                                        ),
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(50.dp)
                                    )
                                }
                            }
                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .height(h.dp * y)
                                    .fillMaxHeight()
                            ) {
                                Text("REPS", fontSize = 10.sp)
                                repeat (y) {
                                    val reps = remember { mutableStateOf("10") }
                                    TextField(
                                        value = reps.value,
                                        onValueChange = { reps.value = it },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = TextFieldDefaults.colors(
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            disabledIndicatorColor = Color.Transparent
                                        ),
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(50.dp)
                                    )
                                }
                            }
                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .height(h.dp * y)
                                    .fillMaxHeight()
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Done set"
                                )
                                repeat (y) {
                                    var checked by remember { mutableStateOf(false) }
                                    Checkbox(checked, {
                                        isAnyChecked = isAnyChecked || it
                                        checked = it
                                    })
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