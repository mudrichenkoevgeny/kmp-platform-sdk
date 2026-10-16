package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierPrivateMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.DeleteUserIdentifierUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.GetUserIdentifierUseCaseMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.detail.IdentifierDetailParams
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class IdentifierDetailComponentImplTest {

    @Test
    fun state_initWithIdentifier_emitsContent() = runComponentTest {
        val identifier = userIdentifierPrivateMock()
        val testContext = createTestContext(identifier = identifier)
        try {
            advanceTimeBy(100.milliseconds)
            val state = testContext.component.state.value
            assertIs<IdentifierDetailScreenState.Content>(state)
            assertEquals(identifier, state.identifier)
        } finally {
            testContext.destroy()
        }
    }

    @Test
    fun state_initWithIdAndSuccessUseCase_emitsContent() = runComponentTest {
        val identifier = userIdentifierPrivateMock()
        val getUserIdentifierUseCase = GetUserIdentifierUseCaseMock().apply {
            resultProvider = { AppResult.Success(identifier) }
        }
        val testContext = createTestContext(
            identifier = null,
            identifierId = identifier.id,
            getUserIdentifierUseCase = getUserIdentifierUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            val state = testContext.component.state.value
            assertIs<IdentifierDetailScreenState.Content>(state)
            assertEquals(identifier, state.identifier)
        } finally {
            testContext.destroy()
        }
    }

    @Test
    fun state_initWithIdAndFailureUseCase_emitsError() = runComponentTest {
        val identifierId = userIdentifierPrivateMock().id
        val getUserIdentifierUseCase = GetUserIdentifierUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val testContext = createTestContext(
            identifier = null,
            identifierId = identifierId,
            getUserIdentifierUseCase = getUserIdentifierUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            val state = testContext.component.state.value
            assertIs<IdentifierDetailScreenState.Error>(state)
        } finally {
            testContext.destroy()
        }
    }

    @Test
    fun onDeleteClick_showsConfirmationDialog() = runComponentTest {
        val identifier = userIdentifierPrivateMock()
        val testContext = createTestContext(identifier = identifier)
        try {
            advanceTimeBy(100.milliseconds)
            testContext.component.onDeleteIdentifierRequested()
            val state = testContext.component.state.value
            assertIs<IdentifierDetailScreenState.Content>(state)
            assertTrue(state.isDeleteConfirmationVisible)
        } finally {
            testContext.destroy()
        }
    }

    @Test
    fun onConfirmDelete_success_invokesOnBack() = runComponentTest {
        var onBackCalls = 0
        val identifier = userIdentifierPrivateMock()
        val deleteUserIdentifierUseCase = DeleteUserIdentifierUseCaseMock().apply {
            resultProvider = { AppResult.Success(Unit) }
        }
        val testContext = createTestContext(
            identifier = identifier,
            deleteUserIdentifierUseCase = deleteUserIdentifierUseCase,
            onBack = { onBackCalls++ }
        )
        try {
            advanceTimeBy(100.milliseconds)
            testContext.component.onDeleteIdentifierRequested()
            testContext.component.onDeleteIdentifierClick()
            advanceTimeBy(100.milliseconds)
            assertEquals(1, onBackCalls)
        } finally {
            testContext.destroy()
        }
    }

    @Test
    fun onUserClick_invokesNavigateToUserDetail() = runComponentTest {
        var navigatedUserId: UserId? = null
        val identifier = userIdentifierPrivateMock()
        val testContext = createTestContext(
            identifier = identifier,
            onNavigateToUserDetail = { navigatedUserId = it }
        )
        try {
            advanceTimeBy(100.milliseconds)
            testContext.component.onUserClick()
            assertEquals(identifier.userId, navigatedUserId)
        } finally {
            testContext.destroy()
        }
    }

    @Test
    fun onBackClick_invokesOnBack() = runComponentTest {
        var onBackCalls = 0
        val identifier = userIdentifierPrivateMock()
        val testContext = createTestContext(
            identifier = identifier,
            onBack = { onBackCalls++ }
        )
        try {
            advanceTimeBy(100.milliseconds)
            testContext.component.onBackClick()
            assertEquals(1, onBackCalls)
        } finally {
            testContext.destroy()
        }
    }
}

@InternalApi
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
    identifier: UserIdentifierPrivate? = userIdentifierPrivateMock(),
    identifierId: UserIdentifierId? = identifier?.id,
    getUserIdentifierUseCase: GetUserIdentifierUseCaseMock? = null,
    deleteUserIdentifierUseCase: DeleteUserIdentifierUseCaseMock? = null,
    isCurrentIdentifier: Boolean = false,
    onNavigateToUserDetail: ((UserId) -> Unit)? = null,
    onNavigateToProfile: () -> Unit = {},
    onBack: () -> Unit = {}
): TestContext {
    val lifecycleRegistry = LifecycleRegistry()
    lifecycleRegistry.resume()
    val componentContext = DefaultComponentContext(lifecycleRegistry)
    val targetIdentifierId = (identifierId ?: identifier?.id)?.asHexDashString() ?: ""
    val component = IdentifierDetailComponentImpl(
        componentContext = componentContext,
        params = IdentifierDetailParams(identifierId = targetIdentifierId),
        getUserIdentifierUseCase = getUserIdentifierUseCase ?: identifier?.let { loaded -> GetUserIdentifierUseCaseMock().apply { resultProvider = { AppResult.Success(loaded) } } },
        deleteUserIdentifierUseCase = deleteUserIdentifierUseCase,
        isCurrentIdentifier = isCurrentIdentifier,
        onNavigateToUserDetail = onNavigateToUserDetail,
        onNavigateToProfile = onNavigateToProfile,
        onBack = onBack
    )
    return TestContext(component, lifecycleRegistry)
}