package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp

import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoginByTotpScreenStateTest {

    @Test
    fun content_totpMode_isCodeValid_trueWhenCorrectLengthDigits() {
        assertTrue(
            LoginByTotpScreenState.Content(
                mfaToken = MFA_TOKEN,
                code = VALID_TOTP_CODE,
                mode = LoginByTotpScreenState.Mode.TOTP
            ).isCodeValid
        )
    }

    @Test
    fun content_totpMode_isCodeValid_falseWhenTooShort() {
        assertFalse(
            LoginByTotpScreenState.Content(
                mfaToken = MFA_TOKEN,
                code = SHORT_TOTP_CODE,
                mode = LoginByTotpScreenState.Mode.TOTP
            ).isCodeValid
        )
    }

    @Test
    fun content_totpMode_isCodeValid_falseWhenContainsNonDigits() {
        assertFalse(
            LoginByTotpScreenState.Content(
                mfaToken = MFA_TOKEN,
                code = NON_DIGIT_TOTP_CODE,
                mode = LoginByTotpScreenState.Mode.TOTP
            ).isCodeValid
        )
    }

    @Test
    fun content_recoveryCodeMode_isCodeValid_trueWhenNotBlank() {
        assertTrue(
            LoginByTotpScreenState.Content(
                mfaToken = MFA_TOKEN,
                code = VALID_RECOVERY_CODE,
                mode = LoginByTotpScreenState.Mode.RECOVERY_CODE
            ).isCodeValid
        )
    }

    @Test
    fun content_recoveryCodeMode_isCodeValid_falseWhenBlank() {
        assertFalse(
            LoginByTotpScreenState.Content(
                mfaToken = MFA_TOKEN,
                code = BLANK_CODE,
                mode = LoginByTotpScreenState.Mode.RECOVERY_CODE
            ).isCodeValid
        )
    }

    @Test
    fun content_canSubmit_requiresValidCodeAndNotLoading() {
        assertTrue(
            LoginByTotpScreenState.Content(
                mfaToken = MFA_TOKEN,
                code = VALID_TOTP_CODE,
                mode = LoginByTotpScreenState.Mode.TOTP,
                actionLoading = NOT_LOADING
            ).canSubmit
        )
    }

    @Test
    fun content_canSubmit_falseWhenCodeInvalid() {
        assertFalse(
            LoginByTotpScreenState.Content(
                mfaToken = MFA_TOKEN,
                code = SHORT_TOTP_CODE,
                mode = LoginByTotpScreenState.Mode.TOTP,
                actionLoading = NOT_LOADING
            ).canSubmit
        )
    }

    @Test
    fun content_canSubmit_falseWhenLoading() {
        assertFalse(
            LoginByTotpScreenState.Content(
                mfaToken = MFA_TOKEN,
                code = VALID_TOTP_CODE,
                mode = LoginByTotpScreenState.Mode.TOTP,
                actionLoading = LOADING
            ).canSubmit
        )
    }

    private companion object {
        const val MFA_TOKEN = "test-mfa-token"
        const val VALID_TOTP_CODE = "123456"
        const val SHORT_TOTP_CODE = "12345"
        const val NON_DIGIT_TOTP_CODE = "12345a"
        const val VALID_RECOVERY_CODE = "ABCD-1234"
        const val BLANK_CODE = "   "
        const val LOADING = true
        const val NOT_LOADING = false
    }
}