package ru.coursecatalog.data.courses.mapper

import java.time.LocalDate
import ru.coursecatalog.data.courses.local.CourseEntity
import ru.coursecatalog.data.courses.remote.CourseDto
import ru.coursecatalog.domain.courses.model.Course

internal fun CourseEntity.toDomain(): Course = Course(
    id = id,
    title = title,
    description = description,
    price = price,
    rating = rating,
    startDate = LocalDate.parse(startDate),
    isFavorite = isFavorite,
    publishDate = LocalDate.parse(publishDate),
)

internal fun CourseDto.toEntity(
    position: Int,
    localFavorite: Boolean?,
): CourseEntity = CourseEntity(
    id = id,
    position = position,
    title = title,
    description = text,
    price = price,
    rating = rate.toDouble(),
    startDate = startDate,
    isFavorite = localFavorite ?: hasLike,
    publishDate = publishDate,
)
