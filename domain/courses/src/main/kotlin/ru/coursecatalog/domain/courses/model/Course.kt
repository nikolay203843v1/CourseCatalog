package ru.coursecatalog.domain.courses.model

import java.time.LocalDate

data class Course(
    val id: Long,
    val title: String,
    val description: String,
    val price: String,
    val rating: Double,
    val startDate: LocalDate,
    val isFavorite: Boolean,
    val publishDate: LocalDate,
)
