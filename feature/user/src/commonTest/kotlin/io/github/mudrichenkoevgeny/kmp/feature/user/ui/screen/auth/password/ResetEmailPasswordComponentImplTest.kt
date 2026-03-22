package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.password

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
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.useridentifier.toUserIdentifier
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.password.PasswordRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireUserIdentifierResponse
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.password.ResetEmailPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.password.SendResetPasswordConfirmationToEmailUseCase
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

class ResetEmailPasswordComponentImplTest {

    @Test
    fun onEmailChanged_invalidEmail_marksEmailInvalid() = runUserUiComponentTest {
        val repo = FakePasswordRepository()
        val harness = createHarness(repo)
        try {
            harness.component.onEmailChanged(INVALID_EMAIL)
            val emailState = assertIs<ResetEmailPasswordScreenState.EmailInput>(harness.component.state.value)
            assertFalse(emailState.isEmailValid)
            assertEquals(INVALID_EMAIL, emailState.email)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onEmailChanged_whenRemainingDelayPositive_skipsToResetInput() = runUserUiComponentTest {
        val repo = FakePasswordRepository()
        repo.remainingDelaySeconds = REMAINING_DELAY_SECONDS
        val harness = createHarness(repo)
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(harness.component.state.value)
            assertEquals(VALID_EMAIL, reset.email)
            assertEquals(REMAINING_DELAY_SECONDS, reset.resendTimerSeconds)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_success_movesToResetInput() = runUserUiComponentTest {
        val repo = FakePasswordRepository()
        repo.sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = RETRY_AFTER_SEND_SUCCESS))
        val harness = createHarness(repo)
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            runCurrent()
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(harness.component.state.value)
            assertEquals(VALID_EMAIL, reset.email)
            assertEquals(RETRY_AFTER_SEND_SUCCESS, reset.resendTimerSeconds)
            assertEquals(VALID_EMAIL, repo.lastSendEmail)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_tooManyRequests_movesToResetInputWithRetry() = runUserUiComponentTest {
        val repo = FakePasswordRepository()
        repo.sendResult = AppResult.Error(
            UserError.TooManyConfirmationRequests(retryAfterSeconds = RETRY_AFTER_RATE_LIMIT)
        )
        val harness = createHarness(repo)
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            runCurrent()
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(harness.component.state.value)
            assertEquals(RETRY_AFTER_RATE_LIMIT, reset.resendTimerSeconds)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_genericError_keepsEmailStepWithError() = runUserUiComponentTest {
        val repo = FakePasswordRepository()
        repo.sendResult = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        val harness = createHarness(repo)
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            val emailState = assertIs<ResetEmailPasswordScreenState.EmailInput>(harness.component.state.value)
            assertFalse(emailState.actionLoading)
            assertIs<CommonError.Unknown>(emailState.actionError)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onPasswordChanged_updatesPasswordValidityViaPolicy() = runUserUiComponentTest {
        val repo = FakePasswordRepository()
        repo.sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
        val harness = createHarness(repo, validatePasswordUseCase(minPasswordLength = MIN_PASSWORD_LENGTH))
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onPasswordChanged(SHORT_PASSWORD)
            advanceUntilIdle()
            var reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(harness.component.state.value)
            assertFalse(reset.isPasswordValid)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()
            reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(harness.component.state.value)
            assertTrue(reset.isPasswordValid)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onConfirmResetClick_success_callsOnFinished() = runUserUiComponentTest {
        val userId = wireUserIdentifierResponse(identifier = VALID_EMAIL).toUserIdentifier()
        val repo = FakePasswordRepository()
        repo.sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
        repo.resetResult = AppResult.Success(userId)
        val harness = createHarness(repo, validatePasswordUseCase(minPasswordLength = MIN_PASSWORD_LENGTH))
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onCodeChanged(FULL_CODE)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()
            harness.component.onConfirmResetClick()
            advanceUntilIdle()
            assertEquals(ONE_CALL, harness.counters.finished)
            assertEquals(VALID_EMAIL, repo.lastResetEmail)
            assertEquals(VALID_PASSWORD, repo.lastResetPassword)
            assertEquals(FULL_CODE, repo.lastResetCode)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onConfirmResetClick_resetError_surfacesError() = runUserUiComponentTest {
        val repo = FakePasswordRepository()
        repo.sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
        repo.resetResult = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        val harness = createHarness(repo, validatePasswordUseCase(minPasswordLength = MIN_PASSWORD_LENGTH))
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onCodeChanged(FULL_CODE)
            harness.component.onPasswordChanged(VALID_PASSWORD)
            advanceUntilIdle()
            harness.component.onConfirmResetClick()
            advanceUntilIdle()
            assertEquals(ZERO_CALLS, harness.counters.finished)
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(harness.component.state.value)
            assertFalse(reset.actionLoading)
            assertIs<CommonError.Unknown>(reset.actionError)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onBackClick_fromReset_returnsToEmailInput() = runUserUiComponentTest {
        val repo = FakePasswordRepository()
        repo.sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
        val harness = createHarness(repo)
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            assertIs<ResetEmailPasswordScreenState.ResetInput>(harness.component.state.value)
            harness.component.onBackClick()
            val emailState = assertIs<ResetEmailPasswordScreenState.EmailInput>(harness.component.state.value)
            assertEquals(VALID_EMAIL, emailState.email)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onBackClick_fromEmail_invokesOnBack() = runUserUiComponentTest {
        val repo = FakePasswordRepository()
        val harness = createHarness(repo)
        try {
            harness.component.onBackClick()
            assertEquals(ONE_CALL, harness.counters.back)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onResetEmailClick_returnsToEmailInput() = runUserUiComponentTest {
        val repo = FakePasswordRepository()
        repo.sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
        val harness = createHarness(repo)
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onResetEmailClick()
            val emailState = assertIs<ResetEmailPasswordScreenState.EmailInput>(harness.component.state.value)
            assertEquals(VALID_EMAIL, emailState.email)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onCodeChanged_ignoresInputLongerThanCodeLength() = runUserUiComponentTest {
        val repo = FakePasswordRepository()
        repo.sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
        val harness = createHarness(repo)
        try {
            harness.component.onEmailChanged(VALID_EMAIL)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onCodeChanged(FULL_CODE)
            harness.component.onCodeChanged(TOO_LONG_CODE)
            val reset = assertIs<ResetEmailPasswordScreenState.ResetInput>(harness.component.state.value)
            assertEquals(FULL_CODE, reset.code)
        } finally {
            harness.destroy()
        }
    }

    private fun createHarness(
        passwordRepository: FakePasswordRepository,
        validatePassword: ValidatePasswordUseCase = validatePasswordUseCase()
    ): Harness {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        val counters = NavigationCounters()
        val sendUseCase = SendResetPasswordConfirmationToEmailUseCase(passwordRepository)
        val resetUseCase = ResetEmailPasswordUseCase(passwordRepository)
        val component = ResetEmailPasswordComponentImpl(
            componentContext = ctx,
            passwordRepository = passwordRepository,
            sendResetPasswordConfirmationToEmailUseCase = sendUseCase,
            resetEmailPasswordUseCase = resetUseCase,
            validatePasswordUseCase = validatePassword,
            onBack = { counters.back++ },
            onFinished = { counters.finished++ }
        )
        return Harness(lifecycle, component, counters)
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

    private class FakePasswordRepository : PasswordRepository {
        var remainingDelaySeconds: Int = ZERO_RETRY
        var sendResult: AppResult<SendConfirmationData> =
            AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
        var resetResult: AppResult<UserIdentifier> =
            AppResult.Success(wireUserIdentifierResponse(identifier = VALID_EMAIL).toUserIdentifier())

        var lastSendEmail: String? = null
        var lastResetEmail: String? = null
        var lastResetPassword: String? = null
        var lastResetCode: String? = null

        override fun getRemainingResetPasswordConfirmationDelayInSeconds(email: String): Int =
            remainingDelaySeconds

        override suspend fun sendResetPasswordConfirmationToEmail(email: String): AppResult<SendConfirmationData> {
            lastSendEmail = email
            return sendResult
        }

        override suspend fun resetPassword(
            email: String,
            newPassword: String,
            confirmationCode: String
        ): AppResult<UserIdentifier> {
            lastResetEmail = email
            lastResetPassword = newPassword
            lastResetCode = confirmationCode
            return resetResult
        }
    }

    private class NavigationCounters(
        var finished: Int = ZERO_CALLS,
        var back: Int = ZERO_CALLS
    )

    private class Harness(
        private val lifecycle: LifecycleRegistry,
        val component: ResetEmailPasswordComponentImpl,
        val counters: NavigationCounters
    ) {
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
