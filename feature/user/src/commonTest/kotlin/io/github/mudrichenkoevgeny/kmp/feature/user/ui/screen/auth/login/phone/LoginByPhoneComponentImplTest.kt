package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.phone

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.toAuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.MockAuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.MockUserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireAuthDataResponse
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.SendLoginConfirmationToPhoneUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.test.runUserUiComponentTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull

class LoginByPhoneComponentImplTest {

    @Test
    fun onPhoneChanged_invalidPhone_marksInvalid() = runUserUiComponentTest {
        val harness = createHarness()
        try {
            harness.component.onPhoneChanged(INVALID_PHONE)
            val phone = assertIs<LoginByPhoneScreenState.PhoneInput>(harness.component.state.value)
            assertFalse(phone.isPhoneNumberValid)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onPhoneChanged_validPhone_whenRemainingDelayPositive_movesToCodeInput() = runUserUiComponentTest {
        val repo = FakeLoginRepository().apply {
            remainingDelaySeconds = REMAINING_DELAY_SECONDS
        }
        val harness = createHarness(repo)
        try {
            harness.component.onPhoneChanged(VALID_PHONE)
            val code = assertIs<LoginByPhoneScreenState.CodeInput>(harness.component.state.value)
            assertEquals(VALID_PHONE, code.phoneNumber)
            assertEquals(REMAINING_DELAY_SECONDS, code.resendTimerSeconds)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_success_movesToCodeInput() = runUserUiComponentTest {
        val repo = FakeLoginRepository().apply {
            sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = RETRY_AFTER_SEND))
        }
        val harness = createHarness(repo)
        try {
            harness.component.onPhoneChanged(VALID_PHONE)
            harness.component.onSendCodeClick()
            runCurrent()
            val code = assertIs<LoginByPhoneScreenState.CodeInput>(harness.component.state.value)
            assertEquals(VALID_PHONE, code.phoneNumber)
            assertEquals(RETRY_AFTER_SEND, code.resendTimerSeconds)
            assertEquals(VALID_PHONE, repo.lastSendPhone)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_tooManyRequests_movesToCodeInputWithRetry() = runUserUiComponentTest {
        val repo = FakeLoginRepository().apply {
            sendResult = AppResult.Error(UserError.TooManyConfirmationRequests(retryAfterSeconds = RETRY_AFTER_RATE_LIMIT))
        }
        val harness = createHarness(repo)
        try {
            harness.component.onPhoneChanged(VALID_PHONE)
            harness.component.onSendCodeClick()
            runCurrent()
            val code = assertIs<LoginByPhoneScreenState.CodeInput>(harness.component.state.value)
            assertEquals(RETRY_AFTER_RATE_LIMIT, code.resendTimerSeconds)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_genericError_keepsPhoneStepWithError() = runUserUiComponentTest {
        val repo = FakeLoginRepository().apply {
            sendResult = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        }
        val harness = createHarness(repo)
        try {
            harness.component.onPhoneChanged(VALID_PHONE)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            val phone = assertIs<LoginByPhoneScreenState.PhoneInput>(harness.component.state.value)
            assertFalse(phone.actionLoading)
            assertIs<CommonError.Unknown>(phone.actionError)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onCodeChanged_fullCode_triggersLogin_andOnFinished() = runUserUiComponentTest {
        val authData = wireAuthDataResponse().toAuthData()
        val repo = FakeLoginRepository().apply {
            sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
            loginByPhoneResult = AppResult.Success(authData)
        }
        val harness = createHarness(repo)
        try {
            harness.component.onPhoneChanged(VALID_PHONE)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onCodeChanged(FULL_CODE)
            advanceUntilIdle()
            assertEquals(ONE_CALL, harness.counters.finished)
            assertEquals(VALID_PHONE, repo.lastLoginPhone)
            assertEquals(FULL_CODE, repo.lastLoginCode)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onConfirmCodeClick_loginError_surfacesError() = runUserUiComponentTest {
        val repo = FakeLoginRepository().apply {
            sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
            loginByPhoneResult = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        }
        val harness = createHarness(repo)
        try {
            harness.component.onPhoneChanged(VALID_PHONE)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onCodeChanged(FULL_CODE)
            advanceUntilIdle()
            assertEquals(ZERO_CALLS, harness.counters.finished)
            val code = assertIs<LoginByPhoneScreenState.CodeInput>(harness.component.state.value)
            assertFalse(code.actionLoading)
            assertIs<CommonError.Unknown>(code.actionError)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onConfirmCodeClick_whenCodeIncomplete_doesNotCallRepository() = runUserUiComponentTest {
        val repo = FakeLoginRepository().apply {
            sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
            loginByPhoneResult = AppResult.Success(wireAuthDataResponse().toAuthData())
        }
        val harness = createHarness(repo)
        try {
            harness.component.onPhoneChanged(VALID_PHONE)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onConfirmCodeClick()
            advanceUntilIdle()
            assertNull(repo.lastLoginPhone)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onResetPhoneClick_returnsToPhoneInput_preservingNumber() = runUserUiComponentTest {
        val repo = FakeLoginRepository().apply {
            sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
        }
        val harness = createHarness(repo)
        try {
            harness.component.onPhoneChanged(VALID_PHONE)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            assertIs<LoginByPhoneScreenState.CodeInput>(harness.component.state.value)
            harness.component.onResetPhoneClick()
            val phone = assertIs<LoginByPhoneScreenState.PhoneInput>(harness.component.state.value)
            assertEquals(VALID_PHONE, phone.phoneNumber)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onBackClick_fromCodeStep_returnsToPhoneInput() = runUserUiComponentTest {
        val repo = FakeLoginRepository().apply {
            sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
        }
        val harness = createHarness(repo)
        try {
            harness.component.onPhoneChanged(VALID_PHONE)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onBackClick()
            assertIs<LoginByPhoneScreenState.PhoneInput>(harness.component.state.value)
            assertEquals(ZERO_CALLS, harness.counters.back)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onBackClick_fromPhoneStep_invokesOnBack() = runUserUiComponentTest {
        val harness = createHarness()
        try {
            harness.component.onBackClick()
            assertEquals(ONE_CALL, harness.counters.back)
        } finally {
            harness.destroy()
        }
    }

    private fun createHarness(
        loginRepository: FakeLoginRepository = FakeLoginRepository().apply {
            sendResult = AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
            loginByPhoneResult = AppResult.Success(wireAuthDataResponse().toAuthData())
        }
    ): Harness {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        val counters = NavigationCounters()
        val sendUseCase = SendLoginConfirmationToPhoneUseCase(loginRepository)
        val loginByPhoneUseCase = LoginByPhoneUseCase(
            loginRepository,
            MockAuthStorage(),
            MockUserStorage()
        )
        val component = LoginByPhoneComponentImpl(
            componentContext = ctx,
            loginRepository = loginRepository,
            sendLoginConfirmationToPhoneUseCase = sendUseCase,
            loginByPhoneUseCase = loginByPhoneUseCase,
            onBack = { counters.back++ },
            onFinished = { counters.finished++ }
        )
        return Harness(lifecycle, component, counters)
    }

    private class FakeLoginRepository : LoginRepository {
        var remainingDelaySeconds: Int = ZERO_RETRY
        var sendResult: AppResult<SendConfirmationData> =
            AppResult.Success(SendConfirmationData(retryAfterSeconds = ZERO_RETRY))
        var loginByPhoneResult: AppResult<AuthData> =
            AppResult.Success(wireAuthDataResponse().toAuthData())

        var lastSendPhone: String? = null
        var lastLoginPhone: String? = null
        var lastLoginCode: String? = null

        override fun getRemainingLoginConfirmationDelayInSeconds(phoneNumber: String): Int = remainingDelaySeconds

        override suspend fun sendLoginConfirmationToPhone(phoneNumber: String): AppResult<SendConfirmationData> {
            lastSendPhone = phoneNumber
            return sendResult
        }

        override suspend fun loginByPhone(phoneNumber: String, confirmationCode: String): AppResult<AuthData> {
            lastLoginPhone = phoneNumber
            lastLoginCode = confirmationCode
            return loginByPhoneResult
        }

        override suspend fun loginByEmail(email: String, password: String): AppResult<AuthData> =
            error(STUB_NOT_USED)

        override suspend fun loginByExternalAuthProvider(
            authProvider: UserAuthProvider,
            token: String
        ): AppResult<AuthData> = error(STUB_NOT_USED)
    }

    private class NavigationCounters(
        var finished: Int = ZERO_CALLS,
        var back: Int = ZERO_CALLS
    )

    private class Harness(
        private val lifecycle: LifecycleRegistry,
        val component: LoginByPhoneComponentImpl,
        val counters: NavigationCounters
    ) {
        fun destroy() {
            lifecycle.destroy()
        }
    }

    private companion object {
        const val VALID_PHONE = "0123456789"
        const val INVALID_PHONE = "012"
        const val FULL_CODE = "123456"
        const val REMAINING_DELAY_SECONDS = 25
        const val RETRY_AFTER_SEND = 15
        const val RETRY_AFTER_RATE_LIMIT = 40
        const val ZERO_RETRY = 0
        const val NOT_RETRYABLE = false
        const val STUB_NOT_USED = "stub"
        const val ZERO_CALLS = 0
        const val ONE_CALL = 1
    }
}
