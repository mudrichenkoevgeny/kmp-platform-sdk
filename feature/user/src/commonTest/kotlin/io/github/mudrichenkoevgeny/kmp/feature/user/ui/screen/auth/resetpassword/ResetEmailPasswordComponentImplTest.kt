package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.otpConfirmationMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.securitySettingsMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.repository.SecuritySettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.identifier.userIdentifierPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.resetpassword.ResetPasswordRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.ResetEmailPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.SendResetPasswordConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidatorImpl
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifier
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class ResetEmailPasswordComponentImplTest {

    @Test
    fun onEmailChanged_invalidEmail_marksEmailInvalid() = runComponentTest {
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = ZERO_RETRY
        val harness = createHarness(repo)
        try {
            harness.component.onEmailChanged(INVALID_EMAIL)
            runCurrent()
            val emailState = assertIs<ResetEmailPasswordScreenState.EmailInput>(harness.component.state.value)
            assertFalse(emailState.isEmailValid)
            assertEquals(INVALID_EMAIL, emailState.email)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onEmailChanged_whenRemainingDelayPositive_skipsToResetInput() = runComponentTest {
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = REMAINING_DELAY_SECONDS
        val harness = createHarness(repo)
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            advanceTimeBy(100)
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(harness.component.state.value)
            assertEquals(VALID_EMAIL, reset.email)
            assertEquals(REMAINING_DELAY_SECONDS, reset.resendTimerSeconds)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_success_movesToResetInput() = runComponentTest {
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = ZERO_RETRY
        repo.sendResult = AppResult.Success(
            otpConfirmationMock(retryAfterSeconds = RETRY_AFTER_SEND_SUCCESS)
        )
        val harness = createHarness(repo)
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceTimeBy(100)
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(harness.component.state.value)
            assertEquals(VALID_EMAIL, reset.email)
            assertEquals(RETRY_AFTER_SEND_SUCCESS, reset.resendTimerSeconds)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_tooManyRequests_movesToResetInputWithRetry() = runComponentTest {
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = ZERO_RETRY
        repo.sendResult = AppResult.Error(
            UserError.TooManyConfirmationRequests(retryAfterSeconds = RETRY_AFTER_RATE_LIMIT)
        )
        val harness = createHarness(repo)
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceTimeBy(100)
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(harness.component.state.value)
            assertEquals(RETRY_AFTER_RATE_LIMIT, reset.resendTimerSeconds)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_genericError_keepsEmailStepWithError() = runComponentTest {
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = ZERO_RETRY
        repo.sendResult = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        val harness = createHarness(repo)
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceTimeBy(100)
            val emailState = assertIs<ResetEmailPasswordScreenState.EmailInput>(harness.component.state.value)
            assertFalse(emailState.actionLoading)
            assertIs<CommonError.Unknown>(emailState.actionError)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onPasswordChanged_updatesPasswordValidityViaPolicy() = runComponentTest {
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = ZERO_RETRY
        repo.sendResult = AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY))
        val harness = createHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceTimeBy(100)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceTimeBy(100)
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(harness.component.state.value)
            assertTrue(reset.isPasswordValid)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onConfirmResetClick_success_callsOnFinished() = runComponentTest {
        val userId = userIdentifierPayloadMock(identifier = VALID_EMAIL).toUserIdentifier()
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = ZERO_RETRY
        repo.sendResult = AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY))
        repo.resetResult = AppResult.Success(userId)
        val harness = createHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceTimeBy(100)
            harness.component.onCodeChanged(FULL_CODE)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceTimeBy(100)
            harness.component.onConfirmResetClick()
            advanceTimeBy(100)
            assertEquals(ONE_CALL, harness.counters.finished)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onConfirmResetClick_resetError_surfacesError() = runComponentTest {
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = ZERO_RETRY
        repo.sendResult = AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY))
        repo.resetResult = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        val harness = createHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceTimeBy(100)
            harness.component.onCodeChanged(FULL_CODE)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceTimeBy(100)
            harness.component.onConfirmResetClick()
            advanceTimeBy(100)
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(harness.component.state.value)
            assertIs<CommonError.Unknown>(reset.actionError)
        } finally {
            harness.destroy()
        }
    }

    private fun createHarness(
        passwordRepository: ResetPasswordRepositoryMock,
        validatePassword: ValidatePasswordUseCase = validatePasswordUseCase()
    ): Harness {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        val counters = NavigationCounters()
        val component = ResetEmailPasswordComponentImpl(
            componentContext = ctx,
            resetPasswordRepository = passwordRepository,
            sendResetPasswordConfirmationToEmailUseCase = SendResetPasswordConfirmationToEmailUseCase(passwordRepository),
            resetEmailPasswordUseCase = ResetEmailPasswordUseCase(passwordRepository),
            validatePasswordUseCase = validatePassword,
            onBack = { counters.back++ },
            onFinished = { counters.finished++ }
        )
        return Harness(lifecycle, component, counters)
    }

    private fun validatePasswordUseCase(): ValidatePasswordUseCase {
        val secRepo = SecuritySettingsRepositoryMock().apply {
            resultProvider = { AppResult.Success(securitySettingsMock()) }
            passwordPolicyResultProvider = { resultProvider() }
        }
        return ValidatePasswordUseCase(secRepo, PasswordPolicyValidatorImpl())
    }

    private class NavigationCounters(var finished: Int = 0, var back: Int = 0)
    private class Harness(val lifecycle: LifecycleRegistry, val component: ResetEmailPasswordComponentImpl, val counters: NavigationCounters) {
        fun destroy() = lifecycle.destroy()
    }

    private companion object {
        const val VALID_EMAIL = "test@example.com"
        const val INVALID_EMAIL = "invalid"
        const val REMAINING_DELAY_SECONDS = 45
        const val RETRY_AFTER_SEND_SUCCESS = 12
        const val RETRY_AFTER_RATE_LIMIT = 30
        const val ZERO_RETRY = 0
        const val VALID_PASSWORD = "Password123"
        const val FULL_CODE = "123456"
        const val NOT_RETRYABLE = false
        const val ONE_CALL = 1
    }
}