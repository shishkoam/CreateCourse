package ua.kblogika.interactive.courses.objects.education

import ua.kblogika.interactive.courses.objects.enums.LessonType

data class Lesson (
    var courseId: String? = null,
    var lessonId: String? = null,
    var name: String? = null,
    var image: Int? = null,
    var info: String? = null,
    var tasks: List<String>? = null,
    var lessonType: LessonType? = null,
    var lastChangeTime: Long = 0
)