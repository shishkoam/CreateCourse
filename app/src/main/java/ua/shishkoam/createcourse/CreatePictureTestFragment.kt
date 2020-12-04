package ua.shishkoam.createcourse

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.davemorrissey.labs.subscaleview.ImageSource
import org.json.JSONArray
import org.json.JSONObject
import ua.kblogika.interactive.utils.util.FileUtils.copyFile
import ua.kblogika.interactive.utils.util.FileUtils.writeToFile
import ua.shishkoam.createcourse.ChatUtils.IMAGES_PATH
import ua.shishkoam.createcourse.ChatUtils.getImageTasksPath
import java.io.File
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreatePictureTestFragment : Fragment() {

    private val REQUEST_CODE = 121
    var currentItem = 0
    var uri: Uri? = null
    var currentPointOnPicture: PointOnPicture = PointOnPicture()
    private val points: MutableList<PointOnPicture> = ArrayList()
    var imageView: PinView? = null

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
        val resultText: TextView = view.findViewById(R.id.result) as TextView
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
            val sb = StringBuilder()
            for (point in points) {
                sb.append("${point.name} X= ${point.x} Y = ${point.y}").append("\n")
            }
            resultText.text = sb.toString()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            uri = data?.data
            uri?.let {
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

    private fun saveImage(tempPhotoUri: Uri) {
        val dir = File(IMAGES_PATH)
        dir.mkdirs()
        val destinationFile = File(dir, "test img ${tempPhotoUri.lastPathSegment}.jpg")
        val sourceFile = File(getRealPathFromURI(tempPhotoUri))
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

    private fun savePoints() {
        val jsonArray = JSONArray()
        for (point in points) {
            val jsonObject = JSONObject()
            jsonObject.put("x", point.x)
            jsonObject.put("y", point.y)
            jsonObject.put("name", point.name)
            jsonObject.put("radius", point.radius)
            jsonArray.put(jsonObject)
        }
        val file = File(getImageTasksPath(), uri?.lastPathSegment ?: "no" + ".txt")
        writeToFile(file, jsonArray.toString())
    }
}