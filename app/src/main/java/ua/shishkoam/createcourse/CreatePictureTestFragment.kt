package ua.shishkoam.createcourse

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.davemorrissey.labs.subscaleview.ImageSource
import org.json.JSONArray
import org.json.JSONObject
import ua.kblogika.interactive.utils.util.FileUtils.copyFile
import ua.kblogika.interactive.utils.util.FileUtils.readFromFile
import ua.kblogika.interactive.utils.util.FileUtils.writeToFile
import ua.shishkoam.createcourse.ChatUtils.IMAGES_PATH
import ua.shishkoam.createcourse.ChatUtils.getImageTasksPath
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreatePictureTestFragment : Fragment() {
    private val args: CreatePictureTestFragmentArgs by navArgs()

    private val REQUEST_CODE = 121
    var currentItem = 0
    var uri: String? = null
    var currentPointOnPicture: PointOnPicture = PointOnPicture()
    private val points: MutableList<PointOnPicture> = ArrayList()
    var imageView: PinView? = null
    var resultText: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editText = view.findViewById<EditText>(R.id.editText)
        imageView = view.findViewById(R.id.imageView) as PinView
        val textview: TextView = view.findViewById(R.id.textview_second) as TextView
        resultText = view.findViewById(R.id.result) as TextView
        if (savedInstanceState == null) {
            val path = args.filePath
            val file = File(path)
            if (path.isNotEmpty() && file.exists()) {
                readPicture(file)
            }
        } else {
            val data = savedInstanceState.getString("data")
            data?.let {
                restoreData(data)
            }
        }

        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            imageView?.savePin()
            points.add(
                PointOnPicture(
                    currentPointOnPicture.x,
                    currentPointOnPicture.y,
                    15,
                    editText.text.toString()
                )
            )
            currentItem++
            currentPointOnPicture = PointOnPicture()
            uri?.let {
                saveImage(it)
                savePoints()
            }
            setResult()
        }
        imageView?.isPanEnabled = true
        imageView?.isZoomEnabled = true
        val gestureDetector =
            GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {

                override fun onLongPress(e: MotionEvent?) {
                    if (imageView?.isReady == true && e != null) {
                        val sCoord = imageView!!.viewToSourceCoord(e.x, e.y)
                        // ...
                        imageView!!.setPin(sCoord!!)
                        textview.text = "X= ${sCoord?.x} Y= ${sCoord?.y}"
                        currentPointOnPicture.x = e.x
                        currentPointOnPicture.y = e.y
                    }
                    super.onLongPress(e)
                }
            })

        view.findViewById<Button>(R.id.load)?.setOnClickListener {
            openGalleryForImage()
            imageView?.clearPin()
        }
        imageView?.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(
                motionEvent
            )
        }
    }

    private fun setResult() {
        val sb = StringBuilder()
        for (point in points) {
            sb.append("${point.name} X= ${point.x} Y = ${point.y}").append("\n")
        }
        resultText?.text = sb.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val uriResult = data?.data
            uriResult?.let {
                uri = getRealPathFromURI(uriResult)
                saveImage(uri!!)
                imageView?.setImage(ImageSource.uri(uri!!))
            }
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun saveImage(tempPhotoUri: String?) {
        val dir = File(IMAGES_PATH)
        dir.mkdirs()
        val sourceFile = File(tempPhotoUri)
        val destinationFile = File(dir, "test img ${sourceFile.name}.jpg")
        copyFile(sourceFile, destinationFile)
    }

    private fun getRealPathFromURI(contentURI: Uri): String {
        val result: String?
        val cursor: Cursor? = requireContext().contentResolver.query(
            contentURI, null,
            null, null, null
        )
        if (cursor == null) { // Source is Dropbox or other similar local file
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            result = try {
                val idx: Int = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                cursor.getString(idx)
            } catch (e: Exception) {
                ""
            }
            cursor.close()
        }
        return result ?: ""
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("data", createJson().toString())
    }

    private fun savePoints() {
        val json = createJson()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH-mm")
        val name = if (uri != null) File(uri!!).name else ""
        val file = File(
            getImageTasksPath(),
            "Зображення " + sdf.format(Date(System.currentTimeMillis())) + " " + name + ".txt"
        )
        writeToFile(file, json.toString())
    }

    private fun createJson(): JSONObject {
        val json = JSONObject()
        val jsonArray = JSONArray()
        for (point in points) {
            val jsonObject = JSONObject()
            jsonObject.put("x", point.x)
            jsonObject.put("y", point.y)
            jsonObject.put("name", point.name)
            jsonObject.put("radius", point.radius)
            jsonArray.put(jsonObject)
        }
        uri?.let {
            json.put("path", it)
        }
        json.put("points", jsonArray)
        return json
    }

    fun readPicture(file: File) {
        val value = readFromFile(file)
        if (!value.isNullOrEmpty()) {
            restoreData(value)
        }
    }

    private fun restoreData(value: String?) {
        val json = JSONObject(value)
        val path = json.getString("path")
        val array = json.getJSONArray("points")
        val points: MutableList<PointOnPicture> = ArrayList()
        for (i in 0 until array.length()) {
            val data = array.getJSONObject(i)
            points.add(
                PointOnPicture(
                    data.getDouble("x").toFloat(),
                    data.getDouble("y").toFloat(),
                    data.getInt("radius"),
                    data.getString("name")
                )
            )
        }
        this.points.clear()
        this.points.addAll(points)
        uri = path
        restoreUi()
    }

    private fun restoreUi() {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap = BitmapFactory.decodeFile(uri, options)
        imageView?.setImage(ImageSource.bitmap(bitmap))
        val pointList: MutableList<PointF> = ArrayList()
        for (point in points) {
            if (point.x != null && point.y != null) {
                pointList.add(PointF(point.x!!.toFloat(), point.y!!.toFloat()))
            }
        }
        imageView?.setAndSavePins(pointList)
        setResult()
    }
}