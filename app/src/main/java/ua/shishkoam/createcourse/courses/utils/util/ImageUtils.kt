package ua.kblogika.interactive.utils.util

import android.graphics.Color

object ImageUtils {
    const val ACCURACY = 20
    private fun setOpacity(color: Int, alpha: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        return Color.HSVToColor(alpha, hsv)
    }

    private fun setBrightness(color: Int, brightness: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= brightness
        if (hsv[2] > 1) {
            hsv[2] = 1f
        }
        return Color.HSVToColor(hsv)
    }

    private fun rotateColor(color: Int, rotation: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        if (hsv[2] == 0f || hsv[1] == 0f && hsv[2] == 1f) {
            hsv[0] = 0f
            hsv[1] = 1f
            hsv[2] = 1f
        } else {
            hsv[0] = (hsv[0] + rotation) % 256
        }
        return Color.HSVToColor(hsv)
    }
}