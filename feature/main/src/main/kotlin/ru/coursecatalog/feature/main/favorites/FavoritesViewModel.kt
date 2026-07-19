package ru.coursecatalog.feature.main.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.coursecatalog.domain.courses.usecase.ObserveFavoriteCoursesUseCase
import ru.coursecatalog.domain.courses.usecase.ToggleFavoriteUseCase
import ru.coursecatalog.feature.main.model.CourseListUiState

internal class FavoritesViewModel(
    observeFavoriteCourses: ObserveFavoriteCoursesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {
    val uiState: StateFlow<CourseListUiState> = observeFavoriteCourses()
        .map { CourseListUiState(courses = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = CourseListUiState(),
        )

    fun toggleFavorite(courseId: Long) {
        viewModelScope.launch {
            toggleFavorite.invoke(courseId)
        }
    }
}
