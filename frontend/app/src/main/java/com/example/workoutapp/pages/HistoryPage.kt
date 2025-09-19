package com.example.workoutapp.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * Displays History page
 */
@Composable
fun HistoryPage(modifier: Modifier = Modifier) {
    Column( // Workout Header
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 500.dp)
            .padding(bottom = 80.dp) // padding to compensate for navbar - navigationBarsPadding()?
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
           .widthIn(max = 700.dp)
           .background(Color.White)
           .padding(bottom = 20.dp)
           .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,

//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (i in 1..3){ // loop over months
        Text( // Month
            modifier = Modifier.padding(start = 20.dp, top = 10.dp),
            text = "September", // Value hardcoded until backend Logic is available
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,

        )
        // Looping for more elements.
        for (i in 1..3) {
            Box( // vertical space between boxes
                modifier = Modifier.padding(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .border(width = 2.dp, color = Color.Black)
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.padding(start = 30.dp)
                    ) {
                        Text( // Workout name
                            text = "My Workout $i ",
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
                            Row(
                                modifier = Modifier.padding(start = 10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "",
                                )
                                Text(
                                    text = "1234 kg",
                                    modifier.padding(start = 6.dp)
                                )

                            }
                        }

                    } // end column 1 "workout text
                    Text(
                        modifier = Modifier.padding(end = 30.dp),
                        text = "29.29.2029",
                        textAlign = TextAlign.End
                    )
                }
            }

        }
    }
    }
}
}

