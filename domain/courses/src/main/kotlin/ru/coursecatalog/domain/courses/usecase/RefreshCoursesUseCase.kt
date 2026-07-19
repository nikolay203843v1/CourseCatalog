package ru.coursecatalog.domain.courses.usecase

import ru.coursecatalog.domain.courses.repository.CourseRepository

class RefreshCoursesUseCase(
    private val repository: CourseRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.refreshCourses()
}
