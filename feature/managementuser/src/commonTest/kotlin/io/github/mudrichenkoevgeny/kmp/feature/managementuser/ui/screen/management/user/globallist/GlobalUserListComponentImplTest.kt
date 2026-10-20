package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user.ManagementUserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUsersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserSummary
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GlobalUserListComponentImplTest {

    @Test
    fun init_loadsUsersSuccessfully() = runComponentTest {
        val user = createTestUserSummary()
        val context = createTestContext(users = listOf(user))

        advanceUntilIdle()
        val state = assertIs<GlobalUserListScreenState.Content>(context.component.state.value)
        assertEquals(listOf(user), state.paging.items)
    }

    @Test
    fun init_emitsErrorState_whenUseCaseFails() = runComponentTest {
        val repository = ManagementUserRepositoryMock()
        val error = CommonError.Unknown()
        repository.getUsersResultProvider = { AppResult.Error(error) }
        val context = createTestContext(repository = repository)

        advanceUntilIdle()
        val state = assertIs<GlobalUserListScreenState.Error>(context.component.state.value)
        assertEquals(error, state.error)
    }

    @Test
    fun onUserClick_invokesCallback() = runComponentTest {
        val user = createTestUserSummary()
        val context = createTestContext(users = listOf(user))
        advanceUntilIdle()

        context.component.onUserClick(user.id)
        assertEquals(user.id, context.lastUserClicked)
    }

    @Test
    fun onCreateUserClick_invokesCallback() = runComponentTest {
        val context = createTestContext()
        context.component.onCreateUserClick()
        assertEquals(1, context.onCreateUserCalls)
    }

    @Test
    fun onRefresh_reloadsUsers() = runComponentTest {
        val context = createTestContext()
        advanceUntilIdle()

        context.component.onRefresh()

        advanceUntilIdle()
        assertIs<GlobalUserListScreenState.Content>(context.component.state.value)
    }

    @Test
    fun onBackClick_invokesCallback() = runComponentTest {
        val context = createTestContext()
        context.component.onBackClick()
        assertEquals(1, context.onBackCalls)
    }

    private fun createTestContext(
        users: List<UserSummary>? = null,
        repository: ManagementUserRepositoryMock = ManagementUserRepositoryMock()
    ): TestContext {
        if (users != null) {
            repository.getUsersResultProvider = { AppResult.Success(createTestPagedResult(users)) }
        }

        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val useCase = GetUsersUseCase(repository)
        val context = TestContext()

        context.component = GlobalUserListComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            getUsersUseCase = useCase,
            onNavigateToUserDetail = { userId -> context.lastUserClicked = userId },
            onNavigateToCreateUser = { context.onCreateUserCalls++ },
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class TestContext {
        lateinit var component: GlobalUserListComponentImpl
        var lastUserClicked: UserId? = null
        var onCreateUserCalls = 0
        var onBackCalls = 0
    }
}