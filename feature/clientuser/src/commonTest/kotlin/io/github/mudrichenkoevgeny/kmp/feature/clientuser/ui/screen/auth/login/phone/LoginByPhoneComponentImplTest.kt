package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.otpConfirmationMock
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.data.authDataPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.login.LoginRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.login.LoginByPhoneUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.login.SendLoginConfirmationToPhoneUseCaseMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class LoginByPhoneComponentImplTest {

    @Test
    fun onPhoneChanged_invalidPhone_marksInvalid() = runComponentTest {
        val context = createLoginByPhoneComponentTestContext()
        try {
            context.component.onPhoneChanged(INVALID_PHONE)
            val state = context.component.state.value
            assertFalse(state.isPhoneNumberValid)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onPhoneChanged_validPhone_whenRemainingDelayPositive_movesToCodeInput() = runComponentTest {
        val repo = LoginRepositoryMock().apply {
            remainingDelayProvider = { REMAINING_DELAY_SECONDS }
        }
        val context = createLoginByPhoneComponentTestContext(loginRepository = repo)
        try {
            context.component.onPhoneChanged(VALID_PHONE)
            val state = context.component.state.value
            assertEquals(VALID_PHONE, state.phoneNumber)
            assertEquals(REMAINING_DELAY_SECONDS, state.resendTimerSeconds)
            assertIs<LoginByPhoneComponent.Child.CodeInput>(context.component.stack.value.active.instance)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSendCodeClick_success_movesToCodeInput() = runComponentTest {
        val sendUseCase = SendLoginConfirmationToPhoneUseCaseMock().apply {
            resultProvider = {
                AppResult.Success(otpConfirmationMock(retryAfterSeconds = RETRY_AFTER_SEND))
            }
        }
        val context = createLoginByPhoneComponentTestContext(sendLoginConfirmationToPhoneUseCase = sendUseCase)
        try {
            context.component.onPhoneChanged(VALID_PHONE)
            context.component.onSendCodeClick()
            runCurrent()
            val state = context.component.state.value
            assertEquals(VALID_PHONE, state.phoneNumber)
            assertEquals(RETRY_AFTER_SEND, state.resendTimerSeconds)
            assertIs<LoginByPhoneComponent.Child.CodeInput>(context.component.stack.value.active.instance)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSendCodeClick_tooManyRequests_movesToCodeInputWithRetry() = runComponentTest {
        val sendUseCase = SendLoginConfirmationToPhoneUseCaseMock().apply {
            resultProvider = {
                AppResult.Error(UserError.TooManyConfirmationRequests(retryAfterSeconds = RETRY_AFTER_RATE_LIMIT))
            }
        }
        val context = createLoginByPhoneComponentTestContext(sendLoginConfirmationToPhoneUseCase = sendUseCase)
        try {
            context.component.onPhoneChanged(VALID_PHONE)
            context.component.onSendCodeClick()
            runCurrent()
            val state = context.component.state.value
            assertEquals(RETRY_AFTER_RATE_LIMIT, state.resendTimerSeconds)
            assertIs<LoginByPhoneComponent.Child.CodeInput>(context.component.stack.value.active.instance)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSendCodeClick_genericError_keepsPhoneStepWithError() = runComponentTest {
        val sendUseCase = SendLoginConfirmationToPhoneUseCaseMock().apply {
            resultProvider = {
                AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
            }
        }
        val context = createLoginByPhoneComponentTestContext(sendLoginConfirmationToPhoneUseCase = sendUseCase)
        try {
            context.component.onPhoneChanged(VALID_PHONE)
            context.component.onSendCodeClick()
            advanceUntilIdle()
            val state = context.component.state.value
            assertFalse(state.actionLoading)
            assertIs<CommonError.Unknown>(state.actionError)
            assertIs<LoginByPhoneComponent.Child.PhoneInput>(context.component.stack.value.active.instance)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onCodeChanged_fullCode_triggersLogin_andOnFinished() = runComponentTest {
        val authData = authDataPayloadMock().toAuthData()
        val loginUseCase = LoginByPhoneUseCaseMock().apply {
            resultProvider = { _, _ -> AppResult.Success(authData) }
        }
        val context = createLoginByPhoneComponentTestContext(loginByPhoneUseCase = loginUseCase)
        try {
            context.component.onPhoneChanged(VALID_PHONE)
            context.sendLoginConfirmationToPhoneUseCase.resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            context.component.onSendCodeClick()
            advanceUntilIdle()
            context.component.onCodeChanged(FULL_CODE)
            advanceUntilIdle()
            assertEquals(ONE_CALL, context.onFinishedCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmCodeClick_loginError_surfacesError() = runComponentTest {
        val loginUseCase = LoginByPhoneUseCaseMock().apply {
            resultProvider = { _, _ -> AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE)) }
        }
        val context = createLoginByPhoneComponentTestContext(loginByPhoneUseCase = loginUseCase)
        try {
            context.component.onPhoneChanged(VALID_PHONE)
            context.sendLoginConfirmationToPhoneUseCase.resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            context.component.onSendCodeClick()
            advanceUntilIdle()
            context.component.onCodeChanged(FULL_CODE)
            advanceUntilIdle()
            assertEquals(ZERO_CALLS, context.onFinishedCalls)
            val state = context.component.state.value
            assertFalse(state.actionLoading)
            assertIs<CommonError.Unknown>(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmCodeClick_whenCodeIncomplete_doesNotCallRepository() = runComponentTest {
        val context = createLoginByPhoneComponentTestContext()
        try {
            context.component.onPhoneChanged(VALID_PHONE)
            context.sendLoginConfirmationToPhoneUseCase.resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            context.component.onSendCodeClick()
            runCurrent()
            context.component.onConfirmCodeClick()
            runCurrent()
            assertEquals(ZERO_CALLS, context.onFinishedCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onResetPhoneClick_returnsToPhoneInput_preservingNumber() = runComponentTest {
        val context = createLoginByPhoneComponentTestContext()
        try {
            context.component.onPhoneChanged(VALID_PHONE)
            context.sendLoginConfirmationToPhoneUseCase.resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            context.component.onSendCodeClick()
            runCurrent()
            assertIs<LoginByPhoneComponent.Child.CodeInput>(context.component.stack.value.active.instance)
            context.component.onResetPhoneClick()
            val state = context.component.state.value
            assertEquals(VALID_PHONE, state.phoneNumber)
            assertIs<LoginByPhoneComponent.Child.PhoneInput>(context.component.stack.value.active.instance)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_fromCodeStep_returnsToPhoneInput() = runComponentTest {
        val context = createLoginByPhoneComponentTestContext()
        try {
            context.component.onPhoneChanged(VALID_PHONE)
            context.sendLoginConfirmationToPhoneUseCase.resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            context.component.onSendCodeClick()
            runCurrent()
            context.component.onBackClick()
            assertIs<LoginByPhoneComponent.Child.PhoneInput>(context.component.stack.value.active.instance)
            assertEquals(ZERO_CALLS, context.onBackCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_fromPhoneStep_invokesOnBack() = runComponentTest {
        val context = createLoginByPhoneComponentTestContext()
        try {
            context.component.onBackClick()
            assertEquals(ONE_CALL, context.onBackCalls)
        } finally {
            context.destroy()
        }
    }

    private fun createLoginByPhoneComponentTestContext(
        loginRepository: LoginRepositoryMock = LoginRepositoryMock(),
        sendLoginConfirmationToPhoneUseCase: SendLoginConfirmationToPhoneUseCaseMock = SendLoginConfirmationToPhoneUseCaseMock(),
        loginByPhoneUseCase: LoginByPhoneUseCaseMock = LoginByPhoneUseCaseMock()
    ): LoginByPhoneComponentTestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = LoginByPhoneComponentTestContext(
            lifecycle = lifecycle,
            sendLoginConfirmationToPhoneUseCase = sendLoginConfirmationToPhoneUseCase,
            loginByPhoneUseCase = loginByPhoneUseCase
        )

        context.component = LoginByPhoneComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            loginRepository = loginRepository,
            sendLoginConfirmationToPhoneUseCase = sendLoginConfirmationToPhoneUseCase,
            loginByPhoneUseCase = loginByPhoneUseCase,
            onNavigateToTotp = { context.lastTotpMfaToken = it },
            onNavigateToPendingDeletion = { context.onNavigateToPendingDeletionCalls++ },
            onBack = { context.onBackCalls++ },
            onFinished = { context.onFinishedCalls++ }
        )

        return context
    }

    private class LoginByPhoneComponentTestContext(
        val lifecycle: LifecycleRegistry,
        val sendLoginConfirmationToPhoneUseCase: SendLoginConfirmationToPhoneUseCaseMock,
        val loginByPhoneUseCase: LoginByPhoneUseCaseMock
    ) {
        lateinit var component: LoginByPhoneComponentImpl
        var onBackCalls: Int = 0
        var onFinishedCalls: Int = 0
        var onNavigateToPendingDeletionCalls: Int = 0
        var lastTotpMfaToken: String? = null

        fun destroy() = lifecycle.destroy()
    }

    private companion object {
        const val VALID_PHONE = "+1234567890"
        const val INVALID_PHONE = "012"
        const val FULL_CODE = "123456"
        const val REMAINING_DELAY_SECONDS = 25
        const val RETRY_AFTER_SEND = 15
        const val RETRY_AFTER_RATE_LIMIT = 40
        const val ZERO_RETRY = 0
        const val NOT_RETRYABLE = false
        const val ZERO_CALLS = 0
        const val ONE_CALL = 1
    }
}
