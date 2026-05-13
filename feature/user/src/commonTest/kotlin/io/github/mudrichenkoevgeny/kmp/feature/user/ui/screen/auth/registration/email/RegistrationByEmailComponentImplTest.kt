package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.registration.email

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.otpConfirmationMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.securitySettingsMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.repository.SecuritySettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.data.authDataPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.registration.RegistrationRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.test.runUserUiComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.RegistrationByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.SendRegistrationConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidatorImpl
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class RegistrationByEmailComponentImplTest {

    @Test
    fun onEmailChanged_invalidEmail_marksEmailInvalid() = runUserUiComponentTest {
        val repo = RegistrationRepositoryMock()
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(INVALID_EMAIL)
            advanceUntilIdle()
            val emailState = assertIs<RegistrationByEmailScreenState.EmailInput>(harness.component.state.value)
            assertFalse(emailState.isEmailValid)
            assertEquals(INVALID_EMAIL, emailState.email)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onEmailChanged_whenRemainingDelayPositive_skipsToRegistrationInput() = runUserUiComponentTest {
        val repo = RegistrationRepositoryMock().apply {
            remainingDelayProvider = { REMAINING_DELAY_SECONDS }
        }
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            advanceTimeBy(100)
            val reg = assertIs<RegistrationByEmailScreenState.RegistrationInput>(harness.component.state.value)
            assertEquals(VALID_EMAIL, reg.email)
            assertEquals(REMAINING_DELAY_SECONDS, reg.resendTimerSeconds)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_success_movesToRegistrationInput() = runUserUiComponentTest {
        val repo = RegistrationRepositoryMock().apply {
            otpConfirmationResultProvider = {
                AppResult.Success(otpConfirmationMock(retryAfterSeconds = RETRY_AFTER_SEND_SUCCESS))
            }
        }
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceTimeBy(100)
            val reg = assertIs<RegistrationByEmailScreenState.RegistrationInput>(harness.component.state.value)
            assertEquals(VALID_EMAIL, reg.email)
            assertEquals(RETRY_AFTER_SEND_SUCCESS, reg.resendTimerSeconds)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_tooManyRequests_movesToRegistrationInputWithRetryFromError() = runUserUiComponentTest {
        val repo = RegistrationRepositoryMock().apply {
            otpConfirmationResultProvider = {
                AppResult.Error(UserError.TooManyConfirmationRequests(retryAfterSeconds = RETRY_AFTER_RATE_LIMIT))
            }
        }
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceTimeBy(100)
            val reg = assertIs<RegistrationByEmailScreenState.RegistrationInput>(harness.component.state.value)
            assertEquals(RETRY_AFTER_RATE_LIMIT, reg.resendTimerSeconds)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_genericError_keepsEmailStepWithError() = runUserUiComponentTest {
        val repo = RegistrationRepositoryMock().apply {
            otpConfirmationResultProvider = {
                AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
            }
        }
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            val emailState = assertIs<RegistrationByEmailScreenState.EmailInput>(harness.component.state.value)
            assertIs<CommonError.Unknown>(emailState.actionError)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onPasswordChanged_updatesPasswordValidityViaPolicy() = runUserUiComponentTest {
        val repo = RegistrationRepositoryMock().apply {
            otpConfirmationResultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
        }
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onPasswordChanged(SHORT_PASSWORD)
            advanceUntilIdle()
            var reg = assertIs<RegistrationByEmailScreenState.RegistrationInput>(harness.component.state.value)
            assertFalse(reg.isPasswordValid)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()
            reg = assertIs<RegistrationByEmailScreenState.RegistrationInput>(harness.component.state.value)
            assertTrue(reg.isPasswordValid)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onRegisterClick_success_callsOnFinished() = runUserUiComponentTest {
        val authData = authDataPayloadMock().toAuthData()
        val repo = RegistrationRepositoryMock().apply {
            otpConfirmationResultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            authDataResultProvider = { AppResult.Success(authData) }
        }
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onCodeChanged(FULL_CODE)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()
            harness.component.onRegisterClick()
            advanceUntilIdle()
            assertEquals(ONE_CALL, harness.onFinishedCalls)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onRegisterClick_registerError_surfacesError() = runUserUiComponentTest {
        val repo = RegistrationRepositoryMock().apply {
            otpConfirmationResultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            authDataResultProvider = { AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE)) }
        }
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onCodeChanged(FULL_CODE)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()
            harness.component.onRegisterClick()
            advanceUntilIdle()
            val reg = assertIs<RegistrationByEmailScreenState.RegistrationInput>(harness.component.state.value)
            assertIs<CommonError.Unknown>(reg.actionError)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onBackClick_fromRegistration_returnsToEmailInput() = runUserUiComponentTest {
        val repo = RegistrationRepositoryMock().apply {
            otpConfirmationResultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
        }
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onBackClick()
            advanceUntilIdle()
            val emailState = assertIs<RegistrationByEmailScreenState.EmailInput>(harness.component.state.value)
            assertEquals(VALID_EMAIL, emailState.email)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onBackClick_fromEmail_invokesOnBack() = runUserUiComponentTest {
        val repo = RegistrationRepositoryMock()
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onBackClick()
            advanceUntilIdle()
            assertEquals(ONE_CALL, harness.onBackCalls)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onTogglePasswordVisibility_togglesFlag() = runUserUiComponentTest {
        val repo = RegistrationRepositoryMock().apply {
            otpConfirmationResultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
        }
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onTogglePasswordVisibility()
            var reg = assertIs<RegistrationByEmailScreenState.RegistrationInput>(harness.component.state.value)
            assertTrue(reg.isPasswordVisible)
            harness.component.onTogglePasswordVisibility()
            reg = assertIs<RegistrationByEmailScreenState.RegistrationInput>(harness.component.state.value)
            assertFalse(reg.isPasswordVisible)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onCodeChanged_ignoresInputLongerThanCodeLength() = runUserUiComponentTest {
        val repo = RegistrationRepositoryMock().apply {
            otpConfirmationResultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
        }
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onCodeChanged(FULL_CODE)
            harness.component.onCodeChanged(TOO_LONG_CODE)
            val reg = assertIs<RegistrationByEmailScreenState.RegistrationInput>(harness.component.state.value)
            assertEquals(FULL_CODE, reg.code)
        } finally {
            harness.destroy()
        }
    }

    private fun validatePasswordUseCase(): ValidatePasswordUseCase {
        val secRepo = SecuritySettingsRepositoryMock().apply {
            resultProvider = { AppResult.Success(securitySettingsMock()) }
        }
        return ValidatePasswordUseCase(secRepo, PasswordPolicyValidatorImpl())
    }

    private class ComponentHarness(
        registrationRepository: RegistrationRepositoryMock,
        validatePasswordUseCase: ValidatePasswordUseCase
    ) {
        var onBackCalls: Int = ZERO_CALLS
        var onFinishedCalls: Int = ZERO_CALLS
        private val lifecycle = LifecycleRegistry()
        val component: RegistrationByEmailComponentImpl

        init {
            lifecycle.resume()
            component = RegistrationByEmailComponentImpl(
                componentContext = DefaultComponentContext(lifecycle),
                registrationRepository = registrationRepository,
                sendRegistrationConfirmationToEmailUseCase = SendRegistrationConfirmationToEmailUseCase(registrationRepository),
                registrationByEmailUseCase = RegistrationByEmailUseCase(registrationRepository, AuthStorageMock(), UserStorageMock()),
                validatePasswordUseCase = validatePasswordUseCase,
                onBack = { onBackCalls++ },
                onFinished = { onFinishedCalls++ }
            )
        }

        fun destroy() = lifecycle.destroy()
    }

    private companion object {
        const val VALID_EMAIL = "test@example.com"
        const val INVALID_EMAIL = "invalid"
        const val RETRY_AFTER_SEND_SUCCESS = 12
        const val RETRY_AFTER_RATE_LIMIT = 30
        const val REMAINING_DELAY_SECONDS = 15
        const val ZERO_RETRY = 0
        const val VALID_PASSWORD = "Password123!"
        const val SHORT_PASSWORD = "1"
        const val FULL_CODE = "123456"
        const val TOO_LONG_CODE = "1234567"
        const val NOT_RETRYABLE = false
        const val ZERO_CALLS = 0
        const val ONE_CALL = 1
    }
}