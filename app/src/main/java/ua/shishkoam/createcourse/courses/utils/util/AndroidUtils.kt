package ua.kblogika.interactive.utils.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

object AndroidUtils {

    fun <T : Enum<*>?> searchEnum(
        enumeration: Class<T>,
        search: String?
    ): T? {
        for (each in enumeration.enumConstants) {
            if (each!!.name.compareTo(search!!, ignoreCase = true) == 0) {
                return each
            }
        }
        return null
    }

    fun setupScreenOrientation(activity: Activity, orientation: Int) {
        activity.requestedOrientation = orientation
    }

    /**
     * @param context          current context
     * @param themeAttributeId attribute id from R.attr
     * @return Drawable by an attr reference for the current theme
     */
    fun getDrawableByThemeAttribute(context: Context, themeAttributeId: Int): Drawable? {
        // Create an array of the attributes we want to resolve
        // using values from a theme
        val attrs = intArrayOf(themeAttributeId /* index 0 */)
        // Obtain the styled attributes. 'themedContext' is a context with a
        val typedArray = context.obtainStyledAttributes(attrs)
        // The parameter is the index of the attribute in the 'attrs' array.
        val drawableFromTheme = typedArray.getDrawable(0 /* index */)
        // Finally, free the resources used by TypedArray
        typedArray.recycle()
        return drawableFromTheme
    }

    fun isAppInstalled(context: Context, packageName: String?): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName!!, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}