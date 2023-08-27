package com.pyc0d3r.classattendanceprototype

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.pyc0d3r.classattendanceprototype.ui.theme.ClassAttendancePrototypeTheme
import kotlinx.coroutines.tasks.await

class AdminActivity : ComponentActivity() {
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
 * Composable function to display the list of students and their attendance status.
 *
 * @param navController The NavController to navigate between screens.
 */
@Composable
fun StudentListScreen(navController: NavController) {
    // Get the application context
    val context = LocalContext.current
    // Initialize Firestore instance
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Remember the list of students' attendance information
    val studentList = remember { mutableStateListOf<Students>() }

    // Fetch student attendance data from Firestore using Kotlin Coroutines
    LaunchedEffect(true) {
        try {
            val querySnapshot = db.collection("Courses").get().await()

            // Map querySnapshot documents to Students objects
            val students = querySnapshot.documents.mapNotNull { documentSnapshot ->
                val student = documentSnapshot.toObject(Students::class.java)
                student?.apply {
                    studentID = documentSnapshot.id
                }
            }

            // Update the studentList with the fetched data
            studentList.clear()
            studentList.addAll(students)
        } catch (e: Exception) {
            // Handle the exception, e.g., show an error message
            Toast.makeText(context, "Fail to get the data.", Toast.LENGTH_SHORT).show()
        }
    }

    // Display the UI
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header text
        Text(
            text = "Welcome: Assoc Prof. Bala Modi",
            modifier = Modifier
                .padding(16.dp),
            style = MaterialTheme.typography.h6
        )

        // Calculate attendance statistics
        val totalStudents = studentList.size
        val attendedStudents = totalStudents - studentList.count { it.studentAttend == false }

        // Display attendance statistics
        Text(
            text = "Attended: $attendedStudents / Total: $totalStudents",
            modifier = Modifier
                .padding(16.dp),
            style = MaterialTheme.typography.subtitle1
        )

        // Display the list of students
        LazyColumn {
            items(studentList) { student ->
                student?.let { nonNullStudent ->
                    // Display student card
                    StudentCard(nonNullStudent) { updatedAttend ->
                        // Update studentList with the new attended value
                        val index = studentList.indexOf(nonNullStudent)
                        if (index != -1) {
                            studentList[index] = nonNullStudent.copy(studentAttend = updatedAttend)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable function to display a student card with attendance information.
 *
 * @param student The student data.
 * @param onAttendChanged Callback invoked when the attendance status is changed.
 */
@Composable
fun StudentCard(student: Students, onAttendChanged: (Boolean) -> Unit) {
    // Create a card to display student information
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
            // Display student name in bold
            student.studentName?.let { Text(text = it, style = TextStyle(fontWeight = FontWeight.Bold)) }

            // Display student number
            Text(text = "Student No: ${student.studentNo}")

            // Create a row to display attendance checkbox and label
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Display attendance checkbox
                Checkbox(
                    checked = student.studentAttend ?: false,
                    onCheckedChange = { newValue ->
                        // Invoke the callback to update the attendance status
                        onAttendChanged(newValue)

                        // Update Firestore with new attendance value
                        val db = FirebaseFirestore.getInstance()
                        val docRef = student.studentID?.let { it1 ->
                            db.collection("Courses").document(it1)
                        }
                        if (docRef != null) {
                            docRef.update("studentAttend", newValue)
                                .addOnSuccessListener {
                                    // Update successful
                                }
                                .addOnFailureListener { exception ->
                                    // Handle failure
                                }
                        }
                    }
                )
                // Add spacing between checkbox and label
                Spacer(modifier = Modifier.width(8.dp))
                // Display attendance label
                Text(text = "Attended", style = MaterialTheme.typography.body2)
            }
        }
    }
}









