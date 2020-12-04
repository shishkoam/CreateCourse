package ua.kblogika.interactive.utils.util

import android.content.Context
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

object StringUtils {
    /**
     * Safe number parsing
     *
     * @param intString int in string format
     * @return parsed int or 0 if failed
     */
    fun safeParseInt(intString: String): Int {
        return try {
            intString.toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }

    /**
     * Safe number parsing
     *
     * @param intToString int in string format
     * @param length      is length of out string
     * @return parsed int or 0 if failed
     */
    fun safeParseToString(intToString: Long, length: Int): String {
        var result = ""
        try {
            result = intToString.toString()
        } catch (e: NumberFormatException) {
            // skip exception
        }
        val resultLength = result.length
        for (i in resultLength until length) {
            result = "0$result"
        }
        return result
    }

    /**
     * Safe double parsing
     *
     * @param doubleString double in string format
     * @return parsed double or 0.0 if failed
     */
    fun safeParseDouble(doubleString: String): Double {
        var result = 0.0
        try {
            result = doubleString.toDouble()
        } catch (e: NumberFormatException) {
            // skip exception
        }
        return result
    }

    fun safeParseDouble(doubleValue: Double?): Double {
        return doubleValue ?: 0.0
    }

    fun smartFormatDouble(doubleValue: Double?, round: Int): Double {
        if (doubleValue == null) {
            return 0.0
        }
        var result = doubleValue!!
        for (i in 0 until round) {
            result *= 10
        }
        result = result.roundToInt().toDouble()
        for (i in 0 until round) {
            result /= 10.0
        }
        return result
    }

    /**
     * @param doubleValue can be both longitude or latitude
     * @param accuracy    Number of digits after decimal point (for seconds)
     * @param forceSign   show sign even if it's a plus
     * @return formatted degrees representation
     */
    /**
     * @param doubleValue can be both longitude or latitude
     * @param accuracy    Number of digits after decimal point (for seconds)
     * @return formatted degrees representation
     */

    fun toReadableDegrees(doubleValue: Double, accuracy: Int, forceSign: Boolean = false): String {
        var degrees = doubleValue.toInt()
        var minutes = ((doubleValue - degrees) * 60).toInt()
        var seconds = (doubleValue - degrees - minutes.toFloat() / 60) * 3600
        degrees = abs(degrees)
        minutes = abs(minutes)
        seconds = abs(seconds)
        var sign = ""
        val precision = 10.0.pow(-accuracy.toDouble())
        val isZero = degrees == 0 && minutes == 0 && seconds < precision
        if (isZero) {
            sign = ""
        } else if (doubleValue < 0) {
            sign = "-"
        } else if (doubleValue > 0 && forceSign) {
            sign = "+"
        }
        return sign + String.format("%d°%02d'%02." + accuracy + "f\"", degrees, minutes, seconds)
    }

    fun getStringResourceByName(aString: String?, context: Context): String {
        val packageName = context.packageName
        val resId = context.resources.getIdentifier(aString, "string", packageName)
        return context.getString(resId)
    }

    fun compareAlphanumeric(left: String?, right: String?): Int {
        if (left == null || right == null) {
            return 0
        }
        val a = left.toLowerCase(Locale.ROOT)
        val b = right.toLowerCase(Locale.ROOT)
        val lengthFirstStr = a.length
        val lengthSecondStr = b.length
        var index1 = 0
        var index2 = 0
        while (index1 < lengthFirstStr && index2 < lengthSecondStr) {
            var ch1 = a[index1]
            var ch2 = b[index2]
            val space1 = CharArray(lengthFirstStr)
            val space2 = CharArray(lengthSecondStr)
            var loc1 = 0
            var loc2 = 0
            do {
                space1[loc1++] = ch1
                index1++
                ch1 = if (index1 < lengthFirstStr) {
                    a[index1]
                } else {
                    break
                }
            } while (Character.isDigit(ch1) == Character.isDigit(space1[0]))
            do {
                space2[loc2++] = ch2
                index2++
                ch2 = if (index2 < lengthSecondStr) {
                    b[index2]
                } else {
                    break
                }
            } while (Character.isDigit(ch2) == Character.isDigit(space2[0]))
            val str1 = String(space1)
            val str2 = String(space2)
            var result: Int
            result = try {
                if (Character.isDigit(space1[0]) && Character.isDigit(space2[0])) {
                    val firstNumberToCompare = str1.trim { it <= ' ' }.toInt()
                    val secondNumberToCompare = str2.trim { it <= ' ' }.toInt()
                    firstNumberToCompare.compareTo(secondNumberToCompare)
                } else {
                    str1.compareTo(str2)
                }
            } catch (ex: NumberFormatException) {
                return 0
            }
            if (result != 0) {
                return result
            }
        }
        return lengthFirstStr - lengthSecondStr
    }

    fun tryParse(value: String, defalt: Double): Double {
        return try {
            value.toDouble()
        } catch (e: Exception) {
            defalt
        }
    }

    /**
     * @param value - to parse to double
     * @return - if parsable and length > 0 returns true , otherwise false
     */
    fun isValidDouble(value: String?): Boolean {
        var value = value ?: return false
        value = value.trim { it <= ' ' }
        if (value.isEmpty()) return false
        try {
            java.lang.Double.valueOf(value)
        } catch (ex: NumberFormatException) {
            return false
        }
        return true
    }

    fun concat(a: Array<String?>, b: Array<String?>): Array<String?> {
        val alen = a.size
        val blen = b.size
        if (alen == 0) {
            return b
        }
        if (blen == 0) {
            return a
        }
        val result = arrayOfNulls<String>(alen + blen)
        System.arraycopy(a, 0, result, 0, alen)
        System.arraycopy(b, 0, result, alen, blen)
        return result
    }
}