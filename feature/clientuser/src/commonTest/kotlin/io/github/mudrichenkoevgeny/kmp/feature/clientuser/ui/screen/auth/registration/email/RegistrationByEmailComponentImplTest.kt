package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.core.security.error.model.SecurityError
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.otpConfirmationMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.usecase.ValidatePasswordUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.data.authDataPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.registration.RegistrationRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.registration.RegistrationByEmailUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.registration.SendRegistrationConfirmationToEmailUseCaseMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class RegistrationByEmailComponentImplTest {

    @Test
    fun onEmailChanged_invalidEmail_marksEmailInvalid() = runComponentTest {
        val context = createRegistrationByEmailComponentTestContext()
        try {
            context.component.onEmailChanged(INVALID_EMAIL)
            advanceUntilIdle()
            val state = context.component.state.value
            assertFalse(state.isEmailValid)
            assertEquals(INVALID_EMAIL, state.email)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onEmailChanged_whenRemainingDelayPositive_skipsToRegistrationInput() = runComponentTest {
        val repo = RegistrationRepositoryMock().apply {
            remainingDelayProvider = { REMAINING_DELAY_SECONDS }
        }
        val context = createRegistrationByEmailComponentTestContext(registrationRepository = repo)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            advanceTimeBy(100.milliseconds)
            val state = context.component.state.value
            assertEquals(VALID_EMAIL, state.email)
            assertEquals(REMAINING_DELAY_SECONDS, state.resendTimerSeconds)
            assertIs<RegistrationByEmailComponent.Child.ConfirmInput>(context.component.stack.value.active.instance)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSendCodeClick_success_movesToRegistrationInput() = runComponentTest {
        val sendUseCase = SendRegistrationConfirmationToEmailUseCaseMock().apply {
            resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = RETRY_AFTER_SEND_SUCCESS)) }
        }
        val context = createRegistrationByEmailComponentTestContext(sendRegistrationConfirmationToEmailUseCase = sendUseCase)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.component.onSendCodeClick()
            advanceTimeBy(100.milliseconds)
            val state = context.component.state.value
            assertEquals(VALID_EMAIL, state.email)
            assertEquals(RETRY_AFTER_SEND_SUCCESS, state.resendTimerSeconds)
            assertIs<RegistrationByEmailComponent.Child.ConfirmInput>(context.component.stack.value.active.instance)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSendCodeClick_tooManyRequests_movesToRegistrationInputWithRetryFromError() = runComponentTest {
        val sendUseCase = SendRegistrationConfirmationToEmailUseCaseMock().apply {
            resultProvider = { AppResult.Error(UserError.TooManyConfirmationRequests(retryAfterSeconds = RETRY_AFTER_RATE_LIMIT)) }
        }
        val context = createRegistrationByEmailComponentTestContext(sendRegistrationConfirmationToEmailUseCase = sendUseCase)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.component.onSendCodeClick()
            advanceTimeBy(100.milliseconds)
            val state = context.component.state.value
            assertEquals(RETRY_AFTER_RATE_LIMIT, state.resendTimerSeconds)
            assertIs<RegistrationByEmailComponent.Child.ConfirmInput>(context.component.stack.value.active.instance)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSendCodeClick_genericError_keepsEmailStepWithError() = runComponentTest {
        val sendUseCase = SendRegistrationConfirmationToEmailUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE)) }
        }
        val context = createRegistrationByEmailComponentTestContext(sendRegistrationConfirmationToEmailUseCase = sendUseCase)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.component.onSendCodeClick()
            advanceUntilIdle()
            val state = context.component.state.value
            assertIs<CommonError.Unknown>(state.actionError)
            assertIs<RegistrationByEmailComponent.Child.EmailInput>(context.component.stack.value.active.instance)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onPasswordChanged_updatesPasswordValidityViaPolicy() = runComponentTest {
        val context = createRegistrationByEmailComponentTestContext()
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.sendRegistrationConfirmationToEmailUseCase.resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            context.component.onSendCodeClick()
            advanceUntilIdle()

            context.validatePasswordUseCase.resultProvider = { AppResult.Error(SecurityError.PasswordTooShort()) }
            context.component.onPasswordChanged(SHORT_PASSWORD)
            advanceUntilIdle()
            var state = context.component.state.value
            assertFalse(state.isPasswordValid)

            context.validatePasswordUseCase.resultProvider = { AppResult.Success(Unit) }
            context.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()
            state = context.component.state.value
            assertTrue(state.isPasswordValid)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRegisterClick_success_callsOnFinished() = runComponentTest {
        val authData = authDataPayloadMock().toAuthData()
        val registerUseCase = RegistrationByEmailUseCaseMock().apply {
            resultProvider = { _, _, _ -> AppResult.Success(authData) }
        }
        val context = createRegistrationByEmailComponentTestContext(registrationByEmailUseCase = registerUseCase)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.sendRegistrationConfirmationToEmailUseCase.resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            context.component.onSendCodeClick()
            advanceUntilIdle()
            context.component.onCodeChanged(FULL_CODE)
            context.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()
            context.component.onRegisterClick()
            advanceUntilIdle()
            assertEquals(ONE_CALL, context.onFinishedCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRegisterClick_registerError_surfacesError() = runComponentTest {
        val registerUseCase = RegistrationByEmailUseCaseMock().apply {
            resultProvider = { _, _, _ -> AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE)) }
        }
        val context = createRegistrationByEmailComponentTestContext(registrationByEmailUseCase = registerUseCase)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.sendRegistrationConfirmationToEmailUseCase.resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            context.component.onSendCodeClick()
            advanceUntilIdle()
            context.component.onCodeChanged(FULL_CODE)
            context.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()
            context.component.onRegisterClick()
            advanceUntilIdle()
            val state = context.component.state.value
            assertIs<CommonError.Unknown>(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_fromRegistration_returnsToEmailInput() = runComponentTest {
        val context = createRegistrationByEmailComponentTestContext()
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.sendRegistrationConfirmationToEmailUseCase.resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            context.component.onSendCodeClick()
            advanceUntilIdle()
            context.component.onBackClick()
            advanceUntilIdle()
            val state = context.component.state.value
            assertEquals(VALID_EMAIL, state.email)
            assertIs<RegistrationByEmailComponent.Child.EmailInput>(context.component.stack.value.active.instance)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_fromEmail_invokesOnBack() = runComponentTest {
        val context = createRegistrationByEmailComponentTestContext()
        try {
            context.component.onBackClick()
            advanceUntilIdle()
            assertEquals(ONE_CALL, context.onBackCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onTogglePasswordVisibility_togglesFlag() = runComponentTest {
        val context = createRegistrationByEmailComponentTestContext()
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.sendRegistrationConfirmationToEmailUseCase.resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            context.component.onSendCodeClick()
            advanceUntilIdle()
            context.component.onTogglePasswordVisibility()
            var state = context.component.state.value
            assertTrue(state.isPasswordVisible)
            context.component.onTogglePasswordVisibility()
            state = context.component.state.value
            assertFalse(state.isPasswordVisible)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onCodeChanged_ignoresInputLongerThanCodeLength() = runComponentTest {
        val context = createRegistrationByEmailComponentTestContext()
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.sendRegistrationConfirmationToEmailUseCase.resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            context.component.onSendCodeClick()
            advanceUntilIdle()
            context.component.onCodeChanged(FULL_CODE)
            context.component.onCodeChanged(TOO_LONG_CODE)
            val state = context.component.state.value
            assertEquals(FULL_CODE, state.code)
        } finally {
            context.destroy()
        }
    }

    private fun createRegistrationByEmailComponentTestContext(
        registrationRepository: RegistrationRepositoryMock = RegistrationRepositoryMock(),
        sendRegistrationConfirmationToEmailUseCase: SendRegistrationConfirmationToEmailUseCaseMock = SendRegistrationConfirmationToEmailUseCaseMock(),
        registrationByEmailUseCase: RegistrationByEmailUseCaseMock = RegistrationByEmailUseCaseMock(),
        validatePasswordUseCase: ValidatePasswordUseCaseMock = ValidatePasswordUseCaseMock()
    ): RegistrationByEmailComponentTestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = RegistrationByEmailComponentTestContext(
            lifecycle = lifecycle,
            sendRegistrationConfirmationToEmailUseCase = sendRegistrationConfirmationToEmailUseCase,
            registrationByEmailUseCase = registrationByEmailUseCase,
            validatePasswordUseCase = validatePasswordUseCase
        )

        context.component = RegistrationByEmailComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            registrationRepository = registrationRepository,
            sendRegistrationConfirmationToEmailUseCase = sendRegistrationConfirmationToEmailUseCase,
            registrationByEmailUseCase = registrationByEmailUseCase,
            validatePasswordUseCase = validatePasswordUseCase,
            onBack = { context.onBackCalls++ },
            onFinished = { context.onFinishedCalls++ }
        )

        return context
    }

    private class RegistrationByEmailComponentTestContext(
        val lifecycle: LifecycleRegistry,
        val sendRegistrationConfirmationToEmailUseCase: SendRegistrationConfirmationToEmailUseCaseMock,
        val registrationByEmailUseCase: RegistrationByEmailUseCaseMock,
        val validatePasswordUseCase: ValidatePasswordUseCaseMock
    ) {
        lateinit var component: RegistrationByEmailComponentImpl
        var onBackCalls: Int = 0
        var onFinishedCalls: Int = 0

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
        const val ONE_CALL = 1
    }
}
