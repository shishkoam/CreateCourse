package ua.shishkoam.createcourse.courses

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import ua.kblogika.interactive.base.presentation.BaseFragment
import ua.kblogika.interactive.courses.objects.education.Course
import ua.kblogika.interactive.utils.interactors.ImageLoader
import ua.shishkoam.createcourse.R
import ua.shishkoam.fundamentals.recyclerview.GridAutofitLayoutManager
import ua.shishkoam.fundamentals.recyclerview.LandingAnimator

/**
 * A fragment representing a list of Items.
 */
class CoursesFragment : BaseFragment(R.layout.fragment_courses_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun createListDelegationAdapter(): ListDelegationAdapter<List<Course>> {
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

        fun courseAdapterDelegate() =
            adapterDelegate<Course, Course>(R.layout.view_holder_courses) {
                val name: TextView = findViewById(R.id.name_text)
                val description: TextView = findViewById(R.id.genre_text)
                val progress: ProgressBar = findViewById(R.id.progress_bar)
                val percent: TextView = findViewById(R.id.percent_text)
                val photo: ImageView = findViewById(R.id.photo_image)
                val likeImageView: ImageButton = findViewById(R.id.likeImageView)
                itemView.setOnClickListener {
                    if (item.lessons.isNullOrEmpty()) {
//                        "Курс у розробці"
                    } else {
//                        val list = ParcelableStringList(item.lessons!!)
//                        val action =
//                            CoursesFragmentDirections.actionOpenLessonsList(list)
//                        findNavController().navigate(action)
                    }
                } // Item is automatically set for you. It's set lazily though (set in onBindViewHolder()). So only use it for deferred calls like clickListeners.

                bind {
                    name.text = item.name
                    description.text = item.info
                    val result = 50
                    progress.progress = result
                    percent.text = "$result%"
                    ImageLoader.loadImage(photo, item.image)
                }
            }

        val listAdapter = ListDelegationAdapter(
            courseAdapterDelegate()
        )
//        listAdapter.items = DummyContent.COURSES
        return listAdapter
    }

}