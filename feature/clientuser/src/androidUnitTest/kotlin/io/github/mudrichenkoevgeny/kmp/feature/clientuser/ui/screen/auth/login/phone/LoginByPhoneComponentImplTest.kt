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
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.SendLoginConfirmationToPhoneUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

@InternalApi
class LoginByPhoneComponentImplTest {

    @Test
    fun onPhoneChanged_invalidPhone_marksInvalid() = runComponentTest {
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
    fun onPhoneChanged_validPhone_whenRemainingDelayPositive_movesToCodeInput() = runComponentTest {
        val repo = createDefaultLoginRepository().apply {
            remainingDelayProvider = { REMAINING_DELAY_SECONDS }
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
    fun onSendCodeClick_success_movesToCodeInput() = runComponentTest {
        val repo = createDefaultLoginRepository().apply {
            otpConfirmationResultProvider = {
                AppResult.Success(otpConfirmationMock(retryAfterSeconds = RETRY_AFTER_SEND))
            }
        }
        val harness = createHarness(repo)
        try {
            harness.component.onPhoneChanged(VALID_PHONE)
            harness.component.onSendCodeClick()
            runCurrent()
            val code = assertIs<LoginByPhoneScreenState.CodeInput>(harness.component.state.value)
            assertEquals(VALID_PHONE, code.phoneNumber)
            assertEquals(RETRY_AFTER_SEND, code.resendTimerSeconds)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onSendCodeClick_tooManyRequests_movesToCodeInputWithRetry() = runComponentTest {
        val repo = createDefaultLoginRepository().apply {
            otpConfirmationResultProvider = {
                AppResult.Error(UserError.TooManyConfirmationRequests(retryAfterSeconds = RETRY_AFTER_RATE_LIMIT))
            }
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
    fun onSendCodeClick_genericError_keepsPhoneStepWithError() = runComponentTest {
        val repo = createDefaultLoginRepository().apply {
            otpConfirmationResultProvider = {
                AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
            }
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
    fun onCodeChanged_fullCode_triggersLogin_andOnFinished() = runComponentTest {
        val authData = authDataPayloadMock().toAuthData()
        val repo = createDefaultLoginRepository().apply {
            otpConfirmationResultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            authDataResultProvider = { AppResult.Success(authData) }
        }
        val harness = createHarness(repo)
        try {
            harness.component.onPhoneChanged(VALID_PHONE)
            harness.component.onSendCodeClick()
            advanceUntilIdle()
            harness.component.onCodeChanged(FULL_CODE)
            advanceUntilIdle()
            assertEquals(ONE_CALL, harness.counters.finished)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onConfirmCodeClick_loginError_surfacesError() = runComponentTest {
        val repo = createDefaultLoginRepository().apply {
            otpConfirmationResultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
            authDataResultProvider = { AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE)) }
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
    fun onConfirmCodeClick_whenCodeIncomplete_doesNotCallRepository() = runComponentTest {
        val harness = createHarness()
        try {
            harness.component.onPhoneChanged(VALID_PHONE)
            harness.component.onSendCodeClick()
            runCurrent()
            harness.component.onConfirmCodeClick()
            runCurrent()
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onResetPhoneClick_returnsToPhoneInput_preservingNumber() = runComponentTest {
        val harness = createHarness()
        try {
            harness.component.onPhoneChanged(VALID_PHONE)
            harness.component.onSendCodeClick()
            runCurrent()
            assertIs<LoginByPhoneScreenState.CodeInput>(harness.component.state.value)
            harness.component.onResetPhoneClick()
            val phone = assertIs<LoginByPhoneScreenState.PhoneInput>(harness.component.state.value)
            assertEquals(VALID_PHONE, phone.phoneNumber)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onBackClick_fromCodeStep_returnsToPhoneInput() = runComponentTest {
        val harness = createHarness()
        try {
            harness.component.onPhoneChanged(VALID_PHONE)
            harness.component.onSendCodeClick()
            runCurrent()
            harness.component.onBackClick()
            assertIs<LoginByPhoneScreenState.PhoneInput>(harness.component.state.value)
            assertEquals(ZERO_CALLS, harness.counters.back)
        } finally {
            harness.destroy()
        }
    }

    @Test
    fun onBackClick_fromPhoneStep_invokesOnBack() = runComponentTest {
        val harness = createHarness()
        try {
            harness.component.onBackClick()
            assertEquals(ONE_CALL, harness.counters.back)
        } finally {
            harness.destroy()
        }
    }

    private fun createDefaultLoginRepository(): LoginRepositoryMock = LoginRepositoryMock().apply {
        otpConfirmationResultProvider = { AppResult.Success(otpConfirmationMock(retryAfterSeconds = ZERO_RETRY)) }
        authDataResultProvider = { AppResult.Success(authDataPayloadMock().toAuthData()) }
    }

    private fun createHarness(
        loginRepository: LoginRepositoryMock = createDefaultLoginRepository()
    ): Harness {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        val counters = NavigationCounters()

        val sendLoginConfirmationToPhoneUseCase = SendLoginConfirmationToPhoneUseCase(loginRepository)
        val loginByPhoneUseCase = LoginByPhoneUseCase(
            loginRepository = loginRepository,
            authStorage = AuthStorageMock(),
            userStorage = UserStorageMock()
        )

        val component = LoginByPhoneComponentImpl(
            componentContext = ctx,
            loginRepository = loginRepository,
            sendLoginConfirmationToPhoneUseCase = sendLoginConfirmationToPhoneUseCase,
            loginByPhoneUseCase = loginByPhoneUseCase,
            onBack = { counters.back++ },
            onFinished = { counters.finished++ }
        )
        return Harness(lifecycle, component, counters)
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
        const val ZERO_CALLS = 0
        const val ONE_CALL = 1
    }
}