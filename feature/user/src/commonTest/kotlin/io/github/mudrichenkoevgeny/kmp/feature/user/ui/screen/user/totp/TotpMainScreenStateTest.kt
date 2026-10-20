package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.TotpMainScreenState
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.totpsetup.createTestTotpSetup
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@InternalApi
class TotpMainScreenStateTest {

    @Test
    fun defaultParameters_setsExpectedFlags() {
        val state = TotpMainScreenState()

        assertFalse(state.isInitialized)
        assertFalse(state.isEnabled)
        assertFalse(state.actionLoading)
        assertNull(state.actionError)
        assertNull(state.initError)
    }

    @Test
    fun canConfirm_requiresCorrectLengthDigitsAndNotLoading() {
        val state = TotpMainScreenState(
            code = VALID_CODE,
            actionLoading = NOT_LOADING
        )

        assertTrue(state.canConfirm)
    }

    @Test
    fun canConfirm_falseWhenCodeTooShort() {
        val state = TotpMainScreenState(
            code = SHORT_CODE,
            actionLoading = NOT_LOADING
        )

        assertFalse(state.canConfirm)
    }

    @Test
    fun canConfirm_falseWhenCodeContainsNonDigits() {
        val state = TotpMainScreenState(
            code = NON_DIGIT_CODE,
            actionLoading = NOT_LOADING
        )

        assertFalse(state.canConfirm)
    }

    @Test
    fun canConfirm_falseWhenActionLoading() {
        val state = TotpMainScreenState(
            code = VALID_CODE,
            actionLoading = ACTION_LOADING
        )

        assertFalse(state.canConfirm)
    }

    private companion object {
        const val EMPTY_STRING = ""
        const val VALID_CODE = "123456"
        const val SHORT_CODE = "12345"
        const val NON_DIGIT_CODE = "12345a"
        const val ACTION_LOADING = true
        const val NOT_LOADING = false

        const val SECRET_KEY = "SECRET"
        const val OTP_AUTH_URL = "otpauth://totp/..."
        const val MFA_TOKEN = "mfa-token"
        const val CODE_ONE = "1111-2222"
        const val CODE_TWO = "3333-4444"

        fun createMockTotpSetup() = createTestTotpSetup(
            secretKey = SECRET_KEY,
            otpAuthUrl = OTP_AUTH_URL,
            mfaToken = MFA_TOKEN
        )
    }
}