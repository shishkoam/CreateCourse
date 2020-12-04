package ua.kblogika.interactive.courses.objects.progress

data class TaskProgress(
    var isSuccess: Boolean = false,
    var info: String? = null,
    var lastChangeTime: Long = 0,
    var successPercent: Int = 0
)