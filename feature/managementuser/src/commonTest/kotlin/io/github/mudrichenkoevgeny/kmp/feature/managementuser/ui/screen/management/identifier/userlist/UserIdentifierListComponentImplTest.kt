package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.identifier.ManagementIdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.userlist.ManagementUserIdentifierListParams
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class UserIdentifierListComponentImplTest {

    @Test
    fun init_loadsIdentifiersSuccessfully() = runComponentTest {
        val userId = UserId.generate()
        val testContext = createTestContext(userId = userId)

        advanceTimeBy(100.milliseconds)

        val state = assertIs<UserIdentifierListScreenState.Content>(testContext.component.state.value)
        assertEquals(1, state.paging.items.size)

        testContext.lifecycle.destroy()
    }

    @Test
    fun init_emitsErrorState_whenUseCaseFails() = runComponentTest {
        val error = CommonError.Unknown()
        val repository = ManagementIdentifierRepositoryMock().apply {
            getUserIdentifiersResultProvider = { AppResult.Error(error) }
        }
        val userId = UserId.generate()
        val testContext = createTestContext(userId = userId, repository = repository)

        advanceTimeBy(100.milliseconds)

        val state = assertIs<UserIdentifierListScreenState.Error>(testContext.component.state.value)
        assertEquals(error, state.error)

        testContext.lifecycle.destroy()
    }

    @Test
    fun onBackClick_invokesOnBack() = runComponentTest {
        val userId = UserId.generate()
        val testContext = createTestContext(userId = userId)

        testContext.component.onBackClick()

        assertEquals(1, testContext.onBackCalls)

        testContext.lifecycle.destroy()
    }

    private fun createTestContext(
        userId: UserId,
        repository: ManagementIdentifierRepositoryMock = ManagementIdentifierRepositoryMock()
    ): TestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val useCase = ManagementGetUserIdentifiersUseCase(repository)
        val context = TestContext(lifecycle)

        context.component = UserIdentifierListComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            params = ManagementUserIdentifierListParams(userId = userId.asHexDashString()),
            managementGetUserIdentifiersUseCase = useCase,
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class TestContext(val lifecycle: LifecycleRegistry) {
        lateinit var component: UserIdentifierListComponentImpl
        var onBackCalls = 0
    }
}
