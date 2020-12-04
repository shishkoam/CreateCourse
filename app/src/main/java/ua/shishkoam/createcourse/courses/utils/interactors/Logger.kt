package ua.kblogika.interactive.utils.interactors

import ua.mil.armysos.storage.StringFileStorage
import ua.mil.armysos.storage.StringStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Logger {
    fun appendLog(text: String?) {
        //remove it if need logging
        if (true) return
        val logFile = File("filepath", "mapLog.txt")
        val storage: StringStorage = StringFileStorage()
        storage.save(text!!, logFile.absolutePath)
    }

    fun appendLogWithTime(text: String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        appendLog(sdf.format(Date(System.currentTimeMillis())) + " " + text)
    }

    fun appendLogWithTime(text: String, list: ArrayList<String?>) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val sb = StringBuilder()
        for (i in list.indices) {
            sb.append(list[i]).append(";")
        }
        appendLog(sdf.format(Date(System.currentTimeMillis())) + " " + text + sb.toString())
    }

    fun writeException(e: Exception?) {
        val sb = getString(e) ?: return
        appendLogWithTime(sb.toString())
    }

    fun getString(e: Exception?): StringBuilder? {
        if (e == null || e.message == null) {
            return null
        }
        val sb = StringBuilder(e.message)
        val stack = e.stackTrace
        if (stack != null) {
            for (aStack in stack) {
                sb.append(aStack.toString())
                sb.append("\n")
            }
        }
        return sb
    }
}