package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.UserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.DisableTotpUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.EnableTotpUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.GetRecoveryCodesUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.RegenerateRecoveryCodesUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.SetupTotpUseCaseMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class TotpSettingsComponentImplTest {

    @Test
    fun init_whenUserNull_emitsError() = runComponentTest {
        val userRepository = UserRepositoryMock()
        val context = createTotpSettingsComponentTestContext(userRepository = userRepository)
        try {
            userRepository.emit(null)
            runCurrent()
            val state = assertIs<TotpSettingsScreenState.Error>(context.component.state.value)
            assertIs<CommonError.Unknown>(state.error)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun init_whenTotpDisabled_emitsDisabled() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = false)
        val userRepository = UserRepositoryMock()
        val context = createTotpSettingsComponentTestContext(userRepository = userRepository)
        try {
            userRepository.emit(user)
            runCurrent()
            assertIs<TotpSettingsScreenState.Disabled>(context.component.state.value)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun init_whenTotpEnabled_loadsRecoveryCodesSuccessfully() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = true)
        val userRepository = UserRepositoryMock()
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val getRecoveryCodesUseCase = GetRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Success(recoveryCodes) }
        }
        val context = createTotpSettingsComponentTestContext(
            userRepository = userRepository,
            getRecoveryCodesUseCase = getRecoveryCodesUseCase
        )
        try {
            userRepository.emit(user)
            advanceTimeBy(100.milliseconds)
            val state = assertIs<TotpSettingsScreenState.Enabled>(context.component.state.value)
            assertEquals(recoveryCodes, state.recoveryCodes)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun init_whenTotpEnabled_loadRecoveryCodesFails_emitsError() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = true)
        val userRepository = UserRepositoryMock()
        val getRecoveryCodesUseCase = GetRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val context = createTotpSettingsComponentTestContext(
            userRepository = userRepository,
            getRecoveryCodesUseCase = getRecoveryCodesUseCase
        )
        try {
            userRepository.emit(user)
            advanceTimeBy(100.milliseconds)
            val state = assertIs<TotpSettingsScreenState.Error>(context.component.state.value)
            assertIs<CommonError.Unknown>(state.error)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSetupClick_success_movesToSetupInProgress() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = false)
        val userRepository = UserRepositoryMock()
        val totpSetup = TotpSetup(secretKey = SECRET_KEY, otpAuthUrl = OTP_AUTH_URL, mfaToken = MFA_TOKEN)
        val setupTotpUseCase = SetupTotpUseCaseMock().apply {
            resultProvider = { AppResult.Success(totpSetup) }
        }
        val context = createTotpSettingsComponentTestContext(
            userRepository = userRepository,
            setupTotpUseCase = setupTotpUseCase
        )
        try {
            userRepository.emit(user)
            runCurrent()
            context.component.onSetupClick()
            advanceTimeBy(100.milliseconds)

            val state = assertIs<TotpSettingsScreenState.SetupInProgress>(context.component.state.value)
            assertEquals(totpSetup, state.setup)
            assertEquals(ONE_CALL, setupTotpUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSetupClick_error_keepsDisabledWithActionError() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = false)
        val userRepository = UserRepositoryMock()
        val setupTotpUseCase = SetupTotpUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val context = createTotpSettingsComponentTestContext(
            userRepository = userRepository,
            setupTotpUseCase = setupTotpUseCase
        )
        try {
            userRepository.emit(user)
            runCurrent()
            context.component.onSetupClick()
            advanceTimeBy(100.milliseconds)

            val state = assertIs<TotpSettingsScreenState.Disabled>(context.component.state.value)
            assertFalse(state.actionLoading)
            assertIs<CommonError.Unknown>(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onCodeChanged_updatesCodeAndClearsActionError() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = false)
        val userRepository = UserRepositoryMock()
        val totpSetup = TotpSetup(secretKey = SECRET_KEY, otpAuthUrl = OTP_AUTH_URL, mfaToken = MFA_TOKEN)
        val setupTotpUseCase = SetupTotpUseCaseMock().apply {
            resultProvider = { AppResult.Success(totpSetup) }
        }
        val context = createTotpSettingsComponentTestContext(
            userRepository = userRepository,
            setupTotpUseCase = setupTotpUseCase
        )
        try {
            userRepository.emit(user)
            runCurrent()
            context.component.onSetupClick()
            advanceTimeBy(100.milliseconds)

            context.component.onCodeChanged(CONFIRMATION_CODE)
            val state = assertIs<TotpSettingsScreenState.SetupInProgress>(context.component.state.value)
            assertEquals(CONFIRMATION_CODE, state.code)
            assertNull(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmSetupClick_success_movesToEnabledAndRefreshesUser() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = false)
        val userRepository = UserRepositoryMock()
        val totpSetup = TotpSetup(secretKey = SECRET_KEY, otpAuthUrl = OTP_AUTH_URL, mfaToken = MFA_TOKEN)
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val setupTotpUseCase = SetupTotpUseCaseMock().apply {
            resultProvider = { AppResult.Success(totpSetup) }
        }
        val enableTotpUseCase = EnableTotpUseCaseMock().apply {
            resultProvider = { _, _ -> AppResult.Success(recoveryCodes) }
        }
        val context = createTotpSettingsComponentTestContext(
            userRepository = userRepository,
            setupTotpUseCase = setupTotpUseCase,
            enableTotpUseCase = enableTotpUseCase
        )
        try {
            userRepository.emit(user)
            runCurrent()
            context.component.onSetupClick()
            advanceTimeBy(100.milliseconds)

            context.component.onCodeChanged(CONFIRMATION_CODE)
            context.component.onConfirmSetupClick()
            advanceTimeBy(100.milliseconds)

            val state = assertIs<TotpSettingsScreenState.Enabled>(context.component.state.value)
            assertEquals(recoveryCodes, state.recoveryCodes)
            assertEquals(ONE_CALL, enableTotpUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmSetupClick_error_keepsSetupWithActionError() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = false)
        val userRepository = UserRepositoryMock()
        val totpSetup = TotpSetup(secretKey = SECRET_KEY, otpAuthUrl = OTP_AUTH_URL, mfaToken = MFA_TOKEN)
        val setupTotpUseCase = SetupTotpUseCaseMock().apply {
            resultProvider = { AppResult.Success(totpSetup) }
        }
        val enableTotpUseCase = EnableTotpUseCaseMock().apply {
            resultProvider = { _, _ -> AppResult.Error(CommonError.Unknown()) }
        }
        val context = createTotpSettingsComponentTestContext(
            userRepository = userRepository,
            setupTotpUseCase = setupTotpUseCase,
            enableTotpUseCase = enableTotpUseCase
        )
        try {
            userRepository.emit(user)
            runCurrent()
            context.component.onSetupClick()
            advanceTimeBy(100.milliseconds)

            context.component.onCodeChanged(CONFIRMATION_CODE)
            context.component.onConfirmSetupClick()
            advanceTimeBy(100.milliseconds)

            val state = assertIs<TotpSettingsScreenState.SetupInProgress>(context.component.state.value)
            assertFalse(state.actionLoading)
            assertIs<CommonError.Unknown>(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onDisableClick_showsConfirmationDialog() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = true)
        val userRepository = UserRepositoryMock()
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val getRecoveryCodesUseCase = GetRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Success(recoveryCodes) }
        }
        val context = createTotpSettingsComponentTestContext(
            userRepository = userRepository,
            getRecoveryCodesUseCase = getRecoveryCodesUseCase
        )
        try {
            userRepository.emit(user)
            advanceTimeBy(100.milliseconds)

            context.component.onDisableClick()
            runCurrent()

            val state = assertIs<TotpSettingsScreenState.Enabled>(context.component.state.value)
            assertTrue(state.showDisableConfirmation)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmDisable_success_movesToDisabledAndRefreshesUser() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = true)
        val userRepository = UserRepositoryMock()
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val getRecoveryCodesUseCase = GetRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Success(recoveryCodes) }
        }
        val disableTotpUseCase = DisableTotpUseCaseMock().apply {
            resultProvider = { AppResult.Success(Unit) }
        }
        val context = createTotpSettingsComponentTestContext(
            userRepository = userRepository,
            getRecoveryCodesUseCase = getRecoveryCodesUseCase,
            disableTotpUseCase = disableTotpUseCase
        )
        try {
            userRepository.emit(user)
            advanceTimeBy(100.milliseconds)

            context.component.onDisableClick()
            context.component.onConfirmDisable()
            advanceTimeBy(100.milliseconds)

            assertIs<TotpSettingsScreenState.Disabled>(context.component.state.value)
            assertEquals(ONE_CALL, disableTotpUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmDisable_error_keepsEnabledWithActionError() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = true)
        val userRepository = UserRepositoryMock()
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val getRecoveryCodesUseCase = GetRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Success(recoveryCodes) }
        }
        val disableTotpUseCase = DisableTotpUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val context = createTotpSettingsComponentTestContext(
            userRepository = userRepository,
            getRecoveryCodesUseCase = getRecoveryCodesUseCase,
            disableTotpUseCase = disableTotpUseCase
        )
        try {
            userRepository.emit(user)
            advanceTimeBy(100.milliseconds)

            context.component.onDisableClick()
            context.component.onConfirmDisable()
            advanceTimeBy(100.milliseconds)

            val state = assertIs<TotpSettingsScreenState.Enabled>(context.component.state.value)
            assertFalse(state.actionLoading)
            assertIs<CommonError.Unknown>(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRegenerateRecoveryCodesClick_showsConfirmationDialog() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = true)
        val userRepository = UserRepositoryMock()
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val getRecoveryCodesUseCase = GetRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Success(recoveryCodes) }
        }
        val context = createTotpSettingsComponentTestContext(
            userRepository = userRepository,
            getRecoveryCodesUseCase = getRecoveryCodesUseCase
        )
        try {
            userRepository.emit(user)
            advanceTimeBy(100.milliseconds)

            context.component.onRegenerateRecoveryCodesClick()
            runCurrent()

            val state = assertIs<TotpSettingsScreenState.Enabled>(context.component.state.value)
            assertTrue(state.showRegenerateConfirmation)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmRegenerateRecoveryCodes_success_updatesRecoveryCodes() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = true)
        val userRepository = UserRepositoryMock()
        val initialCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val freshCodes = TotpRecoveryCodes(codes = listOf(CODE_THREE, CODE_FOUR))
        val getRecoveryCodesUseCase = GetRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Success(initialCodes) }
        }
        val regenerateRecoveryCodesUseCase = RegenerateRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Success(freshCodes) }
        }
        val context = createTotpSettingsComponentTestContext(
            userRepository = userRepository,
            getRecoveryCodesUseCase = getRecoveryCodesUseCase,
            regenerateRecoveryCodesUseCase = regenerateRecoveryCodesUseCase
        )
        try {
            userRepository.emit(user)
            advanceTimeBy(100.milliseconds)

            context.component.onRegenerateRecoveryCodesClick()
            context.component.onConfirmRegenerateRecoveryCodes()
            advanceTimeBy(100.milliseconds)

            val state = assertIs<TotpSettingsScreenState.Enabled>(context.component.state.value)
            assertEquals(freshCodes, state.recoveryCodes)
            assertFalse(state.actionLoading)
            assertEquals(ONE_CALL, regenerateRecoveryCodesUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRegenerateRecoveryCodesClick_error_keepsEnabledWithActionError() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = true)
        val userRepository = UserRepositoryMock()
        val initialCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val getRecoveryCodesUseCase = GetRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Success(initialCodes) }
        }
        val regenerateRecoveryCodesUseCase = RegenerateRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val context = createTotpSettingsComponentTestContext(
            userRepository = userRepository,
            getRecoveryCodesUseCase = getRecoveryCodesUseCase,
            regenerateRecoveryCodesUseCase = regenerateRecoveryCodesUseCase
        )
        try {
            userRepository.emit(user)
            advanceTimeBy(100.milliseconds)

            context.component.onRegenerateRecoveryCodesClick()
            context.component.onConfirmRegenerateRecoveryCodes()
            advanceTimeBy(100.milliseconds)

            val state = assertIs<TotpSettingsScreenState.Enabled>(context.component.state.value)
            assertFalse(state.actionLoading)
            assertIs<CommonError.Unknown>(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_invokesOnBack() = runComponentTest {
        val context = createTotpSettingsComponentTestContext()
        try {
            context.component.onBackClick()
            assertEquals(ONE_CALL, context.onBackCalls)
        } finally {
            context.destroy()
        }
    }

    private fun createTotpSettingsComponentTestContext(
        userRepository: UserRepositoryMock = UserRepositoryMock(),
        setupTotpUseCase: SetupTotpUseCaseMock = SetupTotpUseCaseMock(),
        enableTotpUseCase: EnableTotpUseCaseMock = EnableTotpUseCaseMock(),
        disableTotpUseCase: DisableTotpUseCaseMock = DisableTotpUseCaseMock(),
        getRecoveryCodesUseCase: GetRecoveryCodesUseCaseMock = GetRecoveryCodesUseCaseMock(),
        regenerateRecoveryCodesUseCase: RegenerateRecoveryCodesUseCaseMock = RegenerateRecoveryCodesUseCaseMock()
    ): TotpSettingsComponentTestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = TotpSettingsComponentTestContext(
            lifecycle = lifecycle
        )

        context.component = TotpSettingsComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            userRepository = userRepository,
            setupTotpUseCase = setupTotpUseCase,
            enableTotpUseCase = enableTotpUseCase,
            disableTotpUseCase = disableTotpUseCase,
            getRecoveryCodesUseCase = getRecoveryCodesUseCase,
            regenerateRecoveryCodesUseCase = regenerateRecoveryCodesUseCase,
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class TotpSettingsComponentTestContext(
        val lifecycle: LifecycleRegistry
    ) {
        lateinit var component: TotpSettingsComponentImpl
        var onBackCalls: Int = 0

        fun destroy() = lifecycle.destroy()
    }

    private companion object {
        const val ONE_CALL = 1
        const val CONFIRMATION_CODE = "123456"
        const val SECRET_KEY = "SECRET"
        const val OTP_AUTH_URL = "otpauth://totp/..."
        const val MFA_TOKEN = "mfa-token"
        const val CODE_ONE = "1111-2222"
        const val CODE_TWO = "3333-4444"
        const val CODE_THREE = "5555-6666"
        const val CODE_FOUR = "7777-8888"
    }
}