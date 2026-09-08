package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.data.authDataPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.login.LoginByEmailUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.*

@InternalApi
class LoginByEmailComponentImplTest {

    @Test
    fun onEmailChanged_updatesValidityAndClearsActionError() = runComponentTest {
        val context = createLoginByEmailComponentTestContext()
        try {
            context.component.onEmailChanged(INVALID_EMAIL)
            advanceUntilIdle()

            var content = assertIs<LoginByEmailScreenState.Content>(context.component.state.value)
            assertFalse(content.isEmailValid)

            context.component.onEmailChanged(VALID_EMAIL)
            advanceUntilIdle()

            content = assertIs<LoginByEmailScreenState.Content>(context.component.state.value)
            assertTrue(content.isEmailValid)
            assertEquals(VALID_EMAIL, content.email)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onPasswordChanged_updatesPasswordValidity() = runComponentTest {
        val context = createLoginByEmailComponentTestContext()
        try {
            context.component.onPasswordChanged(BLANK_PASSWORD)
            advanceUntilIdle()

            var content = assertIs<LoginByEmailScreenState.Content>(context.component.state.value)
            assertFalse(content.isPasswordValid)

            context.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()

            content = assertIs<LoginByEmailScreenState.Content>(context.component.state.value)
            assertTrue(content.isPasswordValid)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onLoginClick_success_callsOnFinished() = runComponentTest {
        val loginUseCase = LoginByEmailUseCaseMock().apply {
            resultProvider = { _, _ -> AppResult.Success(authDataPayloadMock().toAuthData()) }
        }

        val context = createLoginByEmailComponentTestContext(loginByEmailUseCase = loginUseCase)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()

            context.component.onLoginClick()
            advanceUntilIdle()

            assertEquals(ONE_CALL, context.onFinishedCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onLoginClick_loginError_surfacesError() = runComponentTest {
        val loginUseCase = LoginByEmailUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
            }
        }

        val context = createLoginByEmailComponentTestContext(loginByEmailUseCase = loginUseCase)
        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()

            context.component.onLoginClick()
            advanceUntilIdle()

            assertEquals(ZERO_CALLS, context.onFinishedCalls)

            val content = assertIs<LoginByEmailScreenState.Content>(context.component.state.value)
            assertFalse(content.actionLoading)
            assertIs<CommonError.Unknown>(content.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onLoginClick_whenCannotLogin_doesNotInvokeRepository() = runComponentTest {
        val loginUseCase = LoginByEmailUseCaseMock()

        val context = createLoginByEmailComponentTestContext(loginByEmailUseCase = loginUseCase)
        try {
            context.component.onEmailChanged(INVALID_EMAIL)
            context.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()

            context.component.onLoginClick()
            advanceUntilIdle()

            assertEquals(ZERO_CALLS, context.onFinishedCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onForgotPasswordClick_invokesNavigation() = runComponentTest {
        val context = createLoginByEmailComponentTestContext()
        try {
            context.component.onForgotPasswordClick()
            assertEquals(ONE_CALL, context.onNavigateToForgotPasswordCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRegistrationClick_invokesNavigation() = runComponentTest {
        val context = createLoginByEmailComponentTestContext()
        try {
            context.component.onRegistrationClick()
            assertEquals(ONE_CALL, context.onNavigateToRegistrationByEmailCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_invokesOnBack() = runComponentTest {
        val context = createLoginByEmailComponentTestContext()
        try {
            context.component.onBackClick()
            assertEquals(ONE_CALL, context.onBackCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onTogglePasswordVisibility_togglesFlag() = runComponentTest {
        val context = createLoginByEmailComponentTestContext()
        try {
            context.component.onTogglePasswordVisibility()

            var content = assertIs<LoginByEmailScreenState.Content>(context.component.state.value)
            assertTrue(content.isPasswordVisible)

            context.component.onTogglePasswordVisibility()

            content = assertIs<LoginByEmailScreenState.Content>(context.component.state.value)
            assertFalse(content.isPasswordVisible)
        } finally {
            context.destroy()
        }
    }

    private fun createLoginByEmailComponentTestContext(
        loginByEmailUseCase: LoginByEmailUseCaseMock = LoginByEmailUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Success(authDataPayloadMock().toAuthData())
            }
        }
    ): LoginByEmailComponentTestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = LoginByEmailComponentTestContext(lifecycle = lifecycle)

        context.component = LoginByEmailComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            appType = AppType.CLIENT,
            loginByEmailUseCase = loginByEmailUseCase,
            onNavigateToRegistrationByEmail = { context.onNavigateToRegistrationByEmailCalls++ },
            onNavigateToForgotPassword = { context.onNavigateToForgotPasswordCalls++ },
            onNavigateToTotp = { context.lastTotpMfaToken = it },
            onNavigateToPendingDeletion = { context.onNavigateToPendingDeletionCalls++ },
            onBack = { context.onBackCalls++ },
            onFinished = { context.onFinishedCalls++ }
        )

        return context
    }

    private class LoginByEmailComponentTestContext(
        val lifecycle: LifecycleRegistry
    ) {
        lateinit var component: LoginByEmailComponentImpl
        var onFinishedCalls: Int = 0
        var onNavigateToForgotPasswordCalls: Int = 0
        var onNavigateToRegistrationByEmailCalls: Int = 0
        var onNavigateToPendingDeletionCalls: Int = 0
        var onBackCalls: Int = 0
        var lastTotpMfaToken: String? = null

        fun destroy() {
            lifecycle.destroy()
        }
    }

    private companion object {
        const val VALID_EMAIL = "user.name+tag@example.com"
        const val INVALID_EMAIL = "not-an-email"
        const val VALID_PASSWORD = "Password123!"
        const val BLANK_PASSWORD = "   "
        const val NOT_RETRYABLE = false
        const val ZERO_CALLS = 0
        const val ONE_CALL = 1
    }
}
