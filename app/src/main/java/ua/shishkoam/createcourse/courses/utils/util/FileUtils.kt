package ua.kblogika.interactive.utils.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.io.*
import java.nio.charset.Charset
import java.util.regex.Pattern

object FileUtils {
    /**
     * Read text file completely into string
     *
     * @param file file object
     * @return text data
     * @throws IOException
     */
    @Throws(IOException::class)
    fun readTextFile(file: File): String {
        val text = StringBuilder()
        val br = BufferedReader(FileReader(file))
        var line: String?
        while (br.readLine().also { line = it } != null) {
            text.append(line)
            text.append('\n')
        }
        br.close()
        return text.toString()
    }

    /**
     * Read binary file completely into byte array
     *
     * @param file file object
     * @return byte array
     * @throws IOException
     */
    @Throws(IOException::class)
    fun readBinaryFile(file: File): ByteArray {
        val buffer = ByteArray(file.length().toInt())
        val bis = BufferedInputStream(FileInputStream(file))
        var totalBytesRead = 0
        while (totalBytesRead < buffer.size) {
            val bytesRemaining = buffer.size - totalBytesRead
            val bytesRead = bis.read(buffer, totalBytesRead, bytesRemaining)
            totalBytesRead += if (bytesRead > 0) {
                bytesRead
            } else {
                break
            }
        }
        bis.close()
        return buffer
    }

    /**
     * Copy a file from one folder to another
     *
     * @param srcFolder  source folder
     * @param destFolder destination folder
     * @param filename   filename
     * @throws IOException
     */
    @Throws(IOException::class)
    fun copy(srcFolder: String, destFolder: String, filename: String) {
        val checkDirectory: File = File(destFolder)
        if (!checkDirectory.exists()) {
            checkDirectory.mkdir()
        }
        val `in`: InputStream = FileInputStream(srcFolder + filename)
        val out: OutputStream = FileOutputStream(destFolder + filename)

        // Transfer bytes from in to out
        val buf = ByteArray(1024)
        var len: Int
        while (`in`.read(buf).also { len = it } > 0) out.write(buf, 0, len)
        `in`.close()
        out.close()
    }

    /**
     * Copy a file
     *
     * @param src source stream
     * @param dst dest stream
     * @throws IOException
     */
    @JvmStatic
    @Throws(IOException::class)
    fun copyFile(src: File, dst: File) {
        val `in`: InputStream = FileInputStream(src)
        val out: OutputStream = FileOutputStream(dst)

        // Transfer bytes from in to out
        val buf = ByteArray(1024)
        var len: Int
        while (`in`.read(buf).also { len = it } > 0) {
            out.write(buf, 0, len)
        }
        `in`.close()
        out.close()
    }

    /**
     * Method moves file srcPath src to dstPath.
     *
     * @param srcPath     - source folder
     * @param srcFileName - source filename
     * @param dstPath     - destination folder
     * @param dstFileName - destination filename
     */
    fun moveFile(
        context: Context,
        srcPath: String,
        srcFileName: String,
        dstPath: String,
        dstFileName: String
    ) {
        var `in`: InputStream?
        val out: OutputStream
        try {

            //create output directory if it doesn't exist
            val dir = File(dstPath)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            `in` = FileInputStream(File(srcPath, srcFileName))
            out = FileOutputStream(File(dstPath, dstFileName))
            val buffer = ByteArray(1024)
            var read: Int
            while (`in`.read(buffer).also { read = it } != -1) {
                out.write(buffer, 0, read)
            }
            `in`.close()
            `in` = null

            // write the output file
            out.flush()
            out.close()

            // delete the original file
            val deleted = File(srcPath, srcFileName)
            deleted.delete()

            // Preventing bug
            // "https://code.google.com/p/android/issues/detail?id=38282"
            context.sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(deleted)
                )
            )
        } catch (fnfe1: FileNotFoundException) {
        } catch (e: Exception) {
        }
    }

    /**
     * Gets name of file from full path
     *
     * @param pathName full path of file
     * @return name of file
     */
    fun fileName(pathName: String): String {
        return File(pathName).name
    }

    /**
     * @param fileNameExt full file name including extension
     * @return file name without extension
     */
    fun getNameOnly(fileNameExt: String): String {
        val pos = fileNameExt.lastIndexOf('.')
        return if (pos == -1) fileNameExt else fileNameExt.substring(0, pos)
    }

    /**
     * Gets file extension from full name
     *
     * @param fileNameExt full file name including extension
     * @return extension of file
     */
    fun getExt(fileNameExt: String): String {
        val pos = fileNameExt.lastIndexOf('.')
        return if (pos == -1) "" else fileNameExt.substring(pos + 1)
    }

    fun renameToBak(file: File): Boolean {
        if (!file.exists()) return false
        val newFile = File(file.parentFile, file.name + ".bak")
        if (newFile.exists()) newFile.delete()
        return file.renameTo(newFile)
    }

    /**
     * If initial file exists then creates and returns new file with name like as "name (n).ext"
     *
     * @param initial - initial file
     * @return - initial file if not exists or new file with parentheses in name
     */
    fun fileWithParentheses(initial: File): File {
        var initial = initial
        if (!initial.exists()) return initial
        val folder = initial.parentFile
        var name = initial.name
        var ext: String? = ""
        val pos = name.lastIndexOf(".")
        if (pos >= 0) {
            ext = name.substring(pos)
            name = name.substring(0, pos)
        }
        var n = 0
        val matcher = Pattern.compile("\\s\\((\\d+)\\)$").matcher(name)
        if (matcher.find()) {
            n = Integer.valueOf(matcher.group(1))
            name = matcher.replaceAll("")
        }
        while (File(folder, String.format("%s (%d)%s", name, ++n, ext)).also { initial = it }
                .exists());
        return initial
    }

    @Throws(IOException::class)
    fun saveStringToFile(file: File, data: String) {
        val writer = BufferedWriter(FileWriter(file))
        writer.write(data)
        writer.flush()
        writer.close()
    }

    fun folderContent(folder: File): Array<File>? {
        if (!folder.exists() || !folder.isDirectory) return null
        val list = folder.listFiles()
        return if (list.isNullOrEmpty()) null else list
    }

    fun isSymbolic(file: File): Boolean {
        return try {
            file.canonicalFile != file.absoluteFile
        } catch (e: IOException) {
            true
        }
    }

    fun writeToFile(dataFile: File, data: String) {
//        if (!dataFile.exists() && !dataFile.isDirectory) {
//            dataFile.mkdirs()
//        }
        try {
            dataFile.createNewFile()
            val fileWriter = FileWriter(dataFile)
            fileWriter.write(data)
            fileWriter.close()
        } catch (ex: Exception) {
            //
        }
    }


    fun readFromFile(file: File): String? {
        val sb = StringBuilder()
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(
                InputStreamReader(
                    FileInputStream(file), Charset.forName("UTF-8")
                )
            )
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
            }
        } catch (e: IOException) {
            // log error
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    // log warning
                }
            }
        }
        return sb.toString()
    }
}