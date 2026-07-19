package ru.coursecatalog.data.courses.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.coursecatalog.data.courses.local.CourseDao
import ru.coursecatalog.data.courses.mapper.toDomain
import ru.coursecatalog.data.courses.mapper.toEntity
import ru.coursecatalog.data.courses.remote.CourseApi
import ru.coursecatalog.domain.courses.model.Course
import ru.coursecatalog.domain.courses.repository.CourseRepository

internal class DefaultCourseRepository(
    private val courseApi: CourseApi,
    private val courseDao: CourseDao,
    private val ioDispatcher: CoroutineDispatcher,
) : CourseRepository {

    override fun observeCourses(): Flow<List<Course>> =
        courseDao.observeCourses().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun observeFavoriteCourses(): Flow<List<Course>> =
        courseDao.observeFavoriteCourses().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun refreshCourses(): Result<Unit> = runCatching {
        withContext(ioDispatcher) {
            val localFavorites = courseDao.getCourses()
                .associate { it.id to it.isFavorite }
            val remoteCourses = courseApi.getCourses().courses
                .mapIndexed { index, dto ->
                    dto.toEntity(
                        position = index,
                        localFavorite = localFavorites[dto.id],
                    )
                }

            courseDao.replaceRemoteCourses(remoteCourses)
        }
    }

    override suspend fun toggleFavorite(courseId: Long): Result<Unit> = runCatching {
        withContext(ioDispatcher) {
            check(courseDao.toggleFavorite(courseId) == 1) {
                "Course with id=$courseId does not exist"
            }
        }
    }
}
