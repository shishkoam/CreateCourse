package ua.kblogika.interactive.utils.util

import ua.kblogika.interactive.utils.util.FileUtils.copyFile
import java.io.*
import java.util.regex.Pattern

object FolderUtils {
    /**
     * @param srcDirPath  Path to folder which should be moved
     * @param destDirPath Path to folder, where srcDir will be pasted
     * @return Whether folder was successfully moved or not
     */
    fun moveFolder(srcDirPath: String?, destDirPath: String?): Boolean {
        val srcFolder = File(srcDirPath)
        val destFolder = File(destDirPath)
        if (!srcFolder.exists() || destFolder.exists() && !destFolder.isDirectory) {
            return false
        } else if (!destFolder.exists()) {
            destFolder.mkdir()
        }
        val targetFolder = File(destDirPath, srcFolder.name)
        if (srcFolder.path == targetFolder.path) {
            return false
        }
        var canMoveSafe = !targetFolder.exists()
        if (!canMoveSafe) {
            var containsNoFiles = true
            for (file in targetFolder.listFiles()) {
                containsNoFiles = containsNoFiles and file.isDirectory
            }
            canMoveSafe = containsNoFiles
        }
        if (canMoveSafe) {
            try {
                copyFolderRecursive(srcFolder, targetFolder)
                deleteFolderRecursive(srcFolder)
                return true
            } catch (ignore: IOException) {
            }
        }
        return false
    }

    @Throws(IOException::class)
    fun copyFolderRecursive(source: File, target: File) {
        if (source.isDirectory) {
            if (!target.exists()) {
                target.mkdir()
            }
            val var2 = source.listFiles()
            val var3 = var2.size
            for (var4 in 0 until var3) {
                val file = var2[var4]
                copyFolderRecursive(file, File(target, file.name))
            }
        } else {
            val `in`: InputStream = FileInputStream(source)
            val out: OutputStream = FileOutputStream(target)
            val buf = ByteArray(1024)
            var len: Int
            while (`in`.read(buf).also { len = it } > 0) {
                out.write(buf, 0, len)
            }
            `in`.close()
            out.close()
        }
    }

    fun deleteFolderRecursive(folder: File) {
        if (folder.isDirectory) {
            val var1 = folder.listFiles()
            val var2 = var1.size
            for (var3 in 0 until var2) {
                val file = var1[var3]
                deleteFolderRecursive(file)
            }
        }
        folder.delete()
    }

    /**
     * @param srcDirPath      Path to folder, from which files will be moved from
     * @param destDirPath     Path to folder, where files will be pasted
     * @param regexFileFilter Regular expression to filter files by name
     * @return Whether any file was successfully moved or not
     */
    fun moveFiles(srcDirPath: String?, destDirPath: String?, regexFileFilter: String?): Boolean {
        val srcFolder = File(srcDirPath)
        val destFolder = File(destDirPath)
        val pattern = Pattern.compile(regexFileFilter)
        var successful = false
        if (srcFolder.path == destFolder.path) {
            return false
        }
        val filter = FilenameFilter { dir, filename -> pattern.matcher(filename).find() }
        if (!srcFolder.exists() || destFolder.exists() && !destFolder.isDirectory) {
            return false
        } else if (!destFolder.exists()) {
            destFolder.mkdir()
        }
        for (file in srcFolder.listFiles(filter)) {
            val destFile = File(destFolder.path, file.name)
            if (!destFile.exists()) {
                try {
                    copyFile(file, destFile)
                    successful = true
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            file.delete()
        }
        return successful
    }
}