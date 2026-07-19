package ru.coursecatalog.feature.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted

internal class LoginViewModel(
    private val loginValidator: LoginValidator,
) : ViewModel() {
    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")

    val uiState: StateFlow<LoginUiState> = combine(email, password) { email, password ->
        LoginUiState(canLogIn = loginValidator.canLogIn(email.trim(), password))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = LoginUiState(),
    )

    fun onEmailChanged(value: String) {
        email.value = value
    }

    fun onPasswordChanged(value: String) {
        password.value = value
    }
}

internal data class LoginUiState(
    val canLogIn: Boolean = false,
)
