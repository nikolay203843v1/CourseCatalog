package ru.coursecatalog.data.courses.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CourseDao {
    @Query("SELECT * FROM courses ORDER BY position")
    fun observeCourses(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE isFavorite = 1 ORDER BY position")
    fun observeFavoriteCourses(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses")
    suspend fun getCourses(): List<CourseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCourses(courses: List<CourseEntity>)

    @Query("DELETE FROM courses WHERE id NOT IN (:courseIds)")
    suspend fun deleteCoursesNotIn(courseIds: List<Long>)

    @Query(
        """
        UPDATE courses
        SET isFavorite = CASE WHEN isFavorite = 1 THEN 0 ELSE 1 END
        WHERE id = :courseId
        """,
    )
    suspend fun toggleFavorite(courseId: Long): Int

    @Transaction
    suspend fun replaceRemoteCourses(courses: List<CourseEntity>) {
        upsertCourses(courses)
        deleteCoursesNotIn(courses.map(CourseEntity::id))
    }
}
