package ru.coursecatalog.domain.courses.repository

import kotlinx.coroutines.flow.Flow
import ru.coursecatalog.domain.courses.model.Course

interface CourseRepository {
    fun observeCourses(): Flow<List<Course>>

    fun observeFavoriteCourses(): Flow<List<Course>>

    suspend fun refreshCourses(): Result<Unit>

    suspend fun toggleFavorite(courseId: Long): Result<Unit>
}
