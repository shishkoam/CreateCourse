package ua.kblogika.interactive.utils.interactors

import android.app.AlertDialog
import android.content.Context

class DebugUtils {
    companion object {
        fun showExceptionInDialog(context: Context, e: Exception) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Error")
            builder.setMessage("$e")
            builder.setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
            builder.create().show()
        }
    }
}