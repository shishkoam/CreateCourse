package ua.shishkoam.createcourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import ua.kblogika.interactive.courses.objects.education.PictureTask
import ua.kblogika.interactive.courses.objects.education.Task
import ua.kblogika.interactive.courses.objects.education.TestTask
import ua.kblogika.interactive.courses.objects.education.TextTask
import ua.shishkoam.fundamentals.recyclerview.LandingAnimator
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.create_text_test).setOnClickListener {
            findNavController().navigate(R.id.action_OpenTextTest)
        }
        view.findViewById<Button>(R.id.create_picture_test).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        val filmRecyclerViewManager =
            LinearLayoutManager(requireContext())
        val listAdapter = createListDelegationAdapter()
        // Set the adapter
        val listValues: MutableList<Task> = ArrayList()
        File(ChatUtils.IMAGES_TASKS_PATH).listFiles()?.let {
            for (file in File(ChatUtils.IMAGES_TASKS_PATH).listFiles()!!) {
                val name = file.nameWithoutExtension
                if (name.contains("Зображення")) {
                    val textTask = PictureTask()
                    textTask.name = name
                    textTask.info = file.absolutePath
                    listValues.add(textTask)
                } else if (name.contains("Тест")) {
                    val textTask = TestTask()
                    textTask.name = name
                    textTask.info = file.absolutePath
                    listValues.add(textTask)
                }
            }
        }
        listAdapter.items = listValues
        val list = view.findViewById<RecyclerView>(R.id.list)
        if (list is RecyclerView) {
            with(list) {
                layoutManager = filmRecyclerViewManager
                val landingItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()
                itemAnimator = landingItemAnimator
                adapter = listAdapter
            }
        }
    }

    private fun createListDelegationAdapter(): ListDelegationAdapter<List<Task>> {
        fun pictureTaskAdapterDelegate() =
            adapterDelegate<PictureTask, Task>(R.layout.view_holder_pricture_task) {
                val name: TextView = findViewById(R.id.name_text)
                val photo: ImageView = findViewById(R.id.photo_image)
                itemView.setOnClickListener {
                    val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(item.info ?: "")
                    findNavController().navigate(action)
                }
                bind {
                    name.text = item.name
                }
            }

        fun testTaskAdapterDelegate() =
            adapterDelegate<TestTask, Task>(R.layout.view_holder_test_task) {
                val name: TextView = findViewById(R.id.name_text)
                val button: Button = findViewById(R.id.send)
                val group: RadioGroup = findViewById(R.id.groupradio)
                itemView.setOnClickListener {
                    val action = FirstFragmentDirections.actionOpenTextTest(item.info ?: "")
                    findNavController().navigate(action)
                }
                bind {
                    name.text = item.name
                    item.answerVariants?.let {
                        val list = item.answerVariants!!
                        val size = list.size
                    }
                    group.visibility = GONE
                    button.visibility = GONE
                    button.setOnClickListener {

                    }
                }
            }

        fun textTaskAdapterDelegate() =
            adapterDelegate<TextTask, Task>(R.layout.view_holder_text_task) {
                val name: TextView = findViewById(R.id.name_text)
                bind {
                    name.text = item.name
                }
            }


        val listAdapter = ListDelegationAdapter(
            pictureTaskAdapterDelegate(),
            textTaskAdapterDelegate(),
            testTaskAdapterDelegate()
        )
//        listAdapter.items = DummyContent.TASKS
        return listAdapter
    }

}