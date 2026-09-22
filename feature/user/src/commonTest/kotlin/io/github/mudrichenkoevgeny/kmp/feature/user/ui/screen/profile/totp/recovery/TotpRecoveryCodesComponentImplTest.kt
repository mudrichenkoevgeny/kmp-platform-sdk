package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.recovery

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.GetRecoveryCodesUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.RegenerateRecoveryCodesUseCaseMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class TotpRecoveryCodesComponentImplTest {

    @Test
    fun init_loadsRecoveryCodesSuccessfully() = runComponentTest {
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val getRecoveryCodesUseCase = GetRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Success(recoveryCodes) }
        }
        val context = createTestContext(getRecoveryCodesUseCase = getRecoveryCodesUseCase)
        try {
            advanceTimeBy(100.milliseconds)
            val state = assertIs<TotpRecoveryCodesScreenState.Content>(context.component.state.value)
            assertEquals(recoveryCodes, state.recoveryCodes)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun init_loadRecoveryCodesFails_emitsError() = runComponentTest {
        val getRecoveryCodesUseCase = GetRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val context = createTestContext(getRecoveryCodesUseCase = getRecoveryCodesUseCase)
        try {
            advanceTimeBy(100.milliseconds)
            val state = assertIs<TotpRecoveryCodesScreenState.Error>(context.component.state.value)
            assertIs<CommonError.Unknown>(state.error)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRegenerateClick_showsConfirmationDialog() = runComponentTest {
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val getRecoveryCodesUseCase = GetRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Success(recoveryCodes) }
        }
        val context = createTestContext(getRecoveryCodesUseCase = getRecoveryCodesUseCase)
        try {
            advanceTimeBy(100.milliseconds)

            context.component.onRegenerateClick()
            runCurrent()

            val state = assertIs<TotpRecoveryCodesScreenState.Content>(context.component.state.value)
            assertTrue(state.showRegenerateConfirmation)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmRegenerate_success_updatesRecoveryCodes() = runComponentTest {
        val initialCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val freshCodes = TotpRecoveryCodes(codes = listOf(CODE_THREE, CODE_FOUR))
        val getRecoveryCodesUseCase = GetRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Success(initialCodes) }
        }
        val regenerateRecoveryCodesUseCase = RegenerateRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Success(freshCodes) }
        }
        val context = createTestContext(
            getRecoveryCodesUseCase = getRecoveryCodesUseCase,
            regenerateRecoveryCodesUseCase = regenerateRecoveryCodesUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)

            context.component.onRegenerateClick()
            context.component.onConfirmRegenerate()
            advanceTimeBy(100.milliseconds)

            val state = assertIs<TotpRecoveryCodesScreenState.Content>(context.component.state.value)
            assertEquals(freshCodes, state.recoveryCodes)
            assertFalse(state.actionLoading)
            assertEquals(ONE_CALL, regenerateRecoveryCodesUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmRegenerate_error_keepsContentWithActionError() = runComponentTest {
        val initialCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val getRecoveryCodesUseCase = GetRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Success(initialCodes) }
        }
        val regenerateRecoveryCodesUseCase = RegenerateRecoveryCodesUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val context = createTestContext(
            getRecoveryCodesUseCase = getRecoveryCodesUseCase,
            regenerateRecoveryCodesUseCase = regenerateRecoveryCodesUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)

            context.component.onRegenerateClick()
            context.component.onConfirmRegenerate()
            advanceTimeBy(100.milliseconds)

            val state = assertIs<TotpRecoveryCodesScreenState.Content>(context.component.state.value)
            assertFalse(state.actionLoading)
            assertIs<CommonError.Unknown>(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_invokesOnBack() = runComponentTest {
        val context = createTestContext()
        try {
            context.component.onBackClick()
            assertEquals(ONE_CALL, context.onBackCalls)
        } finally {
            context.destroy()
        }
    }

    private fun createTestContext(
        getRecoveryCodesUseCase: GetRecoveryCodesUseCaseMock = GetRecoveryCodesUseCaseMock(),
        regenerateRecoveryCodesUseCase: RegenerateRecoveryCodesUseCaseMock = RegenerateRecoveryCodesUseCaseMock()
    ): TestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = TestContext(lifecycle)

        context.component = TotpRecoveryCodesComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            getRecoveryCodesUseCase = getRecoveryCodesUseCase,
            regenerateRecoveryCodesUseCase = regenerateRecoveryCodesUseCase,
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class TestContext(
        val lifecycle: LifecycleRegistry
    ) {
        lateinit var component: TotpRecoveryCodesComponentImpl
        var onBackCalls: Int = 0

        fun destroy() = lifecycle.destroy()
    }

    private companion object {
        const val ONE_CALL = 1
        const val CODE_ONE = "1111-2222"
        const val CODE_TWO = "3333-4444"
        const val CODE_THREE = "5555-6666"
        const val CODE_FOUR = "7777-8888"
    }
}
