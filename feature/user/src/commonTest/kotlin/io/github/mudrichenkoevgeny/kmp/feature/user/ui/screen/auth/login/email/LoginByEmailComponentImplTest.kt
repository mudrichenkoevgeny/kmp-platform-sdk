package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.error.model.SecurityError
import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.toAuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.MockAuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.MockUserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireAuthDataResponse
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByEmailUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.model.PasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidatorImpl
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.test.runUserUiComponentTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LoginByEmailComponentImplTest {

    @Test
    fun onEmailChanged_updatesValidityAndClearsActionError() = runUserUiComponentTest {
        val harness = createHarness()
        try {
            harness.component.onEmailChanged(INVALID_EMAIL)
            var content = assertIs<LoginByEmailScreenState.Content>(harness.component.state.value)
            assertFalse(content.isEmailValid)

            harness.component.onEmailChanged(VALID_EMAIL)
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
        val authData = wireAuthDataResponse().toAuthData()
        val repo = FakeLoginRepository().apply { result = AppResult.Success(authData) }
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
        val repo = FakeLoginRepository().apply {
            result = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
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
        val policy = passwordPolicy(minLength = MIN_PASSWORD_LENGTH)
        val countingRepo = CountingSecuritySettingsRepository(
            successSettings = SecuritySettings(passwordPolicy = policy),
            failAfterSuccessInvocations = FIRST_VALIDATE_SUCCEEDS
        )
        val validatePassword = ValidatePasswordUseCase(countingRepo, PasswordPolicyValidatorImpl())
        val repo = FakeLoginRepository().apply {
            result = AppResult.Success(wireAuthDataResponse().toAuthData())
        }
        val harness = createHarness(loginRepository = repo, validatePasswordUseCase = validatePassword)
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()
            harness.component.onLoginClick()
            advanceUntilIdle()
            assertEquals(ZERO_CALLS, harness.counters.finished)
            assertNull(repo.lastEmail)
            val content = assertIs<LoginByEmailScreenState.Content>(harness.component.state.value)
            assertIs<SecurityError.PasswordPolicyUnavailable>(content.actionError)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onLoginClick_whenCannotLogin_doesNotInvokeRepository() = runUserUiComponentTest {
        val repo = FakeLoginRepository()
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
        loginRepository: FakeLoginRepository = FakeLoginRepository().apply {
            result = AppResult.Success(wireAuthDataResponse().toAuthData())
        },
        validatePasswordUseCase: ValidatePasswordUseCase = validatePasswordUseCaseDefault()
    ): Harness {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        val loginByEmailUseCase = LoginByEmailUseCase(
            loginRepository,
            MockAuthStorage(),
            MockUserStorage()
        )
        val counters = NavigationCounters()
        val component = LoginByEmailComponentImpl(
            componentContext = ctx,
            loginByEmailUseCase = loginByEmailUseCase,
            validatePasswordUseCase = validatePasswordUseCase,
            onNavigateToRegistrationByEmail = { counters.registration++ },
            onNavigateToForgotPassword = { counters.forgotPassword++ },
            onBack = { counters.back++ },
            onFinished = { counters.finished++ }
        )
        return Harness(lifecycle, component, counters)
    }

    private fun validatePasswordUseCaseDefault(): ValidatePasswordUseCase {
        val policy = passwordPolicy(minLength = MIN_PASSWORD_LENGTH)
        val repo = FakeSecuritySettingsRepository(AppResult.Success(SecuritySettings(passwordPolicy = policy)))
        return ValidatePasswordUseCase(repo, PasswordPolicyValidatorImpl())
    }

    private fun passwordPolicy(minLength: Int): PasswordPolicy = PasswordPolicy(
        minLength = minLength,
        requireLetter = false,
        requireUpperCase = false,
        requireLowerCase = false,
        requireDigit = false,
        requireSpecialChar = false,
        commonPasswords = emptySet()
    )

    private class FakeSecuritySettingsRepository(
        private val getResult: AppResult<SecuritySettings>
    ) : SecuritySettingsRepository {

        override suspend fun getSecuritySettings(): AppResult<SecuritySettings> = getResult

        override suspend fun refreshSecuritySettings(): AppResult<SecuritySettings> =
            AppResult.Error(CommonError.Unknown())

        override suspend fun updateSecuritySettings(securitySettings: SecuritySettings) = Unit

        override fun observeSecuritySettings(): Flow<SecuritySettings?> = flowOf(null)
    }

    /**
     * First [failAfterSuccessInvocations] calls return success; later calls return error so
     * [ValidatePasswordUseCase] yields [SecurityError.PasswordPolicyUnavailable].
     */
    private class CountingSecuritySettingsRepository(
        private val successSettings: SecuritySettings,
        private val failAfterSuccessInvocations: Int
    ) : SecuritySettingsRepository {

        private var getSecuritySettingsCalls = ZERO_CALLS

        override suspend fun getSecuritySettings(): AppResult<SecuritySettings> {
            getSecuritySettingsCalls++
            return if (getSecuritySettingsCalls <= failAfterSuccessInvocations) {
                AppResult.Success(successSettings)
            } else {
                AppResult.Error(CommonError.Unknown())
            }
        }

        override suspend fun refreshSecuritySettings(): AppResult<SecuritySettings> =
            AppResult.Error(CommonError.Unknown())

        override suspend fun updateSecuritySettings(securitySettings: SecuritySettings) = Unit

        override fun observeSecuritySettings(): Flow<SecuritySettings?> = flowOf(null)
    }

    private class FakeLoginRepository : LoginRepository {
        var result: AppResult<AuthData> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var lastEmail: String? = null
        var lastPassword: String? = null

        override suspend fun loginByEmail(email: String, password: String): AppResult<AuthData> {
            lastEmail = email
            lastPassword = password
            return result
        }

        override suspend fun loginByPhone(
            phoneNumber: String,
            confirmationCode: String
        ): AppResult<AuthData> = error(STUB_NOT_USED)

        override suspend fun loginByExternalAuthProvider(
            authProvider: UserAuthProvider,
            token: String
        ): AppResult<AuthData> = error(STUB_NOT_USED)

        override suspend fun sendLoginConfirmationToPhone(
            phoneNumber: String
        ): AppResult<SendConfirmationData> = error(STUB_NOT_USED)

        override fun getRemainingLoginConfirmationDelayInSeconds(phoneNumber: String): Int = ZERO_DELAY
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
        const val MIN_PASSWORD_LENGTH = 4
        const val VALID_PASSWORD = "ab12"
        const val SHORT_PASSWORD = "a"
        const val NOT_RETRYABLE = false
        const val STUB_NOT_USED = "stub"
        const val ZERO_DELAY = 0
        const val ZERO_CALLS = 0
        const val ONE_CALL = 1
        const val FIRST_VALIDATE_SUCCEEDS = 1
    }
}
