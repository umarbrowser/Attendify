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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.pyc0d3r.classattendanceprototype.ui.theme.ClassAttendancePrototypeTheme

class StudentHome : ComponentActivity() {
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
 * Adds student data to Firebase Firestore.
 *
 * @param studentName The name of the student.
 * @param studentNo The student number.
 * @param studentAtt The attendance status of the student.
 * @param context The context used to display toast messages.
 */
fun addDataToFirebase(
    studentName: String,
    studentNo: String,
    studentAtt: Boolean,
    context: Context
) {
    // Creating an instance of Firebase Firestore.
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Creating a collection reference for the "Courses" collection in Firebase Firestore.
    val dbCourses: CollectionReference = db.collection("Courses")

    // Creating a Students object to hold the student data.
    val student = Students(studentName = studentName, studentNo = studentNo, studentAttend = studentAtt)

    // Adding the student data to Firebase Firestore using the "add" method.
    dbCourses.add(student).addOnSuccessListener {
        // Displaying a success toast message upon successful data addition.
        Toast.makeText(
            context,
            "Your Course has been added to Firebase Firestore",
            Toast.LENGTH_SHORT
        ).show()
    }.addOnFailureListener { e ->
        // Displaying an error toast message when data addition fails.
        Toast.makeText(context, "Fail to add course \n$e", Toast.LENGTH_SHORT).show()
    }
}


/**
 * Composable for the Student Home Screen.
 *
 * @param modifier Modifier for styling and layout adjustments.
 * @param navController NavController for navigation.
 */
@Composable
fun StudentHomeL(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    // Get the value passed from the previous screen via navigation arguments.
    val value1 = navController.currentBackStackEntry?.arguments?.getString("value1")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Get the current context for displaying toast messages.
        val context = LocalContext.current

        // Display the student's name.
        Text(
            text = "Welcome: $value1",
            modifier = Modifier.padding(start = 30.dp, bottom = 10.dp, end = 30.dp),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))
        Divider()

        // Card with course details and attendance button.
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                // Display course title.
                Text(
                    text = "Course Title: Software Engineering",
                    modifier = Modifier.padding(start = 30.dp, bottom = 10.dp, end = 30.dp),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Display course code.
                Text(
                    text = "Course Code: Cosc 401",
                    modifier = Modifier.padding(start = 30.dp, bottom = 10.dp, end = 30.dp),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Display lecturer's name.
                Text(
                    text = "Lecturer: Assoc Prof. Bala Modi",
                    modifier = Modifier.padding(start = 30.dp, bottom = 10.dp, end = 30.dp),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Button to mark attendance.
                Button(
                    onClick = {
                        if (value1 != null) {
                            // Call the updateStudentAttendance function with the student's name and context.
                            updateStudentAttendance(
                                studentName = value1,
                                context = context,
                                navController = navController
                            )

                        }
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 55.dp, end = 55.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Text(text = "Mark Present", fontSize = 16.sp)
                }
            }
        }
    }
}



fun updateStudentAttendance(studentName: String, context: Context, navController: NavController) {

    val db = FirebaseFirestore.getInstance()
    val studentsCollection = db.collection("Courses")

    studentsCollection.whereEqualTo("studentName", studentName).get()
        .addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val student = document.toObject(Students::class.java)
                if (student != null && !student.studentAttend!!) {
                    // Update studentAttend to true
                    document.reference.update("studentAttend", true)
                        .addOnSuccessListener {
                            // Inform the student that attendance is marked
                            Toast.makeText(context, "Attendance marked for $studentName", Toast.LENGTH_LONG).show()
                            println("Attendance marked for $studentName")
                            navController.navigate("student_mark_screen")

                        }
                        .addOnFailureListener { exception ->
                            // Handle failure
                            Toast.makeText(context, "Please Check Your Network Connection!!!", Toast.LENGTH_LONG).show()
                        }
                } else if (student?.studentAttend == true) {
                    // Inform the student that attendance is already marked
                    println("Attendance already marked for $studentName")
                    Toast.makeText(context, "Attendance already marked for $studentName", Toast.LENGTH_LONG).show()
                   // navController.navigate("student_mark_screen")
                } else {
                    // Student not found
                    Toast.makeText(context, "Student not found", Toast.LENGTH_LONG).show()
                    println("Student $studentName not found")
                }
            }
        }
        .addOnFailureListener { exception ->
            // Handle failure
        }
}


//                addDataToFirebase("Haruna Umar Abdullahi", "UG18/SCCS/1015", true, context)
//                addDataToFirebase("Adamu Umar Adamu", "UG18/SCCS/2007", true, context)
//                addDataToFirebase("Abdulrahman Abubakar", "UG16/SCCS/1047", true, context)
//                addDataToFirebase("Philip James", "UG18/SCCS/1160", true, context)
//                addDataToFirebase("Abdurrahman Baba Dahiru", "UG18/SCCS/1116", true, context)
//                addDataToFirebase("Aisha Mahmoud Arabi", "UG18/SCCS/1122", true, context)
//                addDataToFirebase("Amina Umar", "UG18/SCCS/1144", true, context)
//                addDataToFirebase("Ahmad Saidu", "UG18/SCCS/1134", true, context)
// context.startActivity(Intent(context, StudentMark::class.java))