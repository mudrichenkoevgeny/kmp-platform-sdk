package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@InternalApi
class TotpSettingsScreenStateTest {

    @Test
    fun disabled_defaultParameters_setsExpectedFlags() {
        val state = TotpSettingsScreenState.Disabled()

        assertFalse(state.actionLoading)
        assertNull(state.actionError)
    }

    @Test
    fun disabled_customParameters_setsExpectedValues() {
        val error = CommonError.Unknown()
        val state = TotpSettingsScreenState.Disabled(
            actionLoading = ACTION_LOADING,
            actionError = error
        )

        assertTrue(state.actionLoading)
        assertEquals(error, state.actionError)
    }

    @Test
    fun setupInProgress_defaultParameters_setsExpectedFlags() {
        val setup = createMockTotpSetup()
        val state = TotpSettingsScreenState.SetupInProgress(setup = setup)

        assertEquals(setup, state.setup)
        assertEquals(EMPTY_STRING, state.code)
        assertFalse(state.actionLoading)
        assertNull(state.actionError)
        assertFalse(state.canConfirm)
    }

    @Test
    fun setupInProgress_canConfirm_requiresCorrectLengthDigitsAndNotLoading() {
        val setup = createMockTotpSetup()
        val state = TotpSettingsScreenState.SetupInProgress(
            setup = setup,
            code = VALID_CODE,
            actionLoading = NOT_LOADING
        )

        assertTrue(state.canConfirm)
    }

    @Test
    fun setupInProgress_canConfirm_falseWhenCodeTooShort() {
        val setup = createMockTotpSetup()
        val state = TotpSettingsScreenState.SetupInProgress(
            setup = setup,
            code = SHORT_CODE,
            actionLoading = NOT_LOADING
        )

        assertFalse(state.canConfirm)
    }

    @Test
    fun setupInProgress_canConfirm_falseWhenCodeContainsNonDigits() {
        val setup = createMockTotpSetup()
        val state = TotpSettingsScreenState.SetupInProgress(
            setup = setup,
            code = NON_DIGIT_CODE,
            actionLoading = NOT_LOADING
        )

        assertFalse(state.canConfirm)
    }

    @Test
    fun setupInProgress_canConfirm_falseWhenActionLoading() {
        val setup = createMockTotpSetup()
        val state = TotpSettingsScreenState.SetupInProgress(
            setup = setup,
            code = VALID_CODE,
            actionLoading = ACTION_LOADING
        )

        assertFalse(state.canConfirm)
    }

    @Test
    fun enabled_defaultParameters_setsExpectedFlags() {
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val state = TotpSettingsScreenState.Enabled(recoveryCodes = recoveryCodes)

        assertEquals(recoveryCodes, state.recoveryCodes)
        assertFalse(state.actionLoading)
        assertNull(state.actionError)
    }

    @Test
    fun enabled_customParameters_setsExpectedValues() {
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val error = CommonError.Unknown()
        val state = TotpSettingsScreenState.Enabled(
            recoveryCodes = recoveryCodes,
            actionLoading = ACTION_LOADING,
            actionError = error
        )

        assertEquals(recoveryCodes, state.recoveryCodes)
        assertTrue(state.actionLoading)
        assertEquals(error, state.actionError)
    }

    @Test
    fun error_holdsProvidedAppError() {
        val error = CommonError.Unknown()
        val state = TotpSettingsScreenState.Error(error = error)

        assertEquals(error, state.error)
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

        fun createMockTotpSetup() = TotpSetup(
            secretKey = SECRET_KEY,
            otpAuthUrl = OTP_AUTH_URL,
            mfaToken = MFA_TOKEN
        )
    }
}