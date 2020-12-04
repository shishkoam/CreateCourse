package ua.kblogika.interactive.courses.objects.person

import ua.kblogika.interactive.courses.objects.enums.StudentType
import ua.kblogika.interactive.courses.objects.progress.CourseProgress
import java.util.*

data class Student (
    var studentId: String? = null,
    var name: String? = null,
    var surname: String? = null,
    var info: String? = null,
    var email: String? = null,
    var courseProgressHashMap: HashMap<String?, CourseProgress>? = null,
    var badges: HashSet<Long>? = null,
    var studentType: StudentType? = null
)