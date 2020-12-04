package ua.kblogika.interactive.utils.util

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.view.inputmethod.InputMethodManager

object UIUtils {

    fun getColorResourceByName(aString: String?, context: Context): Int {
        val packageName = context.packageName
        val resId = context.resources.getIdentifier(aString, "color", packageName)
        return context.resources.getColor(resId)
    }

    fun getInt(string: Editable?): Int {
        val data = string?.toString()
        return if (data == null || data == "" || data == "-") {
            0
        } else {
            try {
                Integer.valueOf(data)
            } catch (e: NumberFormatException) {
                0
            }
        }
    }

    fun createTimerResult(min: Int, sec: Int, msec: Int): String {
        return addZeroFirst(min, 2) + ":" + addZeroFirst(sec, 2) + "." + msec
    }

    fun addZeroFirst(i: Int, length: Int): String {
        var s = i.toString()
        if (s.length < length) {
            s = "0$s"
        }
        return s
    }

    fun getDouble(string: Editable?): Double {
        val data = string?.toString()
        return if (data == null || data == "" || data == "-") {
            0.0
        } else {
            try {
                java.lang.Double.valueOf(data)
            } catch (e: NumberFormatException) {
                0.0
            }
        }
    }


    fun hideKeyboard(activity: Activity?) {
        if (activity == null) return
        val imm = activity.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager

        // check if no view has focus:
        val view = activity.currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}