package ua.kblogika.interactive.courses.objects.education

import ua.kblogika.interactive.courses.objects.enums.TaskType
import java.util.*

class TestTask : Task() {
    var answerVariants: List<String>? = null
    var rightVariantsPositions: Set<Int>? = null

    init {
        taskType = TaskType.Test
    }
}