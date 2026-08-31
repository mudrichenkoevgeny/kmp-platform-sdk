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
import io.github.mudrichenkoevgeny.kmp.core.security.mock.usecase.ValidatePasswordUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.identifier.userIdentifierPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.resetpassword.ResetPasswordRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.resetpassword.ResetEmailPasswordUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.resetpassword.SendResetPasswordConfirmationToEmailUseCaseMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifier
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class ResetEmailPasswordComponentImplTest {

    @Test
    fun onEmailChanged_invalidEmail_marksEmailInvalid() = runComponentTest {
        val resetPasswordRepository = ResetPasswordRepositoryMock()
        resetPasswordRepository.remainingDelaySeconds = ZERO_RETRY
        val context = createResetEmailPasswordComponentTestContext(resetPasswordRepository)
        try {
            context.component.onEmailChanged(INVALID_EMAIL)
            runCurrent()
            val emailState = assertIs<ResetEmailPasswordScreenState.EmailInput>(context.component.state.value)
            assertFalse(emailState.isEmailValid)
            assertEquals(INVALID_EMAIL, emailState.email)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onEmailChanged_whenRemainingDelayPositive_skipsToResetInput() = runComponentTest {
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = REMAINING_DELAY_SECONDS
        val context = createResetEmailPasswordComponentTestContext(repo)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            advanceTimeBy(100.milliseconds)
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(context.component.state.value)
            assertEquals(VALID_EMAIL, reset.email)
            assertEquals(REMAINING_DELAY_SECONDS, reset.resendTimerSeconds)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSendCodeClick_success_movesToResetInput() = runComponentTest {
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = ZERO_RETRY
        val sendUseCase = SendResetPasswordConfirmationToEmailUseCaseMock().apply {
            resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = RETRY_AFTER_SEND_SUCCESS)) }
        }
        val context = createResetEmailPasswordComponentTestContext(repo, sendResetPasswordConfirmationToEmailUseCase = sendUseCase)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.component.onSendCodeClick()
            advanceTimeBy(100.milliseconds)
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(context.component.state.value)
            assertEquals(VALID_EMAIL, reset.email)
            assertEquals(RETRY_AFTER_SEND_SUCCESS, reset.resendTimerSeconds)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSendCodeClick_tooManyRequests_movesToResetInputWithRetry() = runComponentTest {
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = ZERO_RETRY
        val sendUseCase = SendResetPasswordConfirmationToEmailUseCaseMock().apply {
            resultProvider = { AppResult.Error(UserError.TooManyConfirmationRequests(retryAfterSeconds = RETRY_AFTER_RATE_LIMIT)) }
        }
        val context = createResetEmailPasswordComponentTestContext(repo, sendResetPasswordConfirmationToEmailUseCase = sendUseCase)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.component.onSendCodeClick()
            advanceTimeBy(100.milliseconds)
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(context.component.state.value)
            assertEquals(RETRY_AFTER_RATE_LIMIT, reset.resendTimerSeconds)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSendCodeClick_genericError_keepsEmailStepWithError() = runComponentTest {
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = ZERO_RETRY
        val sendUseCase = SendResetPasswordConfirmationToEmailUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE)) }
        }
        val context = createResetEmailPasswordComponentTestContext(repo, sendResetPasswordConfirmationToEmailUseCase = sendUseCase)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.component.onSendCodeClick()
            advanceTimeBy(100.milliseconds)
            val emailState = assertIs<ResetEmailPasswordScreenState.EmailInput>(context.component.state.value)
            assertFalse(emailState.actionLoading)
            assertIs<CommonError.Unknown>(emailState.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onPasswordChanged_updatesPasswordValidityViaPolicy() = runComponentTest {
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = ZERO_RETRY
        val context = createResetEmailPasswordComponentTestContext(repo)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.sendResetPasswordConfirmationToEmailUseCase.resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            context.component.onSendCodeClick()
            advanceTimeBy(100.milliseconds)
            
            context.validatePasswordUseCase.resultProvider = { AppResult.Success(Unit) }
            context.component.onPasswordChanged(VALID_PASSWORD)
            advanceTimeBy(100.milliseconds)
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(context.component.state.value)
            assertTrue(reset.isPasswordValid)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmResetClick_success_callsOnFinished() = runComponentTest {
        val userId = userIdentifierPayloadMock(identifier = VALID_EMAIL).toUserIdentifier()
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = ZERO_RETRY
        val resetUseCase = ResetEmailPasswordUseCaseMock().apply {
            resultProvider = { _, _, _ -> AppResult.Success(userId) }
        }
        val context = createResetEmailPasswordComponentTestContext(repo, resetEmailPasswordUseCase = resetUseCase)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.sendResetPasswordConfirmationToEmailUseCase.resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            context.component.onSendCodeClick()
            advanceTimeBy(100.milliseconds)
            context.component.onCodeChanged(FULL_CODE)
            context.component.onPasswordChanged(VALID_PASSWORD)
            advanceTimeBy(100.milliseconds)
            context.component.onConfirmResetClick()
            advanceTimeBy(100.milliseconds)
            assertEquals(ONE_CALL, context.onFinishedCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmResetClick_resetError_surfacesError() = runComponentTest {
        val repo = ResetPasswordRepositoryMock()
        repo.remainingDelaySeconds = ZERO_RETRY
        val resetUseCase = ResetEmailPasswordUseCaseMock().apply {
            resultProvider = { _, _, _ -> AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE)) }
        }
        val context = createResetEmailPasswordComponentTestContext(repo, resetEmailPasswordUseCase = resetUseCase)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.sendResetPasswordConfirmationToEmailUseCase.resultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            context.component.onSendCodeClick()
            advanceTimeBy(100.milliseconds)
            context.component.onCodeChanged(FULL_CODE)
            context.component.onPasswordChanged(VALID_PASSWORD)
            advanceTimeBy(100.milliseconds)
            context.component.onConfirmResetClick()
            advanceTimeBy(100.milliseconds)
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(context.component.state.value)
            assertIs<CommonError.Unknown>(reset.actionError)
        } finally {
            context.destroy()
        }
    }

    private fun createResetEmailPasswordComponentTestContext(
        resetPasswordRepository: ResetPasswordRepositoryMock,
        sendResetPasswordConfirmationToEmailUseCase: SendResetPasswordConfirmationToEmailUseCaseMock = SendResetPasswordConfirmationToEmailUseCaseMock(),
        resetEmailPasswordUseCase: ResetEmailPasswordUseCaseMock = ResetEmailPasswordUseCaseMock(),
        validatePasswordUseCase: ValidatePasswordUseCaseMock = ValidatePasswordUseCaseMock()
    ): ResetEmailPasswordComponentTestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        
        val context = ResetEmailPasswordComponentTestContext(
            lifecycle = lifecycle,
            sendResetPasswordConfirmationToEmailUseCase = sendResetPasswordConfirmationToEmailUseCase,
            resetEmailPasswordUseCase = resetEmailPasswordUseCase,
            validatePasswordUseCase = validatePasswordUseCase
        )

        context.component = ResetEmailPasswordComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            resetPasswordRepository = resetPasswordRepository,
            sendResetPasswordConfirmationToEmailUseCase = sendResetPasswordConfirmationToEmailUseCase,
            resetEmailPasswordUseCase = resetEmailPasswordUseCase,
            validatePasswordUseCase = validatePasswordUseCase,
            onBack = { context.onBackCalls++ },
            onFinished = { context.onFinishedCalls++ }
        )
        
        return context
    }

    private class ResetEmailPasswordComponentTestContext(
        val lifecycle: LifecycleRegistry,
        val sendResetPasswordConfirmationToEmailUseCase: SendResetPasswordConfirmationToEmailUseCaseMock,
        val resetEmailPasswordUseCase: ResetEmailPasswordUseCaseMock,
        val validatePasswordUseCase: ValidatePasswordUseCaseMock
    ) {
        lateinit var component: ResetEmailPasswordComponentImpl
        var onBackCalls: Int = 0
        var onFinishedCalls: Int = 0

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
