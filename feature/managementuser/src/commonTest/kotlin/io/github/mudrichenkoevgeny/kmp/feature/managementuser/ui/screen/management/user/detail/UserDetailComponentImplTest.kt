package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user.ManagementUserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user.security.ManagementUserSecurityRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.UpdateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.security.ManagementDisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userPrivateMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.detail.UserDetailParams
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class UserDetailComponentImplTest {

    @Test
    fun init_loadsUserSuccessfully() = runComponentTest {
        val user = userPrivateMock()
        val testContext = createTestContext(user = user)

        advanceTimeBy(100.milliseconds)

        val state = assertIs<UserDetailScreenState.Content>(testContext.component.state.value)
        assertEquals(user, state.user)
    }

    @Test
    fun onSessionsClick_invokesCallbackWithUserId() = runComponentTest {
        val user = userPrivateMock()
        val testContext = createTestContext(user = user)
        advanceTimeBy(100.milliseconds)

        testContext.component.onSessionsClick()

        assertEquals(1, testContext.onNavigateToUserSessionsCalls)
    }

    @Test
    fun onIdentifiersClick_invokesCallbackWithUserId() = runComponentTest {
        val user = userPrivateMock()
        val testContext = createTestContext(user = user)
        advanceTimeBy(100.milliseconds)

        testContext.component.onIdentifiersClick()

        assertEquals(1, testContext.onNavigateToUserIdentifiersCalls)
    }

    @Test
    fun onAuditEventsClick_invokesCallbackWithUserId() = runComponentTest {
        val user = userPrivateMock()
        val testContext = createTestContext(user = user)
        advanceTimeBy(100.milliseconds)

        testContext.component.onAuditEventsClick()

        assertEquals(1, testContext.onNavigateToUserAuditEventsCalls)
    }

    @Test
    fun onDeleteClick_showsDeleteConfirmationDialog() = runComponentTest {
        val user = userPrivateMock()
        val testContext = createTestContext(user = user)
        advanceTimeBy(100.milliseconds)

        testContext.component.onDeleteClick()

        val state = assertIs<UserDetailScreenState.Content>(testContext.component.state.value)
        assertTrue(state.isDeleteConfirmationVisible)
    }

    @Test
    fun onDismissDeleteDialog_hidesDeleteConfirmationDialog() = runComponentTest {
        val user = userPrivateMock()
        val testContext = createTestContext(user = user)
        advanceTimeBy(100.milliseconds)

        testContext.component.onDeleteClick()
        testContext.component.onDismissDeleteDialog()

        val state = assertIs<UserDetailScreenState.Content>(testContext.component.state.value)
        assertFalse(state.isDeleteConfirmationVisible)
    }

    @Test
    fun onBackClick_invokesCallback() = runComponentTest {
        val user = userPrivateMock()
        val testContext = createTestContext(user = user)
        testContext.component.onBackClick()

        assertEquals(1, testContext.onBackCalls)
    }

    private fun createTestContext(
        user: UserPrivate = userPrivateMock(),
        repository: ManagementUserRepositoryMock = ManagementUserRepositoryMock().apply {
            getUserResultProvider = { AppResult.Success(user) }
        }
    ): TestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val getUserUseCase = GetUserUseCase(repository)
        val updateUserUseCase = UpdateUserUseCase(repository)
        val deleteUserUseCase = DeleteUserUseCase(repository)
        val disableTotpUseCase = ManagementDisableTotpUseCase(ManagementUserSecurityRepositoryMock())
        val context = TestContext()

        context.component = UserDetailComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            params = UserDetailParams(userId = user.id.asHexDashString()),
            getUserUseCase = getUserUseCase,
            updateUserUseCase = updateUserUseCase,
            deleteUserUseCase = deleteUserUseCase,
            managementDisableTotpUseCase = disableTotpUseCase,
            onNavigateToSessions = { context.onNavigateToUserSessionsCalls++ },
            onNavigateToIdentifiers = { context.onNavigateToUserIdentifiersCalls++ },
            onNavigateToAuditEvents = { context.onNavigateToUserAuditEventsCalls++ },
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class TestContext {
        lateinit var component: UserDetailComponentImpl
        var onNavigateToUserSessionsCalls = 0
        var onNavigateToUserIdentifiersCalls = 0
        var onNavigateToUserAuditEventsCalls = 0
        var onBackCalls = 0
    }
}
