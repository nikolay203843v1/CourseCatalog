package ru.coursecatalog.feature.auth

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginValidatorTest {
    private val validator = LoginValidator()

    @Test
    fun `valid email and non-empty password allow login`() {
        assertTrue(validator.canLogIn("developer@example.com", "password"))
    }

    @Test
    fun `email without domain zone is invalid`() {
        assertFalse(validator.canLogIn("developer@example", "password"))
    }

    @Test
    fun `cyrillic email is invalid`() {
        assertFalse(validator.canLogIn("разработчик@example.com", "password"))
    }

    @Test
    fun `empty password prevents login`() {
        assertFalse(validator.canLogIn("developer@example.com", ""))
    }
}
