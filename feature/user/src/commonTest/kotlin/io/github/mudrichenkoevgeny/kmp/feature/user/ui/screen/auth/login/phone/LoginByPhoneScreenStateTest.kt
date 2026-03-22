package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.phone

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoginByPhoneScreenStateTest {

    @Test
    fun codeInput_isCodeFullLength_matchesCodeLength() {
        assertTrue(
            LoginByPhoneScreenState.CodeInput(
                phoneNumber = PHONE,
                code = CODE_SIX_CHARS,
                codeLength = CODE_LENGTH_SIX
            ).isCodeFullLength
        )
        assertFalse(
            LoginByPhoneScreenState.CodeInput(
                phoneNumber = PHONE,
                code = CODE_FIVE_CHARS,
                codeLength = CODE_LENGTH_SIX
            ).isCodeFullLength
        )
        assertTrue(
            LoginByPhoneScreenState.CodeInput(
                phoneNumber = PHONE,
                code = CODE_FOUR_CHARS,
                codeLength = CODE_LENGTH_FOUR
            ).isCodeFullLength
        )
    }

    @Test
    fun codeInput_canResendCode_whenTimerZeroAndNotLoading() {
        assertTrue(
            LoginByPhoneScreenState.CodeInput(
                phoneNumber = PHONE,
                resendTimerSeconds = RESEND_TIMER_READY,
                actionLoading = NOT_LOADING
            ).canResendCode
        )
    }

    @Test
    fun codeInput_canResendCode_falseWhenTimerPositive() {
        assertFalse(
            LoginByPhoneScreenState.CodeInput(
                phoneNumber = PHONE,
                resendTimerSeconds = RESEND_TIMER_BLOCKING,
                actionLoading = NOT_LOADING
            ).canResendCode
        )
    }

    @Test
    fun codeInput_canResendCode_falseWhenLoading() {
        assertFalse(
            LoginByPhoneScreenState.CodeInput(
                phoneNumber = PHONE,
                resendTimerSeconds = RESEND_TIMER_READY,
                actionLoading = LOADING
            ).canResendCode
        )
    }

    private companion object {
        const val PHONE = "79990001122"
        const val CODE_SIX_CHARS = "123456"
        const val CODE_FIVE_CHARS = "12345"
        const val CODE_FOUR_CHARS = "1234"
        const val CODE_LENGTH_SIX = 6
        const val CODE_LENGTH_FOUR = 4
        const val RESEND_TIMER_READY = 0
        const val RESEND_TIMER_BLOCKING = 30
        const val LOADING = true
        const val NOT_LOADING = false
    }
}
