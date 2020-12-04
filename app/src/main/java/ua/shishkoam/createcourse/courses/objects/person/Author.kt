package ua.kblogika.interactive.courses.objects.person

import ua.kblogika.interactive.courses.objects.education.Course
import ua.kblogika.interactive.courses.objects.enums.AuthorType
import java.util.*

data class Author (
    var id: String? = null,
    var name: String? = null,
    var surname: String? = null,
    var info: String? = null,
    var email: String? = null,
    var courses: List<Course>? = null,
    var studentByCourses
            : HashMap<String, List<Student>>? = null,
    var authorType: AuthorType? = null
){
}