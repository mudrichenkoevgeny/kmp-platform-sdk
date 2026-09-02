package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.data.authDataPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.login.LoginByTotpRecoveryCodeUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.login.LoginByTotpUseCaseMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.*

@InternalApi
class LoginByTotpComponentImplTest {

    @Test
    fun onCodeChanged_updatesCodeAndClearsActionError() = runComponentTest {
        val context = createLoginByTotpComponentTestContext()
        try {
            context.component.onCodeChanged(VALID_TOTP_CODE)
            advanceUntilIdle()

            val content = assertIs<LoginByTotpScreenState.Content>(context.component.state.value)
            assertEquals(VALID_TOTP_CODE, content.code)
            assertNull(content.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onToggleModeClick_switchesModeAndClearsCode() = runComponentTest {
        val context = createLoginByTotpComponentTestContext()
        try {
            context.component.onCodeChanged(VALID_TOTP_CODE)
            advanceUntilIdle()

            context.component.onToggleModeClick()
            advanceUntilIdle()

            var content = assertIs<LoginByTotpScreenState.Content>(context.component.state.value)
            assertEquals(LoginByTotpScreenState.Mode.RECOVERY_CODE, content.mode)
            assertEquals("", content.code)
            assertNull(content.actionError)

            context.component.onToggleModeClick()
            advanceUntilIdle()

            content = assertIs<LoginByTotpScreenState.Content>(context.component.state.value)
            assertEquals(LoginByTotpScreenState.Mode.TOTP, content.mode)
            assertEquals("", content.code)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSubmitClick_totpMode_success_callsOnFinished() = runComponentTest {
        val totpUseCase = LoginByTotpUseCaseMock().apply {
            resultProvider = { _, _ -> AppResult.Success(authDataPayloadMock().toAuthData()) }
        }

        val context = createLoginByTotpComponentTestContext(loginByTotpUseCase = totpUseCase)
        try {
            context.component.onCodeChanged(VALID_TOTP_CODE)
            advanceUntilIdle()

            context.component.onSubmitClick()
            advanceUntilIdle()

            assertEquals(ONE_CALL, context.onFinishedCalls)
            assertEquals(ONE_CALL, totpUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSubmitClick_recoveryCodeMode_success_callsOnFinished() = runComponentTest {
        val recoveryUseCase = LoginByTotpRecoveryCodeUseCaseMock().apply {
            resultProvider = { _, _ -> AppResult.Success(authDataPayloadMock().toAuthData()) }
        }

        val context = createLoginByTotpComponentTestContext(loginByTotpRecoveryCodeUseCase = recoveryUseCase)
        try {
            context.component.onToggleModeClick()
            context.component.onCodeChanged(VALID_RECOVERY_CODE)
            advanceUntilIdle()

            context.component.onSubmitClick()
            advanceUntilIdle()

            assertEquals(ONE_CALL, context.onFinishedCalls)
            assertEquals(ONE_CALL, recoveryUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSubmitClick_failure_surfacesError() = runComponentTest {
        val totpUseCase = LoginByTotpUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
            }
        }

        val context = createLoginByTotpComponentTestContext(loginByTotpUseCase = totpUseCase)
        try {
            context.component.onCodeChanged(VALID_TOTP_CODE)
            advanceUntilIdle()

            context.component.onSubmitClick()
            advanceUntilIdle()

            assertEquals(ZERO_CALLS, context.onFinishedCalls)

            val content = assertIs<LoginByTotpScreenState.Content>(context.component.state.value)
            assertFalse(content.actionLoading)
            assertIs<CommonError.Unknown>(content.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSubmitClick_whenCannotSubmit_doesNotInvokeUseCases() = runComponentTest {
        val totpUseCase = LoginByTotpUseCaseMock()
        val recoveryUseCase = LoginByTotpRecoveryCodeUseCaseMock()

        val context = createLoginByTotpComponentTestContext(
            loginByTotpUseCase = totpUseCase,
            loginByTotpRecoveryCodeUseCase = recoveryUseCase
        )
        try {
            context.component.onCodeChanged(INVALID_CODE)
            advanceUntilIdle()

            context.component.onSubmitClick()
            advanceUntilIdle()

            assertEquals(ZERO_CALLS, context.onFinishedCalls)
            assertEquals(ZERO_CALLS, totpUseCase.executeCalls)
            assertEquals(ZERO_CALLS, recoveryUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_invokesOnBack() = runComponentTest {
        val context = createLoginByTotpComponentTestContext()
        try {
            context.component.onBackClick()
            assertEquals(ONE_CALL, context.onBackCalls)
        } finally {
            context.destroy()
        }
    }

    private fun createLoginByTotpComponentTestContext(
        mfaToken: String = MFA_TOKEN,
        loginByTotpUseCase: LoginByTotpUseCaseMock = LoginByTotpUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Success(authDataPayloadMock().toAuthData())
            }
        },
        loginByTotpRecoveryCodeUseCase: LoginByTotpRecoveryCodeUseCaseMock = LoginByTotpRecoveryCodeUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Success(authDataPayloadMock().toAuthData())
            }
        }
    ): LoginByTotpComponentTestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = LoginByTotpComponentTestContext(
            lifecycle = lifecycle
        )

        context.component = LoginByTotpComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            mfaToken = mfaToken,
            loginByTotpUseCase = loginByTotpUseCase,
            loginByTotpRecoveryCodeUseCase = loginByTotpRecoveryCodeUseCase,
            onNavigateToPendingDeletion = { context.onNavigateToPendingDeletionCalls++ },
            onBack = { context.onBackCalls++ },
            onFinished = { context.onFinishedCalls++ }
        )

        return context
    }

    private class LoginByTotpComponentTestContext(
        val lifecycle: LifecycleRegistry
    ) {
        lateinit var component: LoginByTotpComponentImpl
        var onFinishedCalls: Int = 0
        var onNavigateToPendingDeletionCalls: Int = 0
        var onBackCalls: Int = 0

        fun destroy() {
            lifecycle.destroy()
        }
    }

    private companion object {
        const val MFA_TOKEN = "test-mfa-challenge-token"
        const val VALID_TOTP_CODE = "123456"
        const val VALID_RECOVERY_CODE = "ABCD-1234-EFGH"
        const val INVALID_CODE = ""
        const val NOT_RETRYABLE = false
        const val ZERO_CALLS = 0
        const val ONE_CALL = 1
    }
}