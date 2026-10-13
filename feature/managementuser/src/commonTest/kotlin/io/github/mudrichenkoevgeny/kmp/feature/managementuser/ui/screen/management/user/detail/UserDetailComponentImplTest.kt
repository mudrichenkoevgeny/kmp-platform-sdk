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
        val context = createTestContext(user = user)

        advanceTimeBy(100.milliseconds)
        val state = assertIs<UserDetailScreenState.Content>(context.component.state.value)
        assertEquals(user, state.user)
        assertFalse(state.hasChanges)
    }

    @Test
    fun onAuthorityLevelChanged_updatesInputWhenInValidRange() = runComponentTest {
        val context = createTestContext()
        advanceTimeBy(100.milliseconds)

        context.component.onAuthorityLevelChanged("50")
        var state = assertIs<UserDetailScreenState.Content>(context.component.state.value)
        assertEquals("50", state.authorityLevelInput)
        assertTrue(state.hasChanges)

        context.component.onAuthorityLevelChanged("150")
        state = assertIs<UserDetailScreenState.Content>(context.component.state.value)
        assertEquals("50", state.authorityLevelInput)
    }

    @Test
    fun onAccountStatusChanged_updatesInput() = runComponentTest {
        val context = createTestContext()
        advanceTimeBy(100.milliseconds)

        context.component.onAccountStatusChanged("BLOCKED")
        val state = assertIs<UserDetailScreenState.Content>(context.component.state.value)
        assertEquals("BLOCKED", state.accountStatusInput)
        assertTrue(state.hasChanges)
    }

    @Test
    fun onLockoutTypeChanged_updatesInput() = runComponentTest {
        val context = createTestContext()
        advanceTimeBy(100.milliseconds)

        context.component.onLockoutTypeChanged("TEMPORARY")
        val state = assertIs<UserDetailScreenState.Content>(context.component.state.value)
        assertEquals("TEMPORARY", state.lockoutTypeInput)
        assertTrue(state.hasChanges)
    }

    @Test
    fun onUpdateClick_success_reloadsUser() = runComponentTest {
        val repository = ManagementUserRepositoryMock()
        val context = createTestContext(repository = repository)
        advanceTimeBy(100.milliseconds)

        context.component.onAuthorityLevelChanged("50")
        context.component.onUpdateClick()
        advanceTimeBy(100.milliseconds)

        assertEquals(1, repository.lastUpdateUserId?.value?.toHexDashString()?.let { 1 } ?: 0)
    }

    @Test
    fun onDeleteClick_showsDeleteConfirmationDialog() = runComponentTest {
        val context = createTestContext()
        advanceTimeBy(100.milliseconds)

        context.component.onDeleteClick()
        val state = assertIs<UserDetailScreenState.Content>(context.component.state.value)
        assertTrue(state.isDeleteConfirmationVisible)
    }

    @Test
    fun onConfirmDeleteClick_success_invokesCallback() = runComponentTest {
        val repository = ManagementUserRepositoryMock()
        val context = createTestContext(repository = repository)
        advanceTimeBy(100.milliseconds)

        context.component.onDeleteClick()
        context.component.onConfirmDeleteClick()
        advanceTimeBy(100.milliseconds)

        assertEquals(1, context.onBackCalls)
    }

    @Test
    fun onDismissDeleteDialog_hidesDialog() = runComponentTest {
        val context = createTestContext()
        advanceTimeBy(100.milliseconds)

        context.component.onDeleteClick()
        context.component.onDismissDeleteDialog()

        val state = assertIs<UserDetailScreenState.Content>(context.component.state.value)
        assertFalse(state.isDeleteConfirmationVisible)
    }

    @Test
    fun onBackClick_invokesCallback() = runComponentTest {
        val context = createTestContext()
        context.component.onBackClick()
        assertEquals(1, context.onBackCalls)
    }

    private fun createTestContext(
        user: UserPrivate = userPrivateMock(),
        repository: ManagementUserRepositoryMock = ManagementUserRepositoryMock()
    ): TestContext {
        repository.getUserResultProvider = { AppResult.Success(user) }

        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val getUserUseCase = GetUserUseCase(repository)
        val updateUserUseCase = UpdateUserUseCase(repository)
        val deleteUserUseCase = DeleteUserUseCase(repository)
        val disableTotpUseCase = ManagementDisableTotpUseCase(ManagementUserSecurityRepositoryMock())
        val context = TestContext()

        context.component = UserDetailComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            params = UserDetailParams(user.id.asHexDashString()),
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
        var onUserDeletedCalls = 0
        var onNavigateToUserSessionsCalls = 0
        var onNavigateToUserIdentifiersCalls = 0
        var onNavigateToUserAuditEventsCalls = 0
        var onBackCalls = 0
    }
}