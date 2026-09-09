package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.identifiers

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.identifier.ManagementIdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class UserIdentifiersComponentImplTest {

    @Test
    fun init_loadsIdentifiersSuccessfully() = runComponentTest {
        val identifier = userIdentifierMock()
        val context = createTestContext(identifiers = listOf(identifier))

        advanceUntilIdle()
        val state = assertIs<UserIdentifiersScreenState.Content>(context.component.state.value)
        assertEquals(listOf(identifier), state.paging.items)
    }

    @Test
    fun init_emitsErrorState_whenUseCaseFails() = runComponentTest {
        val repository = ManagementIdentifierRepositoryMock()
        val error = CommonError.Unknown()
        repository.getIdentifiersResultProvider = { AppResult.Error(error) }
        val context = createTestContext(repository = repository)

        advanceUntilIdle()
        val state = assertIs<UserIdentifiersScreenState.Error>(context.component.state.value)
        assertEquals(error, state.error)
    }

    @Test
    fun onRefresh_reloadsIdentifiers() = runComponentTest {
        val context = createTestContext()
        advanceUntilIdle()

        context.component.onRefresh()

        advanceUntilIdle()
        assertIs<UserIdentifiersScreenState.Content>(context.component.state.value)
    }

    @Test
    fun onBackClick_invokesCallback() = runComponentTest {
        val context = createTestContext()
        context.component.onBackClick()
        assertEquals(1, context.onBackCalls)
    }

    private fun createTestContext(
        userId: UserId = UserId.generate(),
        identifiers: List<UserIdentifier>? = null,
        repository: ManagementIdentifierRepositoryMock = ManagementIdentifierRepositoryMock(),
    ): TestContext {
        if (identifiers != null) {
            repository.getIdentifiersResultProvider = { AppResult.Success(pagedResultMock(identifiers)) }
        }

        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val useCase = ManagementGetIdentifiersUseCase(repository)
        val context = TestContext()

        context.component = UserIdentifiersComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            userId = userId,
            managementGetIdentifiersUseCase = useCase,
            onBack = { context.onBackCalls++ },
        )

        return context
    }

    private class TestContext {
        lateinit var component: UserIdentifiersComponentImpl
        var onBackCalls = 0
    }
}
