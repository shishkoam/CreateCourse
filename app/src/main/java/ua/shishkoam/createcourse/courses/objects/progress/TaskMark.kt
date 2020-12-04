package ua.kblogika.interactive.courses.objects.progress

import ua.kblogika.interactive.courses.objects.enums.TaskProgressType

data class TaskMark (
    var info: String? = null,
    var taskProgressType: TaskProgressType? = null
)