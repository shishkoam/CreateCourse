package ua.kblogika.interactive.courses.objects.progress

import java.util.*

data class CourseProgress (
    var courseId: String? = null,
    var teacherId: String? = null,
    var tasksProgress: HashMap<Long, TaskProgress>? = null,
    var info: String? = null,
    var successPercent :Int = 0
)