package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.error.model.SecurityError
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.securitySettingsMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.repository.SecuritySettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.data.authDataPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.login.LoginRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.test.runUserUiComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByEmailUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidatorImpl
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.*

@InternalApi
class LoginByEmailComponentImplTest {

    @Test
    fun onEmailChanged_updatesValidityAndClearsActionError() = runUserUiComponentTest {
        val harness = createHarness()
        try {
            harness.component.onEmailChanged(INVALID_EMAIL)
            advanceUntilIdle()

            var content = assertIs<LoginByEmailScreenState.Content>(harness.component.state.value)
            assertFalse(content.isEmailValid)

            harness.component.onEmailChanged(VALID_EMAIL)
            advanceUntilIdle()

            content = assertIs<LoginByEmailScreenState.Content>(harness.component.state.value)
            assertTrue(content.isEmailValid)
            assertEquals(VALID_EMAIL, content.email)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onPasswordChanged_asyncValidation_updatesPasswordValidity() = runUserUiComponentTest {
        val harness = createHarness()
        try {
            harness.component.onPasswordChanged(SHORT_PASSWORD)
            advanceUntilIdle()

            var content = assertIs<LoginByEmailScreenState.Content>(harness.component.state.value)
            assertFalse(content.isPasswordValid)

            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()

            content = assertIs<LoginByEmailScreenState.Content>(harness.component.state.value)
            assertTrue(content.isPasswordValid)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onLoginClick_success_callsOnFinished() = runUserUiComponentTest {
        val repo = LoginRepositoryMock().apply {
            authDataResultProvider = { AppResult.Success(authDataPayloadMock().toAuthData()) }
        }

        val harness = createHarness(loginRepository = repo)
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()

            harness.component.onLoginClick()
            advanceUntilIdle()

            assertEquals(ONE_CALL, harness.counters.finished)
            assertEquals(VALID_EMAIL, repo.lastEmail)
            assertEquals(VALID_PASSWORD, repo.lastPassword)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onLoginClick_loginError_surfacesError() = runUserUiComponentTest {
        val repo = LoginRepositoryMock().apply {
            authDataResultProvider = {
                AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
            }
        }

        val harness = createHarness(loginRepository = repo)
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()

            harness.component.onLoginClick()
            advanceUntilIdle()

            assertEquals(ZERO_CALLS, harness.counters.finished)

            val content = assertIs<LoginByEmailScreenState.Content>(harness.component.state.value)
            assertFalse(content.actionLoading)
            assertIs<CommonError.Unknown>(content.actionError)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onLoginClick_secondValidatePasswordInvocationFailure_showsError() = runUserUiComponentTest {
        val secRepo = SecuritySettingsRepositoryMock().apply {
            resultProvider = {
                AppResult.Error(SecurityError.PasswordPolicyUnavailable())
            }
        }

        val validatePassword = ValidatePasswordUseCase(secRepo, PasswordPolicyValidatorImpl())
        val harness = createHarness(validatePasswordUseCase = validatePassword)

        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()

            harness.component.onLoginClick()
            advanceUntilIdle()

            assertEquals(ZERO_CALLS, harness.counters.finished)

            val content = assertIs<LoginByEmailScreenState.Content>(harness.component.state.value)
            assertIs<SecurityError.PasswordPolicyUnavailable>(content.actionError)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onLoginClick_whenCannotLogin_doesNotInvokeRepository() = runUserUiComponentTest {
        val repo = LoginRepositoryMock()

        val harness = createHarness(loginRepository = repo)
        try {
            harness.component.onEmailChanged(INVALID_EMAIL)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()

            harness.component.onLoginClick()
            advanceUntilIdle()

            assertNull(repo.lastEmail)
            assertEquals(ZERO_CALLS, harness.counters.finished)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onForgotPasswordClick_invokesNavigation() = runUserUiComponentTest {
        val harness = createHarness()
        try {
            harness.component.onForgotPasswordClick()
            assertEquals(ONE_CALL, harness.counters.forgotPassword)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onRegistrationClick_invokesNavigation() = runUserUiComponentTest {
        val harness = createHarness()
        try {
            harness.component.onRegistrationClick()
            assertEquals(ONE_CALL, harness.counters.registration)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onBackClick_invokesOnBack() = runUserUiComponentTest {
        val harness = createHarness()
        try {
            harness.component.onBackClick()
            assertEquals(ONE_CALL, harness.counters.back)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onTogglePasswordVisibility_togglesFlag() = runUserUiComponentTest {
        val harness = createHarness()
        try {
            harness.component.onTogglePasswordVisibility()

            var content = assertIs<LoginByEmailScreenState.Content>(harness.component.state.value)
            assertTrue(content.isPasswordVisible)

            harness.component.onTogglePasswordVisibility()

            content = assertIs<LoginByEmailScreenState.Content>(harness.component.state.value)
            assertFalse(content.isPasswordVisible)
        } finally {
            harness.destroy()
        }
    }

    private fun createHarness(
        loginRepository: LoginRepositoryMock = LoginRepositoryMock().apply {
            authDataResultProvider = {
                AppResult.Success(authDataPayloadMock().toAuthData())
            }
        },
        validatePasswordUseCase: ValidatePasswordUseCase? = null
    ): Harness {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val ctx = DefaultComponentContext(lifecycle)

        val loginByEmailUseCase = LoginByEmailUseCase(
            loginRepository,
            AuthStorageMock(),
            UserStorageMock()
        )

        val counters = NavigationCounters()

        val component = LoginByEmailComponentImpl(
            componentContext = ctx,
            loginByEmailUseCase = loginByEmailUseCase,
            validatePasswordUseCase = validatePasswordUseCase
                ?: validatePasswordUseCaseSuccess(),
            onNavigateToRegistrationByEmail = { counters.registration++ },
            onNavigateToForgotPassword = { counters.forgotPassword++ },
            onBack = { counters.back++ },
            onFinished = { counters.finished++ }
        )

        return Harness(lifecycle, component, counters)
    }

    private fun validatePasswordUseCaseSuccess(): ValidatePasswordUseCase {
        val secRepo = SecuritySettingsRepositoryMock().apply {
            resultProvider = {
                AppResult.Success(securitySettingsMock())
            }
        }

        return ValidatePasswordUseCase(secRepo, PasswordPolicyValidatorImpl())
    }

    private class NavigationCounters(
        var finished: Int = ZERO_CALLS,
        var forgotPassword: Int = ZERO_CALLS,
        var registration: Int = ZERO_CALLS,
        var back: Int = ZERO_CALLS
    )

    private class Harness(
        private val lifecycle: LifecycleRegistry,
        val component: LoginByEmailComponentImpl,
        val counters: NavigationCounters
    ) {
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