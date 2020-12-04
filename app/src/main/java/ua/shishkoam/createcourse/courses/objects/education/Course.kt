package ua.kblogika.interactive.courses.objects.education

import ua.kblogika.interactive.courses.objects.enums.CourseType
import java.util.*

class Course(
    var courseId: String? = null,
    var name: String? = null,
    var info: String? = null,
    var image: Int? = null, //temporary
    var lessons: ArrayList<String>? = null,
    var students: List<String>? = null,
    var authors: List<String>? = null,
    var courseType: CourseType? = null,
    var lastChangeTime: Long = 0
)