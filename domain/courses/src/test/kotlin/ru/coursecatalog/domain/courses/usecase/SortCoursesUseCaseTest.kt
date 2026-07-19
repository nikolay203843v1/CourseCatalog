package ru.coursecatalog.domain.courses.usecase

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.coursecatalog.domain.courses.model.Course
import ru.coursecatalog.domain.courses.model.CourseSortOrder

class SortCoursesUseCaseTest {
    private val sortCourses = SortCoursesUseCase()

    @Test
    fun `default order is preserved`() {
        val courses = listOf(
            course(id = 1, publishDate = "2024-01-01"),
            course(id = 2, publishDate = "2024-03-01"),
        )

        val result = sortCourses(courses, CourseSortOrder.DEFAULT)

        assertEquals(listOf(1L, 2L), result.map(Course::id))
    }

    @Test
    fun `publish date order is descending`() {
        val courses = listOf(
            course(id = 1, publishDate = "2024-01-01"),
            course(id = 2, publishDate = "2024-03-01"),
            course(id = 3, publishDate = "2024-02-01"),
        )

        val result = sortCourses(courses, CourseSortOrder.PUBLISH_DATE_DESCENDING)

        assertEquals(listOf(2L, 3L, 1L), result.map(Course::id))
    }

    private fun course(id: Long, publishDate: String) = Course(
        id = id,
        title = "Course $id",
        description = "Description",
        price = "999",
        rating = 4.5,
        startDate = LocalDate.parse("2024-05-01"),
        isFavorite = false,
        publishDate = LocalDate.parse(publishDate),
    )
}
