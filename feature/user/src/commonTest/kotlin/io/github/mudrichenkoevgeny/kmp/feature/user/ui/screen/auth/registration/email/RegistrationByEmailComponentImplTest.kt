package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.registration.email

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.toAuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.MockAuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.MockUserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration.RegistrationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireAuthDataResponse
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.RegistrationByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.SendRegistrationConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.model.PasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidatorImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.test.runUserUiComponentTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class RegistrationByEmailComponentImplTest {

    @Test
    fun onEmailChanged_invalidEmail_marksEmailInvalid() = runUserUiComponentTest {
        val repo = FakeRegistrationRepository()
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(INVALID_EMAIL)
            val emailState = assertIs<RegistrationByEmailScreenState.EmailInput>(harness.component.state.value)
            assertFalse(emailState.isEmailValid)
            assertEquals(INVALID_EMAIL, emailState.email)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onEmailChanged_whenRemainingDelayPositive_skipsToRegistrationInput() = runUserUiComponentTest {
        val repo = FakeRegistrationRepository(remainingDelaySeconds = REMAINING_DELAY_SECONDS)
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            val reg = assertIs<RegistrationByEmailScreenState.RegistrationInput>(harness.component.state.value)
            assertEquals(VALID_EMAIL, reg.email)
            assertEquals(REMAINING_DELAY_SECONDS, reg.resendTimerSeconds)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_success_movesToRegistrationInput() = runUserUiComponentTest {
        val repo = FakeRegistrationRepository(
            sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = RETRY_AFTER_SEND_SUCCESS))
        )
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            runCurrent()
            val reg = assertIs<RegistrationByEmailScreenState.RegistrationInput>(harness.component.state.value)
            assertEquals(VALID_EMAIL, reg.email)
            assertEquals(RETRY_AFTER_SEND_SUCCESS, reg.resendTimerSeconds)
            assertEquals(VALID_EMAIL, repo.lastSendEmail)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_tooManyRequests_movesToRegistrationInputWithRetryFromError() = runUserUiComponentTest {
        val repo = FakeRegistrationRepository(
            sendResult = AppResult.Error(UserError.TooManyConfirmationRequests(retryAfterSeconds = RETRY_AFTER_RATE_LIMIT))
        )
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            runCurrent()
            val reg = assertIs<RegistrationByEmailScreenState.RegistrationInput>(harness.component.state.value)
            assertEquals(RETRY_AFTER_RATE_LIMIT, reg.resendTimerSeconds)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_genericError_keepsEmailStepWithError() = runUserUiComponentTest {
        val repo = FakeRegistrationRepository(
            sendResult = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        )
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            val emailState = assertIs<RegistrationByEmailScreenState.EmailInput>(harness.component.state.value)
            assertFalse(emailState.actionLoading)
            assertIs<CommonError.Unknown>(emailState.actionError)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onPasswordChanged_updatesPasswordValidityViaPolicy() = runUserUiComponentTest {
        val repo = FakeRegistrationRepository(
            sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
        )
        val harness = ComponentHarness(repo, validatePasswordUseCase(minPasswordLength = MIN_PASSWORD_LENGTH))
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
        val authData = wireAuthDataResponse().toAuthData()
        val repo = FakeRegistrationRepository(
            sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY)),
            registerResult = AppResult.Success(authData)
        )
        val harness = ComponentHarness(repo, validatePasswordUseCase(minPasswordLength = MIN_PASSWORD_LENGTH))
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
            assertEquals(VALID_EMAIL, repo.lastRegisterEmail)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onRegisterClick_registerError_surfacesError() = runUserUiComponentTest {
        val repo = FakeRegistrationRepository(
            sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY)),
            registerResult = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        )
        val harness = ComponentHarness(repo, validatePasswordUseCase(minPasswordLength = MIN_PASSWORD_LENGTH))
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onCodeChanged(FULL_CODE)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()
            harness.component.onRegisterClick()
            advanceUntilIdle()
            assertEquals(ZERO_CALLS, harness.onFinishedCalls)
            val reg = assertIs<RegistrationByEmailScreenState.RegistrationInput>(harness.component.state.value)
            assertFalse(reg.actionLoading)
            assertIs<CommonError.Unknown>(reg.actionError)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onBackClick_fromRegistration_returnsToEmailInput() = runUserUiComponentTest {
        val repo = FakeRegistrationRepository(
            sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
        )
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            assertIs<RegistrationByEmailScreenState.RegistrationInput>(harness.component.state.value)
            harness.component.onBackClick()
            val emailState = assertIs<RegistrationByEmailScreenState.EmailInput>(harness.component.state.value)
            assertEquals(VALID_EMAIL, emailState.email)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onBackClick_fromEmail_invokesOnBack() = runUserUiComponentTest {
        val repo = FakeRegistrationRepository()
        val harness = ComponentHarness(repo, validatePasswordUseCase())
        try {
            harness.component.onBackClick()
            assertEquals(ONE_CALL, harness.onBackCalls)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onTogglePasswordVisibility_togglesFlag() = runUserUiComponentTest {
        val repo = FakeRegistrationRepository(
            sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
        )
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
        val repo = FakeRegistrationRepository(
            sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
        )
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

    private fun validatePasswordUseCase(minPasswordLength: Int = MIN_PASSWORD_LENGTH): ValidatePasswordUseCase {
        val policy = PasswordPolicy(
            minLength = minPasswordLength,
            requireLetter = false,
            requireUpperCase = false,
            requireLowerCase = false,
            requireDigit = false,
            requireSpecialChar = false,
            commonPasswords = emptySet()
        )
        val repo = FakeSecuritySettingsRepository(
            AppResult.Success(SecuritySettings(passwordPolicy = policy))
        )
        return ValidatePasswordUseCase(repo, PasswordPolicyValidatorImpl())
    }

    private class FakeSecuritySettingsRepository(
        private val getResult: AppResult<SecuritySettings>
    ) : SecuritySettingsRepository {

        override suspend fun getSecuritySettings(): AppResult<SecuritySettings> = getResult

        override suspend fun refreshSecuritySettings(): AppResult<SecuritySettings> =
            AppResult.Error(CommonError.Unknown())

        override suspend fun updateSecuritySettings(securitySettings: SecuritySettings) = Unit

        override fun observeSecuritySettings(): Flow<SecuritySettings?> = flowOf(null)
    }

    private class FakeRegistrationRepository(
        var remainingDelaySeconds: Int = ZERO_RETRY,
        var sendResult: AppResult<SendConfirmationData> = AppResult.Success(SendConfirmationData(ZERO_RETRY)),
        var registerResult: AppResult<AuthData> = AppResult.Success(wireAuthDataResponse().toAuthData())
    ) : RegistrationRepository {

        var lastSendEmail: String? = null
        var lastRegisterEmail: String? = null

        override fun getRemainingRegistrationConfirmationDelayInSeconds(email: String): Int = remainingDelaySeconds

        override suspend fun sendRegistrationConfirmationToEmail(email: String): AppResult<SendConfirmationData> {
            lastSendEmail = email
            return sendResult
        }

        override suspend fun registerByEmail(
            email: String,
            password: String,
            confirmationCode: String
        ): AppResult<AuthData> {
            lastRegisterEmail = email
            return registerResult
        }
    }

    private class ComponentHarness(
        registrationRepository: FakeRegistrationRepository,
        validatePasswordUseCase: ValidatePasswordUseCase
    ) {
        var onBackCalls: Int = ZERO_CALLS
        var onFinishedCalls: Int = ZERO_CALLS

        private val lifecycle = LifecycleRegistry()

        val component: RegistrationByEmailComponentImpl

        init {
            lifecycle.resume()
            val ctx = DefaultComponentContext(lifecycle)
            component = RegistrationByEmailComponentImpl(
                componentContext = ctx,
                registrationRepository = registrationRepository,
                sendRegistrationConfirmationToEmailUseCase = SendRegistrationConfirmationToEmailUseCase(
                    registrationRepository
                ),
                registrationByEmailUseCase = RegistrationByEmailUseCase(
                    registrationRepository,
                    MockAuthStorage(),
                    MockUserStorage()
                ),
                validatePasswordUseCase = validatePasswordUseCase,
                onBack = { onBackCalls++ },
                onFinished = { onFinishedCalls++ }
            )
        }

        fun destroy() {
            lifecycle.destroy()
        }
    }

    private companion object {
        const val VALID_EMAIL = "user.name+tag@example.com"
        const val INVALID_EMAIL = "not-an-email"
        const val REMAINING_DELAY_SECONDS = 45
        const val RETRY_AFTER_SEND_SUCCESS = 12
        const val RETRY_AFTER_RATE_LIMIT = 30
        const val ZERO_RETRY = 0
        const val MIN_PASSWORD_LENGTH = 4
        const val VALID_PASSWORD = "ab12"
        const val SHORT_PASSWORD = "a"
        const val FULL_CODE = "123456"
        const val TOO_LONG_CODE = "1234567"
        const val NOT_RETRYABLE = false
        const val ZERO_CALLS = 0
        const val ONE_CALL = 1
    }
}
