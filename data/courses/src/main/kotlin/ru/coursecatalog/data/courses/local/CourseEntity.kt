package ru.coursecatalog.data.courses.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
internal data class CourseEntity(
    @PrimaryKey val id: Long,
    val position: Int,
    val title: String,
    val description: String,
    val price: String,
    val rating: Double,
    val startDate: String,
    val isFavorite: Boolean,
    val publishDate: String,
)
