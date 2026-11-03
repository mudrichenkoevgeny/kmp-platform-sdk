package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.edit

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.UserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.SelfUpdateUserUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.ValidateDisplayNameUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.ValidateNicknameUseCaseMock
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserPrivate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(InternalApi::class)
class UserEditComponentImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val lifecycle = LifecycleRegistry()
    private val componentContext = DefaultComponentContext(lifecycle)
    private val updateSelfUserUseCase = SelfUpdateUserUseCaseMock()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_loadsUser_andSetsInitialContentState() = runTest {
        val user = createTestUserPrivate(nickname = "oldnick", displayName = "Old Name")
        val repository = UserRepositoryMock().apply { emit(user) }

        val component = UserEditComponentImpl(
            componentContext = componentContext,
            userRepository = repository,
            selfUpdateUserUseCase = updateSelfUserUseCase,
            validateNicknameUseCase = ValidateNicknameUseCaseMock(),
            validateDisplayNameUseCase = ValidateDisplayNameUseCaseMock(),
            onBack = {}
        )
        testDispatcher.scheduler.advanceUntilIdle()

        val state = component.state.value
        assertIs<UserEditScreenState.Content>(state)
        assertEquals("oldnick", state.nicknameInput)
        assertEquals("Old Name", state.displayNameInput)
    }

    @Test
    fun onNicknameChanged_updatesInputInContentState() = runTest {
        val user = createTestUserPrivate()
        val repository = UserRepositoryMock().apply { emit(user) }

        val component = UserEditComponentImpl(
            componentContext = componentContext,
            userRepository = repository,
            selfUpdateUserUseCase = updateSelfUserUseCase,
            validateNicknameUseCase = ValidateNicknameUseCaseMock(),
            validateDisplayNameUseCase = ValidateDisplayNameUseCaseMock(),
            onBack = {}
        )
        testDispatcher.scheduler.advanceUntilIdle()

        component.onNicknameChanged("newnick")

        val state = component.state.value as UserEditScreenState.Content
        assertEquals("newnick", state.nicknameInput)
    }

    @Test
    fun onDisplayNameChanged_updatesInputInContentState() = runTest {
        val user = createTestUserPrivate()
        val repository = UserRepositoryMock().apply { emit(user) }

        val component = UserEditComponentImpl(
            componentContext = componentContext,
            userRepository = repository,
            selfUpdateUserUseCase = updateSelfUserUseCase,
            validateNicknameUseCase = ValidateNicknameUseCaseMock(),
            validateDisplayNameUseCase = ValidateDisplayNameUseCaseMock(),
            onBack = {}
        )
        testDispatcher.scheduler.advanceUntilIdle()

        component.onDisplayNameChanged("New Name")

        val state = component.state.value as UserEditScreenState.Content
        assertEquals("New Name", state.displayNameInput)
    }

    @Test
    fun onSaveClick_callsUpdateUser_andTriggersOnBack_onSuccess() = runTest {
        val user = createTestUserPrivate()
        var backCalled = false
        val repository = UserRepositoryMock().apply {
            emit(user)
        }
        updateSelfUserUseCase.resultProvider = { AppResult.Success(user) }

        val component = UserEditComponentImpl(
            componentContext = componentContext,
            userRepository = repository,
            selfUpdateUserUseCase = updateSelfUserUseCase,
            validateNicknameUseCase = ValidateNicknameUseCaseMock(),
            validateDisplayNameUseCase = ValidateDisplayNameUseCaseMock(),
            onBack = { backCalled = true }
        )
        testDispatcher.scheduler.advanceUntilIdle()

        component.onNicknameChanged("validnick")
        component.onDisplayNameChanged("Valid Name")
        component.onSaveClick()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(backCalled)
    }

    @Test
    fun onSaveClick_setsActionError_onRepositoryFailure() = runTest {
        val user = createTestUserPrivate()
        val expectedError = CommonError.Unknown()
        val repository = UserRepositoryMock().apply {
            emit(user)
        }
        updateSelfUserUseCase.resultProvider = { AppResult.Error(expectedError) }

        val component = UserEditComponentImpl(
            componentContext = componentContext,
            userRepository = repository,
            selfUpdateUserUseCase = updateSelfUserUseCase,
            validateNicknameUseCase = ValidateNicknameUseCaseMock(),
            validateDisplayNameUseCase = ValidateDisplayNameUseCaseMock(),
            onBack = {}
        )
        testDispatcher.scheduler.advanceUntilIdle()

        component.onNicknameChanged("validnick")
        component.onDisplayNameChanged("Valid Name")
        component.onSaveClick()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = component.state.value as UserEditScreenState.Content
        assertEquals(expectedError, state.actionError)
    }

    @Test
    fun onSaveClick_setsValidationError_whenValidationFails() = runTest {
        val user = createTestUserPrivate()
        val repository = UserRepositoryMock().apply { emit(user) }
        val expectedError = CommonError.Unknown()
        val validateNicknameUseCase = ValidateNicknameUseCaseMock().apply {
            resultProvider = { _, _ -> AppResult.Error(expectedError) }
        }

        val component = UserEditComponentImpl(
            componentContext = componentContext,
            userRepository = repository,
            selfUpdateUserUseCase = updateSelfUserUseCase,
            validateNicknameUseCase = validateNicknameUseCase,
            validateDisplayNameUseCase = ValidateDisplayNameUseCaseMock(),
            onBack = {}
        )
        testDispatcher.scheduler.advanceUntilIdle()

        component.onSaveClick()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = component.state.value as UserEditScreenState.Content
        assertEquals(expectedError, state.nicknameError)
    }
}
