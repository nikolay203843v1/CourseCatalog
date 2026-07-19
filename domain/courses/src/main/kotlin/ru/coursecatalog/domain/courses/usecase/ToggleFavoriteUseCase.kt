package ru.coursecatalog.domain.courses.usecase

import ru.coursecatalog.domain.courses.repository.CourseRepository

class ToggleFavoriteUseCase(
    private val repository: CourseRepository,
) {
    suspend operator fun invoke(courseId: Long): Result<Unit> =
        repository.toggleFavorite(courseId)
}
