package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoginByEmailScreenStateTest {

    @Test
    fun content_canLogin_requiresValidEmailPasswordAndNotLoading() {
        assertTrue(
            LoginByEmailScreenState.Content(
                isEmailValid = EMAIL_VALID,
                isPasswordValid = PASSWORD_VALID,
                actionLoading = NOT_LOADING
            ).canLogin
        )
    }

    @Test
    fun content_canLogin_falseWhenEmailInvalid() {
        assertFalse(
            LoginByEmailScreenState.Content(
                isEmailValid = EMAIL_INVALID,
                isPasswordValid = PASSWORD_VALID,
                actionLoading = NOT_LOADING
            ).canLogin
        )
    }

    @Test
    fun content_canLogin_falseWhenPasswordInvalid() {
        assertFalse(
            LoginByEmailScreenState.Content(
                isEmailValid = EMAIL_VALID,
                isPasswordValid = PASSWORD_INVALID,
                actionLoading = NOT_LOADING
            ).canLogin
        )
    }

    @Test
    fun content_canLogin_falseWhenLoading() {
        assertFalse(
            LoginByEmailScreenState.Content(
                isEmailValid = EMAIL_VALID,
                isPasswordValid = PASSWORD_VALID,
                actionLoading = LOADING
            ).canLogin
        )
    }

    private companion object {
        const val EMAIL_VALID = true
        const val EMAIL_INVALID = false
        const val PASSWORD_VALID = true
        const val PASSWORD_INVALID = false
        const val LOADING = true
        const val NOT_LOADING = false
    }
}
