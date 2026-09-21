package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target

import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UnlockTargetInputScreenStateTest {

    @Test
    fun emailValidation_validatesCorrectly() {
        val invalidState = UnlockTargetInputScreenState(
            method = UnlockMethod.EMAIL,
            input = "invalid-email"
        )
        assertFalse(invalidState.isInputValid)
        assertFalse(invalidState.canSendCode)

        val validState = UnlockTargetInputScreenState(
            method = UnlockMethod.EMAIL,
            input = "user@example.com"
        )
        assertTrue(validState.isInputValid)
        assertTrue(validState.canSendCode)
    }

    @Test
    fun phoneValidation_validatesCorrectly() {
        val invalidState = UnlockTargetInputScreenState(
            method = UnlockMethod.PHONE,
            input = "123"
        )
        assertFalse(invalidState.isInputValid)
        assertFalse(invalidState.canSendCode)

        val validState = UnlockTargetInputScreenState(
            method = UnlockMethod.PHONE,
            input = "79991234567"
        )
        assertTrue(validState.isInputValid)
        assertTrue(validState.canSendCode)
    }

    @Test
    fun canSendCode_falseWhenLoading() {
        val loadingState = UnlockTargetInputScreenState(
            method = UnlockMethod.EMAIL,
            input = "user@example.com",
            actionLoading = true
        )
        assertTrue(loadingState.isInputValid)
        assertFalse(loadingState.canSendCode)
    }
}
