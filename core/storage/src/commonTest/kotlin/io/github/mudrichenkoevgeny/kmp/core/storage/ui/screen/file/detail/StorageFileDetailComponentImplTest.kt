package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.base.StorageRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.datasource.StorageFileDetailDataSource
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.dialog.StorageFileDetailDialogChild
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.DeleteFileUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.GetFileDetailUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.base.detail.StorageFileDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.file.createTestStorageFilePrivate
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
class StorageFileDetailComponentImplTest {

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
    fun init_loadsFileDetailSuccessfully() = runTest {
        val expectedFile = createTestStorageFilePrivate()
        val repository = StorageRepositoryMock().apply {
            getFileDetailResult = AppResult.Success(expectedFile)
        }
        val getFileDetailUseCase = GetFileDetailUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle = lifecycle)

        val component = StorageFileDetailComponentImpl(
            componentContext = componentContext,
            params = StorageFileDetailParams(fileId = expectedFile.id.asHexDashString()),
            dataSource = object : StorageFileDetailDataSource {
                override val isDeleteAvailable: Boolean = true
                override suspend fun getFileDetail(fileId: String) = getFileDetailUseCase(fileId)
                override suspend fun deleteFile(fileId: String) = AppResult.Success(Unit)
            },
            onNavigateBack = {}
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val state = assertIs<StorageFileDetailScreenState.Content>(component.state.value)
        assertEquals(expectedFile, state.file)
    }

    @Test
    fun onDeleteClick_activatesDeleteDialogSlot() = runTest {
        val expectedFile = createTestStorageFilePrivate()
        val repository = StorageRepositoryMock().apply {
            getFileDetailResult = AppResult.Success(expectedFile)
        }
        val getFileDetailUseCase = GetFileDetailUseCase(repository)
        val deleteFileUseCase = DeleteFileUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle = lifecycle)

        val component = StorageFileDetailComponentImpl(
            componentContext = componentContext,
            params = StorageFileDetailParams(fileId = expectedFile.id.asHexDashString()),
            dataSource = object : StorageFileDetailDataSource {
                override val isDeleteAvailable: Boolean = true
                override suspend fun getFileDetail(fileId: String) = getFileDetailUseCase(fileId)
                override suspend fun deleteFile(fileId: String) = deleteFileUseCase(fileId)
            },
            onNavigateBack = {}
        )

        testDispatcher.scheduler.advanceUntilIdle()

        component.onDeleteClick()

        val child = component.dialogSlot.value.child?.instance
        assertNotNull(child)
        assertIs<StorageFileDetailDialogChild.ConfirmDeleteFile>(child)
    }

    @Test
    fun onConfirmDelete_executesDeleteAndNavigatesBack() = runTest {
        val expectedFile = createTestStorageFilePrivate()
        val repository = StorageRepositoryMock().apply {
            getFileDetailResult = AppResult.Success(expectedFile)
            deleteFileResult = AppResult.Success(Unit)
        }
        val getFileDetailUseCase = GetFileDetailUseCase(repository)
        val deleteFileUseCase = DeleteFileUseCase(repository)

        var backCalls = 0
        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle = lifecycle)

        val component = StorageFileDetailComponentImpl(
            componentContext = componentContext,
            params = StorageFileDetailParams(fileId = expectedFile.id.asHexDashString()),
            dataSource = object : StorageFileDetailDataSource {
                override val isDeleteAvailable: Boolean = true
                override suspend fun getFileDetail(fileId: String) = getFileDetailUseCase(fileId)
                override suspend fun deleteFile(fileId: String) = deleteFileUseCase(fileId)
            },
            onNavigateBack = { backCalls++ }
        )

        testDispatcher.scheduler.advanceUntilIdle()

        component.onDeleteClick()
        component.onConfirmDelete()

        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(component.dialogSlot.value.child?.instance)
        assertEquals(1, backCalls)
    }
}
