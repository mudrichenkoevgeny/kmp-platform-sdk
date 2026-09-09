package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.identifier.ManagementIdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.session.ManagementSessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user.ManagementUserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.CreateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUsersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.UpdateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class UsersManagementRootComponentImplTest {

    @Test
    fun initialStack_startsAtMain() = runComponentTest {
        val context = createTestContext()

        advanceTimeBy(100.milliseconds)
        val currentChild = context.component.stack.value.active.instance
        assertIs<UsersManagementRootComponent.Child.Main>(currentChild)
        assertEquals(UsersManagementDestination.Main, context.component.stack.value.active.configuration)

        context.component.onBackClick()
        assertEquals(1, context.onBackCalls)
    }

    private fun createTestContext(
        userRepository: ManagementUserRepositoryMock = ManagementUserRepositoryMock(),
        sessionRepository: ManagementSessionRepositoryMock = ManagementSessionRepositoryMock(),
        identifierRepository: ManagementIdentifierRepositoryMock = ManagementIdentifierRepositoryMock(),
    ): TestContext {
        val user = userDetailsMock()
        userRepository.getUsersResultProvider = { AppResult.Success(pagedResultMock(listOf(user))) }
        userRepository.getUserResultProvider = { AppResult.Success(user) }

        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val getUsersUseCase = GetUsersUseCase(userRepository)
        val getUserUseCase = GetUserUseCase(userRepository)
        val createUserUseCase = CreateUserUseCase(userRepository)
        val updateUserUseCase = UpdateUserUseCase(userRepository)
        val deleteUserUseCase = DeleteUserUseCase(userRepository)
        val managementGetSessionsUseCase = ManagementGetSessionsUseCase(sessionRepository)
        val managementGetIdentifiersUseCase = ManagementGetIdentifiersUseCase(identifierRepository)

        val context = TestContext()

        context.component = UsersManagementRootComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            getUsersUseCase = getUsersUseCase,
            getUserUseCase = getUserUseCase,
            createUserUseCase = createUserUseCase,
            updateUserUseCase = updateUserUseCase,
            deleteUserUseCase = deleteUserUseCase,
            managementGetSessionsUseCase = managementGetSessionsUseCase,
            managementGetIdentifiersUseCase = managementGetIdentifiersUseCase,
            onBack = { context.onBackCalls++ },
        )

        return context
    }

    private class TestContext {
        lateinit var component: UsersManagementRootComponentImpl
        var onBackCalls = 0
    }
}
