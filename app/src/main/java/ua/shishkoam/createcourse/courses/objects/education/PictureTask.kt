package ua.kblogika.interactive.courses.objects.education

import ua.kblogika.interactive.courses.objects.enums.TaskType


class PictureTask : Task() {
    var text: String? = null
    var picturePath: Int? = null

    init {
        taskType = TaskType.Picture
    }
}