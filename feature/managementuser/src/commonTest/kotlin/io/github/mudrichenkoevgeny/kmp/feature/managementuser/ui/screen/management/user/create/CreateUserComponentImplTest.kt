package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user.ManagementUserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.CreateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class CreateUserComponentImplTest {

    @Test
    fun onEmailChanged_updatesStateAndClearsError() = runComponentTest {
        val context = createTestContext()
        context.component.onEmailChanged("new@test.com")
        assertEquals("new@test.com", context.component.state.value.email)
    }

    @Test
    fun onPasswordChanged_updatesStateAndClearsError() = runComponentTest {
        val context = createTestContext()
        context.component.onPasswordChanged("secret123")
        assertEquals("secret123", context.component.state.value.password)
    }

    @Test
    fun onRoleChanged_updatesStateAndClearsError() = runComponentTest {
        val context = createTestContext()
        context.component.onRoleChanged("ADMIN")
        assertEquals("ADMIN", context.component.state.value.role)
    }

    @Test
    fun onStatusChanged_updatesStateAndClearsError() = runComponentTest {
        val context = createTestContext()
        context.component.onStatusChanged("BLOCKED")
        assertEquals("BLOCKED", context.component.state.value.status)
    }

    @Test
    fun onAuthorityLevelChanged_updatesStateAndClearsError() = runComponentTest {
        val context = createTestContext()
        context.component.onAuthorityLevelChanged("10")
        assertEquals("10", context.component.state.value.authorityLevel)
    }

    @Test
    fun onCreateClick_invokesUseCaseAndOnSuccess_whenUseCaseSucceeds() = runComponentTest {
        val repository = ManagementUserRepositoryMock()
        repository.createUserResultProvider = { AppResult.Success(userDetailsMock()) }
        val context = createTestContext(repository = repository)

        context.component.onEmailChanged("user@example.com")
        context.component.onPasswordChanged("pass12345")
        context.component.onCreateClick()

        advanceTimeBy(100.milliseconds)
        assertEquals(1, context.onSuccessCalls)
        assertEquals("user@example.com", repository.lastCreateRequest?.email)
    }

    @Test
    fun onCreateClick_emitsErrorState_whenUseCaseFails() = runComponentTest {
        val repository = ManagementUserRepositoryMock()
        val expectedError = CommonError.Unknown()
        repository.createUserResultProvider = { AppResult.Error(expectedError) }
        val context = createTestContext(repository = repository)

        context.component.onCreateClick()

        advanceTimeBy(100.milliseconds)
        assertEquals(0, context.onSuccessCalls)
        assertEquals(expectedError, context.component.state.value.error)
    }

    @Test
    fun onBackClick_invokesOnBackCallback() = runComponentTest {
        val context = createTestContext()
        context.component.onBackClick()
        assertEquals(1, context.onBackCalls)
    }

    private fun createTestContext(
        repository: ManagementUserRepositoryMock = ManagementUserRepositoryMock(),
    ): TestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val useCase = CreateUserUseCase(repository)
        val context = TestContext()

        context.component = CreateUserComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            createUserUseCase = useCase,
            onSuccess = { context.onSuccessCalls++ },
            onBack = { context.onBackCalls++ },
        )

        return context
    }

    private class TestContext {
        lateinit var component: CreateUserComponentImpl
        var onSuccessCalls: Int = 0
        var onBackCalls: Int = 0
    }
}
