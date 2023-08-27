package com.pyc0d3r.classattendanceprototype

import com.google.firebase.firestore.Exclude
/**
 * Data class representing a student's attendance information.
 *
 * @property studentID The unique identifier for the student.
 * @property studentName The name of the student.
 * @property studentNo The student's identification number.
 * @property studentAttend Indicates whether the student has attended or not. Default is false.
 *
 * @constructor Creates a new instance of the [Students] data class.
 */
data class Students(
    @get:Exclude var studentID: String? = "",
    var studentName: String? = "",
    var studentNo: String? = "",
    var studentAttend: Boolean? = false
)
