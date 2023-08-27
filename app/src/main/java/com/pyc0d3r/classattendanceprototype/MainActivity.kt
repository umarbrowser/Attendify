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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.pyc0d3r.classattendanceprototype.ui.theme.ClassAttendancePrototypeTheme
import java.util.*

/**
 * The main entry point of the app. This activity is responsible for initializing the app's UI
 * and setting up the content using Jetpack Compose.
 *
 * @see ComponentActivity
 */
class MainActivity : ComponentActivity() {

    /**
     * Lifecycle method called when the activity is created. Sets up the Compose UI using the
     * `setContent` function and applies the app's theme.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the app's UI using Jetpack Compose
        setContent {
            // Apply the app's theme to the entire UI
            ClassAttendancePrototypeTheme {
                // Create a surface container with the app's background color
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // Call the Navigation composable to display the app's navigation flow
                    Navigation()
                }
            }
        }
    }
}


/**
 * Composable function responsible for setting up the app's navigation using Jetpack Compose Navigation.
 * It defines the navigation flow and associated composables for each destination.
 *
 * @see Composable
 */
@Composable
fun Navigation() {
    // Create a NavController to manage the app's navigation
    val navController = rememberNavController()

    // Define the navigation flow using NavHost with different composables for each destination
    NavHost(navController = navController, startDestination = "main_screen") {
        // Main Screen: This is the start destination where users can log in
        composable("main_screen") {
            LoginScreen(navController = navController)
        }

        // Student Home Screen: Displays student information and attendance marking option
        composable(
            route = "student_home_screen/{value1}",
            arguments = listOf(
                navArgument("value1") { type = NavType.StringType },
            )
        ) {
            StudentHomeL(navController = navController)
        }

        // Student Mark Screen: Allows students to mark their attendance
        composable("student_mark_screen") {
            StudentMark(navController = navController)
        }

        // Admin Student List Screen: Displays a list of students for the admin
        composable("admin_student_list_screen") {
            StudentListScreen(navController = navController)
        }
    }
}

/**
 * Composable function responsible for displaying the login screen of the Class Attendance Management System.
 * Users can enter their credentials to log in as a student or an admin.
 *
 * @param modifier The modifier for custom styling of this composable.
 * @param navController The NavController used for navigation between screens.
 *
 * @see Composable
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    // States to hold the entered username and password
    val username = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    // State to toggle password visibility
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App title and logo
        Text(text = "Attendify",
            modifier = Modifier.padding(start = 30.dp, bottom = 10.dp, end = 30.dp),

            style = MaterialTheme.typography.h4, textAlign = TextAlign.Center)
        // App subtitle
        Text(text = "Class Attendance Management System",
            modifier = Modifier.padding(start = 30.dp, end = 30.dp),

            style = MaterialTheme.typography.body2, textAlign = TextAlign.Center)
        // Logo image
        Image(painter = painterResource(id = R.drawable.well_logo), contentDescription = null,
            modifier = Modifier
                .padding(start = 30.dp, bottom = 10.dp, end = 30.dp)
                .size(200.dp)
                .fillMaxWidth())
        // Username input field
        OutlinedTextField(
            singleLine = true,
            modifier = modifier
                .padding(bottom = 16.dp),
            value = username.value,
            onValueChange = { username.value = it.replace(" ", "") },
            placeholder = { Text("Username/Admin Login") },
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Username/Admin Login") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        // Password input field
        OutlinedTextField(
            modifier = modifier,
            value = password.value,
            onValueChange = { password.value = it.filter { char -> char.isDigit() }},
            placeholder = { Text(text = "password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    val icon: ImageVector =
                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    Icon(imageVector = icon, contentDescription = "Toggle Password Visibility")
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
        // Login button
        Button(
            onClick = {
              // addDataToFirebase("Said Uwais Abdulhamid", "UG17/SCCS/1108", true, context)
                // Check credentials and navigate based on role
                if (username.value.lowercase(Locale.ROOT) == "ug18/sccs/1134" && password.value == "1134") {
                    val value1 = "Ahmad Saidu"
                    navController.navigate("student_home_screen/$value1")
                } else if (username.value.lowercase(Locale.getDefault()) == "admin" && password.value == "1960") {
                    navController.navigate("admin_student_list_screen")
                } else if (username.value.lowercase(Locale.getDefault()) == "ug18/sccs/1015" && password.value == "1015") {
                    val value1 = "Umar Haruna Abdullahi"
                    navController.navigate("student_home_screen/$value1")
                }else if (username.value.lowercase(Locale.getDefault()) == "ug18/sccs/1144" && password.value == "1144") {
                    val value1 = "Amina Umar"
                    navController.navigate("student_home_screen/$value1")
                }else if (username.value.lowercase(Locale.getDefault()) == "ug16/sccs/1047" && password.value == "1047") {
                    val value1 = "Abdulrahman Abubakar"
                    navController.navigate("student_home_screen/$value1")
                }else if (username.value.lowercase(Locale.getDefault()) == "ug18/sccs/1122" && password.value == "1122") {
                    val value1 = "Aisha Mahmoud Arabi"
                    navController.navigate("student_home_screen/$value1")
                }else if (username.value.lowercase(Locale.getDefault()) == "ug18/sccs/1160" && password.value == "1160") {
                    val value1 = "Philip James"
                    navController.navigate("student_home_screen/$value1")
                }else if (username.value.lowercase(Locale.getDefault()) == "ug18/sccs/2007" && password.value == "2007") {
                    val value1 = "Adamu Umar Adamu"
                    navController.navigate("student_home_screen/$value1")
                }
                else if (username.value.lowercase(Locale.getDefault()) == "ug18/sccs/1116" && password.value == "1116") {
                    val value1 = "Abdurrahman Baba Dahiru"
                    navController.navigate("student_home_screen/$value1")
                }else if (username.value.lowercase(Locale.getDefault()) == "ug18/sccs/1142" && password.value == "1142") {
                    val value1 = "Ahmad Khalid Skole"
                    navController.navigate("student_home_screen/$value1")
                }
                else if (username.value.lowercase(Locale.getDefault()) == "ug18/sccs/1126" && password.value == "1126") {
                    val value1 = "Muhammad Abubakar"
                    navController.navigate("student_home_screen/$value1")
                }
                else if (username.value.lowercase(Locale.getDefault()) == "ug17/sccs/1108" && password.value == "1108") {
                    val value1 = "Said Uwais Abdulhamid"
                    navController.navigate("student_home_screen/$value1")
                }
                else {
                    Toast.makeText(context, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 55.dp, end = 55.dp),
            colors =
            ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        ) {
            Text(text = "Login", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Gombe State University | Computer Science Department | Software Engineering (Cosc401) project | Group 3",
            modifier = Modifier.padding(start = 30.dp, end = 30.dp),

            style = MaterialTheme.typography.body2, textAlign = TextAlign.Center)
    }
}
