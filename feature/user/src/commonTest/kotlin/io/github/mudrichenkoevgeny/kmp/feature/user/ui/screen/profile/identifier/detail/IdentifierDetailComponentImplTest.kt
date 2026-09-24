package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.DeleteUserIdentifierUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.GetUserIdentifierUseCaseMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class IdentifierDetailComponentImplTest {

    @Test
    fun init_withProvidedIdentifier_setsContentStateImmediately() = runComponentTest {
        val mockIdentifier = userIdentifierMock()
        val context = createTestContext(
            identifier = mockIdentifier,
            isCurrentIdentifier = true
        )
        try {
            advanceTimeBy(100.milliseconds)
            val state = assertIs<IdentifierDetailScreenState.Content>(context.component.state.value)
            assertEquals(mockIdentifier, state.identifier)
            assertTrue(state.isCurrentIdentifier)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun init_withIdentifierId_loadsIdentifierSuccessfully() = runComponentTest {
        val mockIdentifier = userIdentifierMock()
        val getUserIdentifierUseCase = GetUserIdentifierUseCaseMock().apply {
            resultProvider = { AppResult.Success(mockIdentifier) }
        }
        val context = createTestContext(
            identifier = null,
            identifierId = mockIdentifier.id,
            getUserIdentifierUseCase = getUserIdentifierUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            val state = assertIs<IdentifierDetailScreenState.Content>(context.component.state.value)
            assertEquals(mockIdentifier, state.identifier)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun init_whenLoadFails_emitsError() = runComponentTest {
        val getUserIdentifierUseCase = GetUserIdentifierUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val context = createTestContext(
            identifier = null,
            identifierId = UserIdentifierId.generate(),
            getUserIdentifierUseCase = getUserIdentifierUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            val state = assertIs<IdentifierDetailScreenState.Error>(context.component.state.value)
            assertIs<CommonError.Unknown>(state.error)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onDeleteIdentifierClick_success_callsOnBack() = runComponentTest {
        var backCalled = false
        val deleteUserIdentifierUseCase = DeleteUserIdentifierUseCaseMock().apply {
            resultProvider = { AppResult.Success(Unit) }
        }
        val context = createTestContext(
            identifier = userIdentifierMock(),
            deleteUserIdentifierUseCase = deleteUserIdentifierUseCase,
            onBack = { backCalled = true }
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onDeleteIdentifierClick()
            advanceTimeBy(100.milliseconds)
            assertTrue(backCalled)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onDeleteIdentifierClick_error_setsActionError() = runComponentTest {
        val deleteUserIdentifierUseCase = DeleteUserIdentifierUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val context = createTestContext(
            identifier = userIdentifierMock(),
            deleteUserIdentifierUseCase = deleteUserIdentifierUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onDeleteIdentifierClick()
            advanceTimeBy(100.milliseconds)
            val state = assertIs<IdentifierDetailScreenState.Content>(context.component.state.value)
            assertIs<CommonError.Unknown>(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_callsOnBack() = runComponentTest {
        var backCalled = false
        val context = createTestContext(
            identifier = userIdentifierMock(),
            onBack = { backCalled = true }
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onBackClick()
            assertTrue(backCalled)
        } finally {
            context.destroy()
        }
    }
}

private class TestContext(
    val component: IdentifierDetailComponentImpl,
    val lifecycleRegistry: LifecycleRegistry
) {
    fun destroy() {
        lifecycleRegistry.destroy()
    }
}

@InternalApi
private fun createTestContext(
    identifier: UserIdentifier? = userIdentifierMock(),
    identifierId: UserIdentifierId? = identifier?.id,
    getUserIdentifierUseCase: GetUserIdentifierUseCaseMock? = null,
    deleteUserIdentifierUseCase: DeleteUserIdentifierUseCaseMock? = null,
    isCurrentIdentifier: Boolean = false,
    onBack: () -> Unit = {}
): TestContext {
    val lifecycleRegistry = LifecycleRegistry()
    lifecycleRegistry.resume()
    val componentContext = DefaultComponentContext(lifecycleRegistry)
    val component = IdentifierDetailComponentImpl(
        componentContext = componentContext,
        identifier = identifier,
        identifierId = identifierId,
        getUserIdentifierUseCase = getUserIdentifierUseCase,
        deleteUserIdentifierUseCase = deleteUserIdentifierUseCase,
        isCurrentIdentifier = isCurrentIdentifier,
        onBack = onBack
    )
    return TestContext(component, lifecycleRegistry)
}
