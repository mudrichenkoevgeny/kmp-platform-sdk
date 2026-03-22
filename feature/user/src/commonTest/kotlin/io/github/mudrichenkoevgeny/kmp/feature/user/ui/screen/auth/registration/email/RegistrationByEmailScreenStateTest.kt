package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.registration.email

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RegistrationByEmailScreenStateTest {

    @Test
    fun registrationInput_isCodeFullLength_respectsCodeLength() {
        assertTrue(
            RegistrationByEmailScreenState.RegistrationInput(
                email = EMAIL,
                code = CODE_SIX_CHARS,
                codeLength = CODE_LENGTH_SIX
            ).isCodeFullLength
        )
        assertFalse(
            RegistrationByEmailScreenState.RegistrationInput(
                email = EMAIL,
                code = CODE_FIVE_CHARS,
                codeLength = CODE_LENGTH_SIX
            ).isCodeFullLength
        )
    }

    @Test
    fun registrationInput_canResendCode_whenTimerZeroAndNotLoading() {
        assertTrue(
            baseRegistrationInput(
                resendTimerSeconds = RESEND_TIMER_READY,
                actionLoading = NOT_LOADING
            ).canResendCode
        )
        assertFalse(
            baseRegistrationInput(
                resendTimerSeconds = RESEND_TIMER_BLOCKING,
                actionLoading = NOT_LOADING
            ).canResendCode
        )
        assertFalse(
            baseRegistrationInput(
                resendTimerSeconds = RESEND_TIMER_READY,
                actionLoading = LOADING
            ).canResendCode
        )
    }

    @Test
    fun registrationInput_canRegister_requiresFullCodeValidPasswordAndNotLoading() {
        assertTrue(
            RegistrationByEmailScreenState.RegistrationInput(
                email = EMAIL,
                code = CODE_SIX_CHARS,
                codeLength = CODE_LENGTH_SIX,
                isPasswordValid = PASSWORD_VALID,
                actionLoading = NOT_LOADING
            ).canRegister
        )
    }

    @Test
    fun registrationInput_canRegister_falseWhenCodeIncomplete() {
        assertFalse(
            RegistrationByEmailScreenState.RegistrationInput(
                email = EMAIL,
                code = CODE_THREE_CHARS,
                codeLength = CODE_LENGTH_SIX,
                isPasswordValid = PASSWORD_VALID,
                actionLoading = NOT_LOADING
            ).canRegister
        )
    }

    @Test
    fun registrationInput_canRegister_falseWhenPasswordInvalid() {
        assertFalse(
            RegistrationByEmailScreenState.RegistrationInput(
                email = EMAIL,
                code = CODE_SIX_CHARS,
                isPasswordValid = PASSWORD_INVALID,
                actionLoading = NOT_LOADING
            ).canRegister
        )
    }

    @Test
    fun registrationInput_canRegister_falseWhenLoading() {
        assertFalse(
            RegistrationByEmailScreenState.RegistrationInput(
                email = EMAIL,
                code = CODE_SIX_CHARS,
                isPasswordValid = PASSWORD_VALID,
                actionLoading = LOADING
            ).canRegister
        )
    }

    private fun baseRegistrationInput(
        resendTimerSeconds: Int,
        actionLoading: Boolean
    ): RegistrationByEmailScreenState.RegistrationInput =
        RegistrationByEmailScreenState.RegistrationInput(
            email = EMAIL,
            resendTimerSeconds = resendTimerSeconds,
            actionLoading = actionLoading
        )

    private companion object {
        const val EMAIL = "user@example.com"
        const val CODE_SIX_CHARS = "123456"
        const val CODE_FIVE_CHARS = "12345"
        const val CODE_THREE_CHARS = "123"
        const val CODE_LENGTH_SIX = 6
        const val RESEND_TIMER_READY = 0
        const val RESEND_TIMER_BLOCKING = 10
        const val PASSWORD_VALID = true
        const val PASSWORD_INVALID = false
        const val LOADING = true
        const val NOT_LOADING = false
    }
}
