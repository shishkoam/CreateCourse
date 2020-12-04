package ua.kblogika.interactive.utils.util

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.DataFormatException
import java.util.zip.Deflater
import java.util.zip.Inflater


object CompressUtils {
    @Throws(IOException::class, DataFormatException::class)
    fun decompress(data: ByteArray?): ByteArray? {
        if (data == null) {
            return null
        }
        val inflater = Inflater()
        inflater.setInput(data)
        val outputStream = ByteArrayOutputStream(data.size)
        val buffer = ByteArray(1024)
        while (!inflater.finished()) {
            val count: Int = inflater.inflate(buffer)
            outputStream.write(buffer, 0, count)
        }
        outputStream.close()
        return outputStream.toByteArray()
    }

    @Throws(IOException::class)
    fun compress(data: ByteArray?): ByteArray? {
        if (data == null) {
            return null
        }
        val deflater = Deflater()
        deflater.setInput(data)
        val outputStream = ByteArrayOutputStream(data.size)
        deflater.finish()
        val buffer = ByteArray(1024)
        while (!deflater.finished()) {
            val count: Int = deflater.deflate(buffer) // returns the generated code... index
            outputStream.write(buffer, 0, count)
        }
        outputStream.close()
        return outputStream.toByteArray()
    }
}