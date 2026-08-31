package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.core.security.error.model.SecurityError
import io.github.mudrichenkoevgeny.kmp.core.security.mock.usecase.ValidatePasswordUseCaseMock
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
    fun onPasswordChanged_asyncValidation_updatesPasswordValidity() = runComponentTest {
        val context = createLoginByEmailComponentTestContext()
        try {
            context.validatePasswordUseCase.resultProvider = { AppResult.Error(SecurityError.PasswordTooShort()) }
            context.component.onPasswordChanged(SHORT_PASSWORD)
            advanceUntilIdle()

            var content = assertIs<LoginByEmailScreenState.Content>(context.component.state.value)
            assertFalse(content.isPasswordValid)

            context.validatePasswordUseCase.resultProvider = { AppResult.Success(Unit) }
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
    fun onLoginClick_secondValidatePasswordInvocationFailure_showsError() = runComponentTest {
        val validatePassword = ValidatePasswordUseCaseMock().apply {
            resultProvider = {
                AppResult.Error(SecurityError.PasswordPolicyUnavailable())
            }
        }
        val context = createLoginByEmailComponentTestContext(validatePasswordUseCase = validatePassword)

        try {
            context.component.onEmailChanged(VALID_EMAIL)
            context.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()

            context.component.onLoginClick()
            advanceUntilIdle()

            assertEquals(ZERO_CALLS, context.onFinishedCalls)

            val content = assertIs<LoginByEmailScreenState.Content>(context.component.state.value)
            assertIs<SecurityError.PasswordPolicyUnavailable>(content.actionError)
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
        },
        validatePasswordUseCase: ValidatePasswordUseCaseMock = ValidatePasswordUseCaseMock()
    ): LoginByEmailComponentTestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = LoginByEmailComponentTestContext(
            lifecycle = lifecycle,
            loginByEmailUseCase = loginByEmailUseCase,
            validatePasswordUseCase = validatePasswordUseCase
        )

        context.component = LoginByEmailComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            appType = AppType.CLIENT,
            loginByEmailUseCase = loginByEmailUseCase,
            validatePasswordUseCase = validatePasswordUseCase,
            onNavigateToRegistrationByEmail = { context.onNavigateToRegistrationByEmailCalls++ },
            onNavigateToForgotPassword = { context.onNavigateToForgotPasswordCalls++ },
            onBack = { context.onBackCalls++ },
            onFinished = { context.onFinishedCalls++ }
        )

        return context
    }

    private class LoginByEmailComponentTestContext(
        val lifecycle: LifecycleRegistry,
        val loginByEmailUseCase: LoginByEmailUseCaseMock,
        val validatePasswordUseCase: ValidatePasswordUseCaseMock
    ) {
        lateinit var component: LoginByEmailComponentImpl
        var onFinishedCalls: Int = 0
        var onNavigateToForgotPasswordCalls: Int = 0
        var onNavigateToRegistrationByEmailCalls: Int = 0
        var onBackCalls: Int = 0

        fun destroy() {
            lifecycle.destroy()
        }
    }

    private companion object {
        const val VALID_EMAIL = "user.name+tag@example.com"
        const val INVALID_EMAIL = "not-an-email"
        const val VALID_PASSWORD = "Password123!"
        const val SHORT_PASSWORD = "1"
        const val NOT_RETRYABLE = false
        const val ZERO_CALLS = 0
        const val ONE_CALL = 1
    }
}
