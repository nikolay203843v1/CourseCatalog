package ru.coursecatalog.feature.auth.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.coursecatalog.feature.auth.LoginValidator
import ru.coursecatalog.feature.auth.LoginViewModel

val authModule = module {
    factory { LoginValidator() }
    viewModel { LoginViewModel(loginValidator = get()) }
}
