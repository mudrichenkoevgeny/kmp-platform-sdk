package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.password

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ResetEmailPasswordScreenStateTest {

    @Test
    fun resetInput_isCodeFullLength_respectsCodeLength() {
        assertTrue(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL,
                code = CODE_SIX_CHARS,
                codeLength = CODE_LENGTH_SIX
            ).isCodeFullLength
        )
        assertFalse(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL,
                code = CODE_FIVE_CHARS,
                codeLength = CODE_LENGTH_SIX
            ).isCodeFullLength
        )
    }

    @Test
    fun resetInput_canConfirm_requiresFullCodeValidPasswordAndNotLoading() {
        assertTrue(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL,
                code = CODE_SIX_CHARS,
                newPassword = NEW_PASSWORD,
                isPasswordValid = PASSWORD_VALID,
                actionLoading = NOT_LOADING
            ).canConfirm
        )
    }

    @Test
    fun resetInput_canConfirm_falseWhenCodeIncomplete() {
        assertFalse(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL,
                code = CODE_TWO_CHARS,
                isPasswordValid = PASSWORD_VALID,
                actionLoading = NOT_LOADING
            ).canConfirm
        )
    }

    @Test
    fun resetInput_canConfirm_falseWhenPasswordInvalid() {
        assertFalse(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL,
                code = CODE_SIX_CHARS,
                isPasswordValid = PASSWORD_INVALID,
                actionLoading = NOT_LOADING
            ).canConfirm
        )
    }

    @Test
    fun resetInput_canConfirm_falseWhenLoading() {
        assertFalse(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL,
                code = CODE_SIX_CHARS,
                isPasswordValid = PASSWORD_VALID,
                actionLoading = LOADING
            ).canConfirm
        )
    }

    @Test
    fun resetInput_canResendCode_whenTimerZeroAndNotLoading() {
        assertTrue(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL,
                resendTimerSeconds = RESEND_TIMER_READY,
                actionLoading = NOT_LOADING
            ).canResendCode
        )
        assertFalse(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL,
                resendTimerSeconds = RESEND_TIMER_BLOCKING,
                actionLoading = NOT_LOADING
            ).canResendCode
        )
        assertFalse(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL,
                resendTimerSeconds = RESEND_TIMER_READY,
                actionLoading = LOADING
            ).canResendCode
        )
    }

    private companion object {
        const val EMAIL = "user@example.com"
        const val NEW_PASSWORD = "secret"
        const val CODE_SIX_CHARS = "123456"
        const val CODE_FIVE_CHARS = "12345"
        const val CODE_TWO_CHARS = "12"
        const val CODE_LENGTH_SIX = 6
        const val RESEND_TIMER_READY = 0
        const val RESEND_TIMER_BLOCKING = 5
        const val PASSWORD_VALID = true
        const val PASSWORD_INVALID = false
        const val LOADING = true
        const val NOT_LOADING = false
    }
}
