package ru.coursecatalog.feature.main.model

import ru.coursecatalog.domain.courses.model.Course

internal data class CourseListUiState(
    val courses: List<Course> = emptyList(),
    val isLoading: Boolean = false,
    val hasLoadError: Boolean = false,
)
