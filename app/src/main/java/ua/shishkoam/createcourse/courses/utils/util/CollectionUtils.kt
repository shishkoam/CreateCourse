package ua.kblogika.interactive.utils.util

import android.os.Bundle

object CollectionUtils {
    fun toBundleBooleanMap(input: HashMap<String?, Boolean>): Bundle? {
        val output = Bundle()
        for (key in input.keys) {
            output.putBoolean(key, input[key]!!)
        }
        return output
    }

    fun fromBundleBooleanMap(input: Bundle?): Map<String?, Boolean>? {
        return input?.let {
            val output: MutableMap<String?, Boolean> = HashMap()
            for (key in input.keySet()) {
                output[key] = input.getBoolean(key)
            }
            return output
        }
    }
}