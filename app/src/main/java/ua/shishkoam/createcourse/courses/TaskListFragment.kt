package ua.kblogika.interactive.courses

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateViewHolder
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import ua.kblogika.interactive.base.presentation.BaseFragment
import ua.kblogika.interactive.courses.objects.education.PictureTask
import ua.kblogika.interactive.courses.objects.education.Task
import ua.kblogika.interactive.courses.objects.education.TestTask
import ua.kblogika.interactive.courses.objects.education.TextTask
import ua.kblogika.interactive.utils.interactors.ImageLoader
import ua.shishkoam.createcourse.R
import ua.shishkoam.fundamentals.recyclerview.LandingAnimator

class TaskListFragment : BaseFragment(R.layout.fragment_task_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
//            val savedData =
//                CollectionUtils.fromBundleBooleanMap(savedInstanceState.getBundle("likes"))
//            savedData?.let {
//                likedLessons.putAll(savedData)
//            }
        }
//        val orientation = this.resources.configuration.orientation
        val filmRecyclerViewManager =
            LinearLayoutManager(requireContext())
        val listAdapter = createListDelegationAdapter()
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
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
                val description: TextView = findViewById(R.id.description_text)
                val photo: ImageView = findViewById(R.id.photo_image)
                bind {
                    name.text = item.name
                    description.text = item.info
                    ImageLoader.loadImage(photo, item.picturePath)
                }
            }

        fun AdapterDelegateViewHolder<TestTask>.setupRadioButton(
            radioButton1: RadioButton, size: Int, pos: Int, answerVariants: List<String>
        ) {
            if (size > pos) {
                radioButton1.text = answerVariants[pos]
                radioButton1.visibility = VISIBLE
            } else {
                radioButton1.visibility = GONE
            }
        }

        fun testTaskAdapterDelegate() =
            adapterDelegate<TestTask, Task>(R.layout.view_holder_test_task) {
                val name: TextView = findViewById(R.id.name_text)
                val description: TextView = findViewById(R.id.description_text)
                val radioButton1: RadioButton = findViewById(R.id.radia_id1)
                val radioButton2: RadioButton = findViewById(R.id.radia_id2)
                val radioButton3: RadioButton = findViewById(R.id.radia_id3)
                val radioButton4: RadioButton = findViewById(R.id.radia_id4)
                val radioButton5: RadioButton = findViewById(R.id.radia_id5)
                val button: Button = findViewById(R.id.send)

                bind {
                    name.text = item.name
                    description.text = item.info
                    item.answerVariants?.let {
                        val list = item.answerVariants!!
                        val size = list.size
                        setupRadioButton(radioButton1, size, 0, list)
                        setupRadioButton(radioButton2, size, 1, list)
                        setupRadioButton(radioButton3, size, 2, list)
                        setupRadioButton(radioButton4, size, 3, list)
                        setupRadioButton(radioButton5, size, 4, list)
                    }
                    button.setOnClickListener {

                    }
                }
            }

        fun textTaskAdapterDelegate() =
            adapterDelegate<TextTask, Task>(R.layout.view_holder_text_task) {
                val name: TextView = findViewById(R.id.name_text)
                val description: TextView = findViewById(R.id.description_text)
                bind {
                    name.text = item.name
                    description.text = item.info
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putParcelable("likes", CollectionUtils.toBundleBooleanMap(likedLessons))
    }


}