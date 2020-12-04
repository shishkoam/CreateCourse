package ua.kblogika.interactive.utils.util

import android.graphics.Point
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ListView
import android.widget.ScrollView

object ViewUtils {
    var VIEW_DISABLING_TIME: Long = 1000 // ms

    fun setViewTemporaryDisabled(button: View) {
        button.isEnabled = false
        val handler = Handler()
        handler.postDelayed({ button.isEnabled = true }, VIEW_DISABLING_TIME)
    }

    fun setNoExtractUi(editText: EditText) {
        editText.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
    }

    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter ?: return
        val desiredWidth =
            View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.UNSPECIFIED)
        var totalHeight = 0
        var view: View? = null
        for (i in 0 until listAdapter.count) {
            view = listAdapter.getView(i, view, listView)
            if (i == 0) view.layoutParams =
                ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += view.measuredHeight
        }
        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
        listView.requestLayout()
    }

    fun dp2px(view: View, padding_in_dp: Int): Int {
        val scale = view.resources.displayMetrics.density
        return (padding_in_dp * scale + 0.5f).toInt()
    }

    fun scrollToView(scrollViewParent: ScrollView, view: View) {
        // Get deepChild Offset
        val childOffset = Point()
        getDeepChildOffset(scrollViewParent, view.parent, view, childOffset)
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y)
    }

    fun getDeepChildOffset(
        mainParent: ViewGroup,
        parent: ViewParent,
        child: View,
        accumulatedOffset: Point
    ) {
        val parentGroup = parent as ViewGroup
        accumulatedOffset.x += child.left
        accumulatedOffset.y += child.top
        if (parentGroup == mainParent) {
            return
        }
        getDeepChildOffset(mainParent, parentGroup.parent, parentGroup, accumulatedOffset)
    }
}