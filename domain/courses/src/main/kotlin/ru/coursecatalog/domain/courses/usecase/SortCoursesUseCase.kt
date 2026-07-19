package ru.coursecatalog.domain.courses.usecase

import ru.coursecatalog.domain.courses.model.Course
import ru.coursecatalog.domain.courses.model.CourseSortOrder

class SortCoursesUseCase {
    operator fun invoke(
        courses: List<Course>,
        sortOrder: CourseSortOrder,
    ): List<Course> = when (sortOrder) {
        CourseSortOrder.DEFAULT -> courses
        CourseSortOrder.PUBLISH_DATE_DESCENDING ->
            courses.sortedByDescending(Course::publishDate)
    }
}
