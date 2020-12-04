package ua.mil.armysos.storage

import java.io.*

class StringFileStorage : StringStorage {
    override fun load(path: String): String? {
        val file = File(path)
        if (file.exists()) {
            val text = StringBuilder()
            val br = BufferedReader(FileReader(file))
            var line: String?

            while (br.readLine().also { line = it } != null) {
                text.append(line)
                text.append('\n')
            }

            br.close()

            return text.toString()
        } else {
            return null
        }
    }

    override fun save(data: String, path: String): Boolean {
        val file = File(path)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return try {
            //BufferedWriter for performance, true to set append to file flag
            val buf = BufferedWriter(FileWriter(file, true))
            buf.append(data)
            buf.newLine()
            buf.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}