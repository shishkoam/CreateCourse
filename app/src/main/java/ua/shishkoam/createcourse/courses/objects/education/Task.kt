package ua.kblogika.interactive.courses.objects.education

import ua.kblogika.interactive.courses.objects.enums.TaskType
import ua.kblogika.interactive.courses.objects.progress.TaskMark

abstract class Task {
    var lessonId: String? = null
    var taskId: String? = null
    var name: String? = null
    var info: String? = null
    var taskType: TaskType? = null
    var taskMark: TaskMark? = null
    var lastChangeTime: Long = 0
}