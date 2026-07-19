package ru.coursecatalog.data.courses.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CourseEntity::class],
    version = 1,
    exportSchema = true,
)
internal abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}
