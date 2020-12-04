package ua.kblogika.interactive.courses

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import ua.kblogika.interactive.base.presentation.BaseFragment
import ua.kblogika.interactive.courses.objects.education.Lesson
import ua.kblogika.interactive.utils.interactors.ImageLoader
import ua.kblogika.interactive.utils.util.CollectionUtils
import ua.shishkoam.createcourse.R
import ua.shishkoam.fundamentals.recyclerview.GridAutofitLayoutManager
import ua.shishkoam.fundamentals.recyclerview.LandingAnimator

/**
 * A fragment representing a list of Items.
 */
class LessonListFragment : BaseFragment(R.layout.fragment_lesson_list_list) {
    private val likedLessons: HashMap<String?, Boolean> = HashMap()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            val savedData =
                CollectionUtils.fromBundleBooleanMap(savedInstanceState.getBundle("likes"))
            savedData?.let {
                likedLessons.putAll(savedData)
            }
        }
        val orientation = this.resources.configuration.orientation
        val filmRecyclerViewManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridAutofitLayoutManager(requireContext(), GridAutofitLayoutManager.AUTO_FIT, 320f)
        } else {
            LinearLayoutManager(requireContext())
        }
        val listAdapter = createListDelegationAdapter()
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = filmRecyclerViewManager
                setHasFixedSize(true)
                val landingItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()
                itemAnimator = landingItemAnimator
                adapter = listAdapter
            }
        }
    }

    private fun createListDelegationAdapter(): ListDelegationAdapter<List<Lesson>> {
        fun setLikeColor(
            isChecked: Boolean,
            likeImageView: ImageButton,
            context: Context
        ) {
            if (isChecked){
                likeImageView.setImageDrawable(ContextCompat.getDrawable(context.applicationContext,android.R.drawable.btn_star_big_on));
            }else{
                likeImageView.setImageDrawable(ContextCompat.getDrawable(context.applicationContext,android.R.drawable.btn_star_big_off));
            }
        }

//        val myProgress = DummyContent.STUDENT.courseProgressHashMap
        fun courseAdapterDelegate() =
            adapterDelegate<Lesson, Lesson>(R.layout.view_holder_lessons) {
                val name: TextView = findViewById(R.id.name_text)
                val description: TextView = findViewById(R.id.genre_text)
                val percent: TextView = findViewById(R.id.percent_text)
                val photo: ImageView = findViewById(R.id.photo_image)
                val likeImageView: ImageButton = findViewById(R.id.likeImageView)
                likeImageView.setOnClickListener { // Triggers click upwards to the adapter on click
                    val likedState = likedLessons[item] ?: false
                    likedLessons[item.courseId] = !likedState
                    setLikeColor(!likedState, likeImageView, context)
                }
                itemView.setOnClickListener {
//                    val action =
//                            LessonListFragmentDirections.actionOpenTasks()
//                        findNavController().navigate(action)
                    }
                bind {
                    name.text = item.name
                    description.text = item.info
                    percent.text = "50%"
                    ImageLoader.loadImage(photo, item.image)
                    setLikeColor(likedLessons[item.courseId] == true, likeImageView, context)
                }
            }

        val listAdapter = ListDelegationAdapter(
            courseAdapterDelegate()
        )
//        listAdapter.items = DummyContent.LESSONS
        return listAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("likes", CollectionUtils.toBundleBooleanMap(likedLessons))
    }


}