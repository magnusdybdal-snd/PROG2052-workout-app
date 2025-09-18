package com.example.workoutapp.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.workoutapp.Routes

/**
 * Displays Workout page
 */
@Composable
fun WorkoutPage(modifier: Modifier = Modifier, navController: NavController){
    Column (
        modifier = modifier
            .fillMaxSize()
            .widthIn(max = 550.dp)
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Workouts",
            fontSize = 50.sp,
            color = Color.Black,
        )
        Button(
            onClick = {/*TODO*/ },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 15.dp)
        ) {
            Text(
                text = "Start empty workout",
            )
        }
        Row (
            modifier = Modifier
                .align(Alignment.Start)
        ){
            Text(
                text = "My workout",
                fontSize = 30.sp,
            )
            IconButton (
                onClick = {/*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add workout"
                )
            }
            IconButton (
                onClick = {/*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search workout"
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp), // spacing between boxes
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (i in 1..4) {
                Box(
                    modifier = Modifier
                        .border(width = 2.dp, color = Color.Black)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("My workout $i", fontSize = 20.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = { navController.navigate(Routes.TEST) }, // TODO remove test
                                shape = RoundedCornerShape(5.dp),
                                modifier = Modifier.background(Color.White)
                            ) {
                                Text("Start")
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Extra"
                                )
                            }
                        }
                    }
                }
            }
        }
        Text(
            text = "Example workout",
            fontSize = 30.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(top = 40.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp), // spacing between boxes
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (i in 1..4) {
                Box(
                    modifier = Modifier
                        .border(width = 2.dp, color = Color.Black)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Example $i", fontSize = 20.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = { /*TODO*/ },
                                shape = RoundedCornerShape(5.dp),
                                modifier = Modifier.background(Color.White)
                            ) {
                                Text("Start")
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Extra"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}