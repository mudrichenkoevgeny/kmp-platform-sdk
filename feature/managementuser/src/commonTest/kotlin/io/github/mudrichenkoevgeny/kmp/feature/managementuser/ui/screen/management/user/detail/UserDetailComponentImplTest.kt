package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user.ManagementUserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.UpdateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class UserDetailComponentImplTest {

    @Test
    fun init_loadsUserSuccessfully() = runComponentTest {
        val user = userDetailsMock()
        val context = createTestContext(user = user)

        advanceTimeBy(100.milliseconds)
        val state = assertIs<UserDetailScreenState.Content>(context.component.state.value)
        assertEquals(user, state.user)
    }

    @Test
    fun onAuthorityLevelChanged_updatesInputAndClearsSaveError() = runComponentTest {
        val context = createTestContext()
        advanceTimeBy(100.milliseconds)

        context.component.onAuthorityLevelChanged("5")
        val state = assertIs<UserDetailScreenState.Content>(context.component.state.value)
        assertEquals("5", state.authorityLevelInput)
    }

    @Test
    fun onAccountStatusChanged_updatesInputAndClearsSaveError() = runComponentTest {
        val context = createTestContext()
        advanceTimeBy(100.milliseconds)

        context.component.onAccountStatusChanged("BLOCKED")
        val state = assertIs<UserDetailScreenState.Content>(context.component.state.value)
        assertEquals("BLOCKED", state.accountStatusInput)
    }

    @Test
    fun onUpdateClick_invokesUseCaseAndReloadsUser_whenSucceeds() = runComponentTest {
        val repository = ManagementUserRepositoryMock()
        val context = createTestContext(repository = repository)
        advanceTimeBy(100.milliseconds)

        context.component.onAuthorityLevelChanged("10")
        context.component.onUpdateClick()

        advanceTimeBy(100.milliseconds)
        assertEquals(10, repository.lastUpdateRequest?.authorityLevel)
    }

    @Test
    fun onUpdateClick_emitsSaveError_whenFails() = runComponentTest {
        val repository = ManagementUserRepositoryMock()
        val error = CommonError.Unknown()
        repository.updateUserResultProvider = { _, _ -> AppResult.Error(error) }
        val context = createTestContext(repository = repository)
        advanceTimeBy(100.milliseconds)

        context.component.onUpdateClick()

        advanceTimeBy(100.milliseconds)
        val state = assertIs<UserDetailScreenState.Content>(context.component.state.value)
        assertEquals(error, state.saveError)
    }

    @Test
    fun onDeleteClick_invokesUseCaseAndOnBack_whenSucceeds() = runComponentTest {
        val repository = ManagementUserRepositoryMock()
        val context = createTestContext(repository = repository)
        advanceTimeBy(100.milliseconds)

        context.component.onDeleteClick()

        advanceTimeBy(100.milliseconds)
        assertEquals(1, context.onBackCalls)
    }

    @Test
    fun onDeleteClick_emitsDeleteError_whenFails() = runComponentTest {
        val repository = ManagementUserRepositoryMock()
        val error = CommonError.Unknown()
        repository.deleteUserResultProvider = { _ -> AppResult.Error(error) }
        val context = createTestContext(repository = repository)
        advanceTimeBy(100.milliseconds)

        context.component.onDeleteClick()

        advanceTimeBy(100.milliseconds)
        val state = assertIs<UserDetailScreenState.Content>(context.component.state.value)
        assertEquals(error, state.deleteError)
    }

    @Test
    fun onSessionsClick_invokesCallback() = runComponentTest {
        val context = createTestContext()
        context.component.onSessionsClick()
        assertEquals(1, context.onNavigateToSessionsCalls)
    }

    @Test
    fun onIdentifiersClick_invokesCallback() = runComponentTest {
        val context = createTestContext()
        context.component.onIdentifiersClick()
        assertEquals(1, context.onNavigateToIdentifiersCalls)
    }

    @Test
    fun onBackClick_invokesCallback() = runComponentTest {
        val context = createTestContext()
        context.component.onBackClick()
        assertEquals(1, context.onBackCalls)
    }

    private fun createTestContext(
        user: UserDetails = userDetailsMock(),
        repository: ManagementUserRepositoryMock = ManagementUserRepositoryMock(),
    ): TestContext {
        repository.getUserResultProvider = { AppResult.Success(user) }

        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val getUserUseCase = GetUserUseCase(repository)
        val updateUserUseCase = UpdateUserUseCase(repository)
        val deleteUserUseCase = DeleteUserUseCase(repository)
        val context = TestContext()

        context.component = UserDetailComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            userId = user.id,
            getUserUseCase = getUserUseCase,
            updateUserUseCase = updateUserUseCase,
            deleteUserUseCase = deleteUserUseCase,
            onNavigateToSessions = { context.onNavigateToSessionsCalls++ },
            onNavigateToIdentifiers = { context.onNavigateToIdentifiersCalls++ },
            onBack = { context.onBackCalls++ },
        )

        return context
    }

    private class TestContext {
        lateinit var component: UserDetailComponentImpl
        var onNavigateToSessionsCalls = 0
        var onNavigateToIdentifiersCalls = 0
        var onBackCalls = 0
    }
}
