package ru.coursecatalog.domain.courses.usecase

import kotlinx.coroutines.flow.Flow
import ru.coursecatalog.domain.courses.model.Course
import ru.coursecatalog.domain.courses.repository.CourseRepository

class ObserveFavoriteCoursesUseCase(
    private val repository: CourseRepository,
) {
    operator fun invoke(): Flow<List<Course>> = repository.observeFavoriteCourses()
}
