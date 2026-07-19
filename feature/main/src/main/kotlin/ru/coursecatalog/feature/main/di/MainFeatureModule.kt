package ru.coursecatalog.feature.main.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.coursecatalog.domain.courses.usecase.ObserveCoursesUseCase
import ru.coursecatalog.domain.courses.usecase.ObserveFavoriteCoursesUseCase
import ru.coursecatalog.domain.courses.usecase.RefreshCoursesUseCase
import ru.coursecatalog.domain.courses.usecase.SortCoursesUseCase
import ru.coursecatalog.domain.courses.usecase.ToggleFavoriteUseCase
import ru.coursecatalog.feature.main.favorites.FavoritesViewModel
import ru.coursecatalog.feature.main.home.HomeViewModel

val mainFeatureModule = module {
    factory { ObserveCoursesUseCase(repository = get()) }
    factory { ObserveFavoriteCoursesUseCase(repository = get()) }
    factory { RefreshCoursesUseCase(repository = get()) }
    factory { ToggleFavoriteUseCase(repository = get()) }
    factory { SortCoursesUseCase() }

    viewModel {
        HomeViewModel(
            observeCourses = get(),
            refreshCourses = get(),
            toggleFavorite = get(),
            sortCourses = get(),
        )
    }
    viewModel {
        FavoritesViewModel(
            observeFavoriteCourses = get(),
            toggleFavorite = get(),
        )
    }
}
