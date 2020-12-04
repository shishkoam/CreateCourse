package ua.kblogika.interactive.utils.interactors

import android.graphics.Bitmap
import android.util.LruCache
import android.widget.ImageView
import com.bumptech.glide.Glide

object  ImageLoader {


    private var mMemoryCache: LruCache<String, Bitmap>? = null
    fun init() {

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8
        mMemoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.byteCount / 1024
            }
        }
    }

    fun addBitmapToMemoryCache(key: String, bitmap: Bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache?.put(key, bitmap)
        }
    }

    fun getBitmapFromMemCache(key: String): Bitmap? {
        return mMemoryCache?.get(key)
    }

    fun clearCache() {
        mMemoryCache?.evictAll()
    }

    fun loadImage(imageView: ImageView, drawable: Int?, errorDrawable: Int = android.R.drawable.stat_notify_error) {
        Glide.with(imageView.context.applicationContext).load(drawable)
            .error(errorDrawable)
            .into(imageView)
    }
}