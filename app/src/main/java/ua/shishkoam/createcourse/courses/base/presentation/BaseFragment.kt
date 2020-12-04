package ua.kblogika.interactive.base.presentation

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

import ua.kblogika.interactive.base.interactors.OptionMenuState
import ua.shishkoam.createcourse.R

open class BaseFragment(id: Int, private val menuVisibility: Boolean = true) : Fragment(id){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? OptionMenuState)?.setOptionMenuVisibility(menuVisibility)
    }

    fun showNotification(context: Context, title: String, message: String, cls: Class<*>? = null) {

    }



    //dialog fragment

//    fun showInfoDialog(message: String) {
//        val builder = AlertDialog.Builder(activity)
//        builder.setTitle(R.string.information)
//            .setIcon(R.drawable.ic_info_blue_24dp)
//            .setMessage(message)
//            .setPositiveButton(android.R.string.ok, null)
//        builder.create().show()
//    }
//
//    open fun showErrorDialog(context: Context?, message: String) {
//        if (context == null) return
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle(R.string.error)
//        builder.setMessage(message)
//        builder.setIcon(R.drawable.ic_error_red_24dp)
//        builder.setCancelable(false)
//        builder.setPositiveButton(
//            android.R.string.ok
//        ) { dialog, which -> dialog.dismiss() }
//        builder.show()
//    }
}