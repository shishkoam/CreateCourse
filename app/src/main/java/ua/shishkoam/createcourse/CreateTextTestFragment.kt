package ua.shishkoam.createcourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import org.json.JSONArray
import org.json.JSONObject
import ua.kblogika.interactive.utils.util.FileUtils
import ua.shishkoam.fundamentals.recyclerview.LandingAnimator
import java.io.File
import java.util.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateTextTestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateTextTestFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    var currentItem = 0
    var currentPointOnPicture: TestVariant = TestVariant()
    private val testVariants: MutableList<TestVariant> = ArrayList()
    private var listAdapter: ListDelegationAdapter<List<TestVariant>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_text_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editText = view.findViewById<EditText>(R.id.editText)
        val checkbox = view.findViewById<CheckBox>(R.id.checkbox)
        val resultText: TextView = view.findViewById(R.id.result) as TextView
        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            var hasAlready = false
            for (point in testVariants) {
                if (point.text == editText.text.toString()) {
                    hasAlready = true
                }
            }
            if (hasAlready) {
                return@setOnClickListener
            }
            currentItem++
            val sb = StringBuilder()
            currentPointOnPicture = TestVariant(editText.text.toString(), checkbox.isChecked)
            testVariants.add(currentPointOnPicture)
            listAdapter?.items = testVariants
            listAdapter?.notifyDataSetChanged()
            for (point in testVariants) {
                val prefix = if (point.isRightAnswer) "+" else "-"
                sb.append("$prefix >>> ${point.text}").append("\n")
            }
            resultText.text = sb.toString()
            resultText.visibility = GONE
        }

        view.findViewById<Button>(R.id.save).setOnClickListener {
            savePoints()
        }

        view.findViewById<Button>(R.id.load)?.run {
            visibility = GONE
            setOnClickListener {

            }
        }
        val list: RecyclerView = view.findViewById(R.id.list) as RecyclerView

        listAdapter = createListDelegationAdapter()
        listAdapter?.items = testVariants

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            val landingItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()
            itemAnimator = landingItemAnimator
            adapter = listAdapter
        }
    }

    private fun createListDelegationAdapter(): ListDelegationAdapter<List<TestVariant>> {
        //        val myProgress = DummyContent.STUDENT.courseProgressHashMap
        fun courseAdapterDelegate() =
            adapterDelegate<TestVariant, TestVariant>(R.layout.holder_test_variant_item) {
                val textview: TextView = findViewById(R.id.textview)
                val checkBox: CheckBox = findViewById(R.id.checkbox)
                checkBox.isEnabled = false
                bind {
                    textview.text = item.text
                    checkBox.isChecked = item.isRightAnswer
                }
            }

        val listAdapter = ListDelegationAdapter(
            courseAdapterDelegate()
        )
        return listAdapter
    }


    private fun savePoints() {
        val jsonArray = JSONArray()
        for (testVariant in testVariants) {
            val jsonObject = JSONObject()
            jsonObject.put("isRight", testVariant.isRightAnswer)
            jsonObject.put("text", testVariant.text)
            jsonArray.put(jsonObject)
        }
        val file =
            File(ChatUtils.getImageTasksPath(), "text ${System.currentTimeMillis() / 1000}.txt")
        FileUtils.writeToFile(file, jsonArray.toString())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateTextTestFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateTextTestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}