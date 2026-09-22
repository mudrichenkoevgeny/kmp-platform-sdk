package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp

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
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByPhoneUseCase
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@InternalApi
class UnlockOtpComponentImplTest {

    @Test
    fun onCodeChanged_updatesInputAndClearsActionError() = runComponentTest {
        val repositoryMock = UnlockRepositoryMock().apply {
            unlockByEmailResult = AppResult.Error(CommonError.Unknown())
        }
        val context = createTestContext(
            method = UnlockMethod.EMAIL,
            target = VALID_EMAIL,
            unlockRepositoryMock = repositoryMock
        )
        try {
            context.component.onCodeChanged("123456")
            context.component.onUnlockClick()
            advanceUntilIdle()

            assertIs<CommonError.Unknown>(context.component.state.value.actionError)

            context.component.onCodeChanged("654321")
            val state = context.component.state.value
            assertEquals("654321", state.codeInput)
            assertNull(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onUnlockClick_emailMethod_success_invokesCallback() = runComponentTest {
        val repositoryMock = UnlockRepositoryMock()
        val context = createTestContext(
            method = UnlockMethod.EMAIL,
            target = VALID_EMAIL,
            unlockRepositoryMock = repositoryMock
        )
        try {
            context.component.onCodeChanged("123456")
            context.component.onUnlockClick()
            advanceUntilIdle()

            assertTrue(context.unlockSuccessCalled)
            assertFalse(context.component.state.value.actionLoading)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onUnlockClick_phoneMethod_success_invokesCallback() = runComponentTest {
        val repositoryMock = UnlockRepositoryMock()
        val context = createTestContext(
            method = UnlockMethod.PHONE,
            target = VALID_PHONE,
            unlockRepositoryMock = repositoryMock
        )
        try {
            context.component.onCodeChanged("123456")
            context.component.onUnlockClick()
            advanceUntilIdle()

            assertTrue(context.unlockSuccessCalled)
            assertFalse(context.component.state.value.actionLoading)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onResendCodeClick_resendsCodeAndStartsTimer() = runComponentTest {
        val repositoryMock = UnlockRepositoryMock()
        val context = createTestContext(
            method = UnlockMethod.EMAIL,
            target = VALID_EMAIL,
            unlockRepositoryMock = repositoryMock
        )
        try {
            context.component.onResendCodeClick()
            advanceUntilIdle()

            assertEquals(VALID_EMAIL, repositoryMock.lastEmail)
            assertFalse(context.component.state.value.actionLoading)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_invokesNavigation() = runComponentTest {
        val context = createTestContext(method = UnlockMethod.EMAIL, target = VALID_EMAIL)
        try {
            context.component.onBackClick()
            assertEquals(1, context.onBackCalls)
        } finally {
            context.destroy()
        }
    }

    private fun createTestContext(
        method: UnlockMethod,
        target: String,
        initialDelaySeconds: Int = 0,
        unlockRepositoryMock: UnlockRepositoryMock = UnlockRepositoryMock()
    ): TestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = TestContext(lifecycle)

        context.component = UnlockOtpComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            method = method,
            target = target,
            initialDelaySeconds = initialDelaySeconds,
            unlockByEmailUseCase = UnlockByEmailUseCase(unlockRepositoryMock),
            unlockByPhoneUseCase = UnlockByPhoneUseCase(unlockRepositoryMock),
            sendUnlockEmailConfirmationUseCase = SendUnlockEmailConfirmationUseCase(unlockRepositoryMock),
            sendUnlockPhoneConfirmationUseCase = SendUnlockPhoneConfirmationUseCase(unlockRepositoryMock),
            onUnlockSuccess = { context.unlockSuccessCalled = true },
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class TestContext(
        val lifecycle: LifecycleRegistry
    ) {
        lateinit var component: UnlockOtpComponentImpl
        var unlockSuccessCalled = false
        var onBackCalls = 0

        fun destroy() {
            lifecycle.destroy()
        }
    }

    private companion object {
        const val VALID_EMAIL = "user@example.com"
        const val VALID_PHONE = "79991234567"
    }
}
