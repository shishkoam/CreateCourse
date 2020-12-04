package ua.shishkoam.createcourse

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ua.shishkoam.createcourse.ChatUtils.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.zip.Deflater
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val editText = EditText(this)
            builder.setTitle("Відправити створенні завдання")
                .setIcon(R.drawable.ic_info_blue_24dp)
                .setView(editText)
                .setPositiveButton(
                    "Відправити"
                ) { _, _ -> sendBundleInfo(editText.text.toString()) }
                .setNegativeButton("Відміна", null)
            builder.create().show()

        }
        if (!allPermissionsGranted()) {
            getRuntimePermissions()
        }

    }


    private fun getRequiredPermissions(): Array<String?> {
        return try {
            val info = this.packageManager
                .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if (ps != null && ps.isNotEmpty()) {
                ps
            } else {
                arrayOfNulls(0)
            }
        } catch (e: Exception) {
            arrayOfNulls(0)
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in getRequiredPermissions()) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val allNeededPermissions = ArrayList<String>()
        for (permission in getRequiredPermissions()) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    allNeededPermissions.add(permission)
                }
            }
        }

        if (allNeededPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this, allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS
            )
        }
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "Permission granted: $permission")
            return true
        }
        Log.i(TAG, "Permission NOT granted: $permission")
        return false
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val TAG = "ChooserActivity"
        private const val PERMISSION_REQUESTS = 1
    }

    private fun sendBundleInfo(message: String) {
        try {
            val filelocation: File = createInfoZip()
            val emailIntent = Intent(Intent.ACTION_SEND)
            //            emailIntent.setType("vnd.android.cursor.dir/email");
            emailIntent.type = "message/rfc822"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("artmap.help@gmail.com"))
            //                    Uri path = Uri.fromFile(filelocation);
//                    emailIntent .putExtra(Intent.EXTRA_STREAM,  path);
            val file = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                filelocation
            )
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            emailIntent.putExtra(Intent.EXTRA_STREAM, file)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Завдання")
            emailIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(emailIntent, "Відправлення повідомлення…"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    var BUNDLE_ZIP_FILE = "bundle.zip"

    @Throws(IOException::class)
    fun createInfoZip(): File {

        val dir = File(CHAT_DIR)
        val zipFile = File(dir, BUNDLE_ZIP_FILE)
        if (zipFile.exists()) {
            zipFile.delete()
        }
        zipFile.createNewFile()
        val out = ZipOutputStream(FileOutputStream(zipFile))
        out.setLevel(Deflater.DEFAULT_COMPRESSION)
        File(DOWNLOADS_PATH).listFiles()?.let{
            for(file in File(DOWNLOADS_PATH).listFiles()!!) {
                zipFile(out, file)
            }
        }
        File(IMAGES_PATH).listFiles()?.let {
            for (file in File(IMAGES_PATH).listFiles()!!) {
                zipFile(out, file)
            }
        }
        File(IMAGES_TASKS_PATH).listFiles()?.let {
            for (file in File(IMAGES_TASKS_PATH).listFiles()!!) {
                zipFile(out, file)
            }
        }
        out.close()
        return zipFile
    }

    @Throws(IOException::class)
    private fun zipFile(zos: ZipOutputStream, file: File) {
        zos.putNextEntry(ZipEntry(file.name))
        val fis = FileInputStream(file)
        val buffer = ByteArray(4092)
        var byteCount = 0
        try {
            while (fis.read(buffer).also { byteCount = it } != -1) {
                zos.write(buffer, 0, byteCount)
            }
        } finally {
            safeClose(fis)
        }
        zos.closeEntry()
    }

    private fun safeClose(fis: FileInputStream) {
        try {
            fis.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}