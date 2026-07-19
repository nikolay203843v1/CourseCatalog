package ru.coursecatalog.feature.main.course

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import java.time.format.DateTimeFormatter
import java.util.Locale
import ru.coursecatalog.core.ui.R as CoreUiR
import ru.coursecatalog.domain.courses.model.Course
import ru.coursecatalog.feature.main.R
import ru.coursecatalog.feature.main.databinding.ItemCourseBinding

internal class CourseListAdapter(
    onFavoriteClick: (Long) -> Unit,
) : ListDelegationAdapter<List<Course>>(
    courseAdapterDelegate(onFavoriteClick),
) {
    init {
        items = emptyList()
    }

    fun submitCourses(courses: List<Course>) {
        val oldCourses = items.orEmpty()
        val diffResult = DiffUtil.calculateDiff(CourseDiffCallback(oldCourses, courses))
        items = courses
        diffResult.dispatchUpdatesTo(this)
    }
}

private class CourseDiffCallback(
    private val oldItems: List<Course>,
    private val newItems: List<Course>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition].id == newItems[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition] == newItems[newItemPosition]
}

private fun courseAdapterDelegate(
    onFavoriteClick: (Long) -> Unit,
) = adapterDelegateViewBinding<Course, Course, ItemCourseBinding>(
    viewBinding = { inflater, parent ->
        ItemCourseBinding.inflate(inflater, parent, false)
    },
) {
    bind {
        binding.courseImage.setImageResource(item.imageResource())
        binding.titleText.text = item.title
        binding.descriptionText.text = item.description
        binding.priceText.text = binding.root.context.getString(R.string.price_format, item.price)
        binding.ratingText.text = String.format(Locale.US, "%.1f", item.rating)
        binding.dateText.text = item.startDate
            .format(COURSE_DATE_FORMATTER)
            .replaceFirstChar { firstCharacter ->
                if (firstCharacter.isLowerCase()) {
                    firstCharacter.titlecase(COURSE_DATE_LOCALE)
                } else {
                    firstCharacter.toString()
                }
            }

        val bookmarkIcon = if (item.isFavorite) {
            CoreUiR.drawable.ic_bookmark_filled
        } else {
            CoreUiR.drawable.ic_bookmark
        }
        binding.bookmarkButton.setImageResource(bookmarkIcon)
        binding.bookmarkButton.imageTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                binding.root.context,
                if (item.isFavorite) CoreUiR.color.cc_primary else CoreUiR.color.cc_on_surface,
            ),
        )
        binding.bookmarkButton.contentDescription = binding.root.context.getString(
            if (item.isFavorite) {
                R.string.remove_from_favorites
            } else {
                R.string.add_to_favorites
            },
        )
        binding.bookmarkButton.setOnClickListener { onFavoriteClick(item.id) }
    }
}

private fun Course.imageResource(): Int = when (id) {
    100L -> R.drawable.course_java_banner
    101L -> R.drawable.course_3d_banner
    102L -> R.drawable.course_python_banner
    103L -> R.drawable.course_system_analytics_banner
    104L -> R.drawable.course_data_analytics_banner
    else -> R.drawable.course_fallback
}

private val COURSE_DATE_LOCALE = Locale.forLanguageTag("ru-RU")
private val COURSE_DATE_FORMATTER =
    DateTimeFormatter.ofPattern("d MMMM yyyy", COURSE_DATE_LOCALE)
