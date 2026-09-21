package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.unlock.UnlockRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockEmailConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockPhoneConfirmationUseCase
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull

@InternalApi
class UnlockTargetInputComponentImplTest {

    @Test
    fun onInputChanged_updatesInputAndClearsActionError() = runComponentTest {
        val repositoryMock = UnlockRepositoryMock().apply {
            sendUnlockEmailConfirmationResult = AppResult.Error(CommonError.Unknown())
        }
        val context = createTestContext(
            method = UnlockMethod.EMAIL,
            prefilledInput = VALID_EMAIL,
            unlockRepositoryMock = repositoryMock
        )
        try {
            context.component.onSendCodeClick()
            advanceUntilIdle()

            assertIs<CommonError.Unknown>(context.component.state.value.actionError)

            context.component.onInputChanged(NEW_EMAIL)
            advanceUntilIdle()

            val state = context.component.state.value
            assertEquals(NEW_EMAIL, state.input)
            assertNull(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSendCodeClick_emailMethod_success_navigatesToOtp() = runComponentTest {
        val repositoryMock = UnlockRepositoryMock()
        val context = createTestContext(
            method = UnlockMethod.EMAIL,
            prefilledInput = VALID_EMAIL,
            unlockRepositoryMock = repositoryMock
        )
        try {
            context.component.onSendCodeClick()
            advanceUntilIdle()

            assertEquals(VALID_EMAIL, repositoryMock.lastEmail)
            assertEquals(VALID_EMAIL, context.lastNavigateOtpTarget)
            assertFalse(context.component.state.value.actionLoading)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSendCodeClick_emailMethod_error_surfacesError() = runComponentTest {
        val repositoryMock = UnlockRepositoryMock().apply {
            sendUnlockEmailConfirmationResult = AppResult.Error(CommonError.Unknown())
        }
        val context = createTestContext(
            method = UnlockMethod.EMAIL,
            prefilledInput = VALID_EMAIL,
            unlockRepositoryMock = repositoryMock
        )
        try {
            context.component.onSendCodeClick()
            advanceUntilIdle()

            assertNull(context.lastNavigateOtpTarget)
            val state = context.component.state.value
            assertFalse(state.actionLoading)
            assertIs<CommonError.Unknown>(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSendCodeClick_phoneMethod_success_navigatesToOtp() = runComponentTest {
        val repositoryMock = UnlockRepositoryMock()
        val context = createTestContext(
            method = UnlockMethod.PHONE,
            prefilledInput = VALID_PHONE,
            unlockRepositoryMock = repositoryMock
        )
        try {
            context.component.onSendCodeClick()
            advanceUntilIdle()

            assertEquals(VALID_PHONE, repositoryMock.lastPhoneNumber)
            assertEquals(VALID_PHONE, context.lastNavigateOtpTarget)
            assertFalse(context.component.state.value.actionLoading)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSendCodeClick_phoneMethod_error_surfacesError() = runComponentTest {
        val repositoryMock = UnlockRepositoryMock().apply {
            sendUnlockPhoneConfirmationResult = AppResult.Error(CommonError.Unknown())
        }
        val context = createTestContext(
            method = UnlockMethod.PHONE,
            prefilledInput = VALID_PHONE,
            unlockRepositoryMock = repositoryMock
        )
        try {
            context.component.onSendCodeClick()
            advanceUntilIdle()

            assertNull(context.lastNavigateOtpTarget)
            val state = context.component.state.value
            assertFalse(state.actionLoading)
            assertIs<CommonError.Unknown>(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSendCodeClick_invalidInput_doesNotSendCode() = runComponentTest {
        val repositoryMock = UnlockRepositoryMock()
        val context = createTestContext(
            method = UnlockMethod.EMAIL,
            prefilledInput = INVALID_EMAIL,
            unlockRepositoryMock = repositoryMock
        )
        try {
            context.component.onSendCodeClick()
            advanceUntilIdle()

            assertNull(repositoryMock.lastEmail)
            assertNull(context.lastNavigateOtpTarget)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_invokesNavigation() = runComponentTest {
        val context = createTestContext(method = UnlockMethod.EMAIL)
        try {
            context.component.onBackClick()
            assertEquals(ONE_CALL, context.onBackCalls)
        } finally {
            context.destroy()
        }
    }

    private fun createTestContext(
        method: UnlockMethod,
        prefilledInput: String = "",
        unlockRepositoryMock: UnlockRepositoryMock = UnlockRepositoryMock()
    ): TestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = TestContext(lifecycle)

        context.component = UnlockTargetInputComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            method = method,
            prefilledInput = prefilledInput,
            sendUnlockEmailConfirmationUseCase = SendUnlockEmailConfirmationUseCase(unlockRepositoryMock),
            sendUnlockPhoneConfirmationUseCase = SendUnlockPhoneConfirmationUseCase(unlockRepositoryMock),
            onNavigateToOtp = { target -> context.lastNavigateOtpTarget = target },
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class TestContext(
        val lifecycle: LifecycleRegistry
    ) {
        lateinit var component: UnlockTargetInputComponentImpl
        var lastNavigateOtpTarget: String? = null
        var onBackCalls: Int = 0

        fun destroy() {
            lifecycle.destroy()
        }
    }

    private companion object {
        const val VALID_EMAIL = "user@example.com"
        const val NEW_EMAIL = "new.user@example.com"
        const val INVALID_EMAIL = "invalid-email"
        const val VALID_PHONE = "79991234567"
        const val ONE_CALL = 1
    }
}
