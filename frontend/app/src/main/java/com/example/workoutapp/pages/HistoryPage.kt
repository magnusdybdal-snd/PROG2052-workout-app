package com.example.workoutapp.pages

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.R

/**
 * Displays History page
 */
@Composable
fun HistoryPage(modifier: Modifier = Modifier) {
    Column( // Workout Header
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 500.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(top = 80.dp)
                .padding(bottom = 20.dp),
            text = "History",
            fontSize = 50.sp,
          //  fontWeight = FontWeight.SemiBold,
            color = Color.Black,
        )

    Column( // Boxes
       modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 500.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text( // Month
            modifier = Modifier.padding(start = 20.dp),
            text = "September", // Value hardcoded until backend Logic is available
            fontSize = 20.sp,
            //fontWeight = FontWeight.SemiBold,

        )
        Row(
            modifier = Modifier
                .heightIn(80.dp, 80.dp)
                .padding(vertical = 6.dp)
                .border(width = 2.dp, color = Color.Black),
            verticalAlignment = Alignment.CenterVertically
        ) {


                Column(
                    modifier = Modifier.padding(start = 30.dp)
                ){
                    Text( // Workout name
                        text = "My Workout x ",
                        fontSize = 20.sp
                    )
                    Row {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "",
                        )
                        Text(
                            text = "1:29:29",
                            modifier.padding(start = 6.dp)
                        )

                    }

                } // end column 1 "woukout text
                Column ( // Date
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ){
                    Text(
                       modifier = Modifier.padding(end = 40.dp),
                        text = "29.29.2929",
                        )
                }

        }
    }
}
}
