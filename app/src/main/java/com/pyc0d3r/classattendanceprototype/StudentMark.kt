package com.pyc0d3r.classattendanceprototype

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pyc0d3r.classattendanceprototype.ui.theme.ClassAttendancePrototypeTheme

class StudentMark : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClassAttendancePrototypeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                }
            }
        }
    }
}

/**
 * Composable that displays a screen indicating successful attendance marking.
 *
 * @param modifier Modifier to apply to the composable.
 * @param navController NavController for navigation.
 */
@Composable
fun StudentMark(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display an image indicating successful attendance marking.
        Image(
            painter = painterResource(id = R.drawable.mark),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 30.dp, bottom = 10.dp, end = 30.dp)
                .size(400.dp)
                .fillMaxWidth()
        )

        // Display a text indicating successful attendance marking.
        Text(
            text = "Marked Present",
            modifier = Modifier.padding(start = 30.dp, bottom = 10.dp, end = 30.dp),
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Button to navigate back to the main screen.
        Button(
            onClick = {
                navController.navigate("main_screen")
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 55.dp, end = 55.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        ) {
            Text(text = "Logout", fontSize = 16.sp)
        }
    }
}


