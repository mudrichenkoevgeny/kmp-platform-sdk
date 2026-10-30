package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.management.ManagementStorageSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.dialog.StorageSettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.GetManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ResetRemoteManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.UpdateManagementStorageSettingsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class, InternalApi::class)
class EditStorageSettingsComponentImplTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_loadsSettingsSuccessfully() = runTest {
        val repository = ManagementStorageSettingsRepositoryMock()
        val getUseCase = GetManagementStorageSettingsUseCase(repository)
        val updateUseCase = UpdateManagementStorageSettingsUseCase(repository)
        val resetUseCase = ResetRemoteManagementStorageSettingsUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle = lifecycle)

        val component = EditStorageSettingsComponentImpl(
            componentContext = componentContext,
            getSettingsUseCase = getUseCase,
            updateSettingsUseCase = updateUseCase,
            resetSettingsUseCase = resetUseCase,
            onBack = {}
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val state = assertIs<EditStorageSettingsScreenState.Content>(component.state.value)
        assertEquals("1024", state.minUploadSizeBytes)
        assertEquals("104857600", state.maxUploadSizeBytes)
    }

    @Test
    fun onSaveClick_updatesSettingsAndNavigatesBack() = runTest {
        val repository = ManagementStorageSettingsRepositoryMock()
        val getUseCase = GetManagementStorageSettingsUseCase(repository)
        val updateUseCase = UpdateManagementStorageSettingsUseCase(repository)
        val resetUseCase = ResetRemoteManagementStorageSettingsUseCase(repository)

        var backCalls = 0
        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle = lifecycle)

        val component = EditStorageSettingsComponentImpl(
            componentContext = componentContext,
            getSettingsUseCase = getUseCase,
            updateSettingsUseCase = updateUseCase,
            resetSettingsUseCase = resetUseCase,
            onBack = { backCalls++ }
        )

        testDispatcher.scheduler.advanceUntilIdle()

        component.onMinUploadSizeBytesChanged("2048")
        component.onSaveClick()

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, backCalls)
    }

    @Test
    fun onResetClick_activatesResetDialogSlot() = runTest {
        val repository = ManagementStorageSettingsRepositoryMock()
        val getUseCase = GetManagementStorageSettingsUseCase(repository)
        val updateUseCase = UpdateManagementStorageSettingsUseCase(repository)
        val resetUseCase = ResetRemoteManagementStorageSettingsUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle = lifecycle)

        val component = EditStorageSettingsComponentImpl(
            componentContext = componentContext,
            getSettingsUseCase = getUseCase,
            updateSettingsUseCase = updateUseCase,
            resetSettingsUseCase = resetUseCase,
            onBack = {}
        )

        testDispatcher.scheduler.advanceUntilIdle()

        component.onResetClick()

        val child = component.dialogSlot.value.child?.instance
        assertNotNull(child)
        assertIs<StorageSettingsDialogChild.ConfirmReset>(child)
    }

    @Test
    fun onConfirmReset_resetsSettingsAndNavigatesBack() = runTest {
        val repository = ManagementStorageSettingsRepositoryMock()
        val getUseCase = GetManagementStorageSettingsUseCase(repository)
        val updateUseCase = UpdateManagementStorageSettingsUseCase(repository)
        val resetUseCase = ResetRemoteManagementStorageSettingsUseCase(repository)

        var backCalls = 0
        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle = lifecycle)

        val component = EditStorageSettingsComponentImpl(
            componentContext = componentContext,
            getSettingsUseCase = getUseCase,
            updateSettingsUseCase = updateUseCase,
            resetSettingsUseCase = resetUseCase,
            onBack = { backCalls++ }
        )

        testDispatcher.scheduler.advanceUntilIdle()

        component.onResetClick()
        component.onConfirmReset()

        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(component.dialogSlot.value.child?.instance)
        assertEquals(1, backCalls)
    }
}
