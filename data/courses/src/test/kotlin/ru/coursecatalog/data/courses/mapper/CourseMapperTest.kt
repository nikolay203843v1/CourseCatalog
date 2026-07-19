package ru.coursecatalog.data.courses.mapper

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.coursecatalog.data.courses.remote.CourseDto

class CourseMapperTest {
    @Test
    fun `server favorite is used when local value is absent`() {
        val entity = courseDto(hasLike = true).toEntity(
            position = 0,
            localFavorite = null,
        )

        assertTrue(entity.isFavorite)
    }

    @Test
    fun `local favorite has priority over server value`() {
        val entity = courseDto(hasLike = true).toEntity(
            position = 0,
            localFavorite = false,
        )

        assertFalse(entity.isFavorite)
    }

    private fun courseDto(hasLike: Boolean) = CourseDto(
        id = 100,
        title = "Java-разработчик с нуля",
        text = "Описание",
        price = "999",
        rate = "4.9",
        startDate = "2024-05-22",
        hasLike = hasLike,
        publishDate = "2024-02-02",
    )
}
