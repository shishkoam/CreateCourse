package ua.kblogika.interactive.utils.util

import android.content.Context
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

object FormatUtils {

    private fun getMeasuredSize(size: Long, divider: Long): String {
        val measuredSize = size / divider.toDouble()
        val ratioSizePart = measuredSize % 1
        return if (ratioSizePart in 0.05..0.95) String.format(
            "%.1f ",
            measuredSize
        ) else String.format("%d ", Math.round(measuredSize))
    }

    /**
     * Formats a "delta" value by rounding it and always putting a sign in front, even if it's
     * a plus.
     *
     * @param delta a positive or negative "delta" value
     * @return formatted string which represents an integer number prefixed with a sign
     */
    fun formatDeltaInteger(delta: Int): String {
        return DecimalFormat("+0;-0").format(delta.toLong())
    }

    /**
     * Formats a "delta" value by subtracting previous from current and always putting a sign
     * in front, even if it's a plus.
     *
     * @param delta a positive or negative "delta" value
     * @return formatted string which represents a decimal number prefixed with a sign
     */
    fun formatDeltaDecimal(delta: Double): String {
        return DecimalFormat("+0.#;-0.#").format(delta)
    }

    /**
     * Formats a "delta" value by subtracting previous from current and always putting a sign
     * in front, even if it's a plus.
     *
     * @param delta a positive or negative "delta" value
     * @return formatted string which represents a decimal number prefixed with a sign
     */
    fun formatDeltaDecimalWith(delta: Double): String {
        return DecimalFormat("+0.1;-0.1").format(delta)
    }

    /**
     * Get string representation of the Time zone for the date.
     *
     * @param timeZone the time zone
     * @param date     date for which the time zone is displayed - daylight saving may apply
     * @return string representation of the time zone
     */
    fun formatTimeZone(timeZone: TimeZone, date: Date): String {
        val displayName = timeZone.getDisplayName(timeZone.inDaylightTime(date), TimeZone.LONG)
        if (displayName.startsWith("GMT")) {
            return displayName
        }
        val offset = timeZone.getOffset(date.time)
        val hours = TimeUnit.MILLISECONDS.toHours(offset.toLong())
        val minutes = Math.abs(
            TimeUnit.MILLISECONDS.toMinutes(offset.toLong())
                    - TimeUnit.HOURS.toMinutes(hours)
        )
        return String.format(
            "GMT %+d:%02d, %s",
            hours, minutes, displayName
        )
    }

    fun getFormatedDeltaTime(deltaTimeSeconds: Long): String {
        return String.format(
            Locale.getDefault(), "%02d:%02d:%02d",
            deltaTimeSeconds / (60 * 60) % 24,
            deltaTimeSeconds / 60 % 60,
            deltaTimeSeconds % 60
        )
    }

    fun getFormatedDeltaTimeMilliseconds(deltaTimeSeconds: Long): String {
        return String.format(
            Locale.getDefault(), "%02d:%02d:%02d:%04d",
            deltaTimeSeconds / (60 * 60 * 1000) % 24,
            deltaTimeSeconds / (60 * 1000) % 60,
            deltaTimeSeconds / 1000 % 60,
            deltaTimeSeconds % 1000
        )
    }

    private val TWO_FRACTIONS_FORMAT = DecimalFormat("0.##")
    fun getCalculationDetailsFormatDouble(value: Double): String {
        var value = value
        value = (value * 100).roundToInt() / 100.0
        return TWO_FRACTIONS_FORMAT.format(value)
    }
}