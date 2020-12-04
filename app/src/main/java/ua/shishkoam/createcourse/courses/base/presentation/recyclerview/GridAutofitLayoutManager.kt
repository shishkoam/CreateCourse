package ua.shishkoam.fundamentals.recyclerview

import android.content.Context
import android.util.TypedValue
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class GridAutofitLayoutManager : GridLayoutManager {
    private var columnWidth = 0
    private var isColumnWidthChanged = true
    private var lastWidth = 0
    private var lastHeight = 0
    private var recommendedItemWidth = 0f

    companion object{
        const val AUTO_FIT = -1
    }

    constructor(context: Context, columnWidth: Int, recommendedItemWidth: Float) : super(context, 1) {
        /* Initially set spanCount to 1, will be changed automatically later. */
        this.recommendedItemWidth = recommendedItemWidth
        setColumnWidth(checkedColumnWidth(context, columnWidth))
    }

    constructor(
        context: Context,
        columnWidth: Int,
        orientation: Int,
        reverseLayout: Boolean
    ) : super(context, 1, orientation, reverseLayout) {

        /* Initially set spanCount to 1, will be changed automatically later. */
        setColumnWidth(checkedColumnWidth(context, columnWidth))
    }

    private fun checkedColumnWidth(context: Context, columnNumber: Int): Int {
        return if (AUTO_FIT == columnNumber) {
            /* Set default columnWidth value (166dp here). It is better to move this constant
                to static constant on top, but we need context to convert it to dp, so can't really
                do so. */
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, recommendedItemWidth,
                context.resources.displayMetrics
            ).toInt()
        } else {
            columnNumber
        }
    }

    fun setColumnWidth(newColumnWidth: Int) {
        if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
            columnWidth = newColumnWidth
            isColumnWidthChanged = true
        }
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        val width = width
        val height = height
        if (columnWidth > 0 && width > 0 && height > 0 && (isColumnWidthChanged || lastWidth != width || lastHeight != height)) {
            val totalSpace: Int
            totalSpace = if (orientation == VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }
            val spanCount = Math.max(1, totalSpace / columnWidth)
            setSpanCount(spanCount)
            isColumnWidthChanged = false
        }
        lastWidth = width
        lastHeight = height
        super.onLayoutChildren(recycler, state)
    }
}