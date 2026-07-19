package ru.coursecatalog.feature.auth

internal class LoginValidator {
    fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email)

    fun canLogIn(email: String, password: String): Boolean =
        isValidEmail(email) && password.isNotEmpty()

    private companion object {
        val EMAIL_REGEX = Regex(
            pattern = "^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@" +
                "[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?" +
                "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)+$",
        )
    }
}
