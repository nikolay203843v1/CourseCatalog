package ru.coursecatalog.feature.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.coursecatalog.domain.courses.model.CourseSortOrder
import ru.coursecatalog.domain.courses.usecase.ObserveCoursesUseCase
import ru.coursecatalog.domain.courses.usecase.RefreshCoursesUseCase
import ru.coursecatalog.domain.courses.usecase.SortCoursesUseCase
import ru.coursecatalog.domain.courses.usecase.ToggleFavoriteUseCase
import ru.coursecatalog.feature.main.model.CourseListUiState

internal class HomeViewModel(
    observeCourses: ObserveCoursesUseCase,
    private val refreshCourses: RefreshCoursesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val sortCourses: SortCoursesUseCase,
) : ViewModel() {
    private val sortOrder = MutableStateFlow(CourseSortOrder.DEFAULT)
    private val loadStatus = MutableStateFlow<LoadStatus>(LoadStatus.Loading)

    val uiState: StateFlow<CourseListUiState> = combine(
        observeCourses(),
        sortOrder,
        loadStatus,
    ) { courses, order, status ->
        CourseListUiState(
            courses = sortCourses(courses, order),
            isLoading = status == LoadStatus.Loading && courses.isEmpty(),
            hasLoadError = status == LoadStatus.Error && courses.isEmpty(),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = CourseListUiState(isLoading = true),
    )

    init {
        refresh()
    }

    fun sortByPublishDate() {
        sortOrder.value = CourseSortOrder.PUBLISH_DATE_DESCENDING
    }

    fun toggleFavorite(courseId: Long) {
        viewModelScope.launch {
            toggleFavorite.invoke(courseId)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            loadStatus.value = LoadStatus.Loading
            loadStatus.value = if (refreshCourses().isSuccess) {
                LoadStatus.Idle
            } else {
                LoadStatus.Error
            }
        }
    }

    private sealed interface LoadStatus {
        data object Loading : LoadStatus
        data object Idle : LoadStatus
        data object Error : LoadStatus
    }
}
