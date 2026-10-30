package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.base.StorageRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.UploadFileUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItemStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.ui.component.fileupload.StorageFileUploadUiConfig
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.file.createTestStorageFilePrivate
import io.ktor.utils.io.ByteReadChannel
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

@OptIn(ExperimentalCoroutinesApi::class, InternalApi::class)
class StorageFileUploadComponentImplTest {

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
    fun onFilesSelected_addsItemsToQueue() {
        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle = lifecycle)
        val repository = StorageRepositoryMock()
        val useCase = UploadFileUseCase(storageRepository = repository)

        val component = StorageFileUploadComponentImpl(
            componentContext = componentContext,
            config = StorageFileUploadUiConfig(),
            uploadFileUseCase = useCase,
            onUploaded = {}
        )

        val payload = SelectedFilePayload(
            fileName = "test.png",
            fileSizeBytes = 100L,
            mimeType = "image/png",
            openReadChannel = { ByteReadChannel(byteArrayOf(1, 2, 3)) }
        )

        component.onFilesSelected(listOf(payload))

        assertEquals(1, component.state.value.queueItems.size)
        assertEquals("test.png", component.state.value.queueItems[0].fileName)
        assertEquals(UploadQueueItemStatus.IDLE, component.state.value.queueItems[0].status)
    }

    @Test
    fun onUploadClicked_executesUploadUseCaseAndTriggersOnUploaded() = runTest {
        val expectedFile = createTestStorageFilePrivate()
        val repository = StorageRepositoryMock().apply { uploadFileResult = AppResult.Success(expectedFile) }
        val useCase = UploadFileUseCase(storageRepository = repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle = lifecycle)

        var uploadedResultFiles: List<StorageFilePrivate>? = null
        val component = StorageFileUploadComponentImpl(
            componentContext = componentContext,
            config = StorageFileUploadUiConfig(),
            uploadFileUseCase = useCase,
            onUploaded = { files -> uploadedResultFiles = files }
        )

        val payload = SelectedFilePayload(
            fileName = "test.png",
            fileSizeBytes = 100L,
            mimeType = "image/png",
            openReadChannel = { ByteReadChannel(byteArrayOf(1, 2, 3)) }
        )

        component.onFilesSelected(listOf(payload))
        component.onUploadClicked()

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, component.state.value.queueItems.size)
        assertEquals(UploadQueueItemStatus.COMPLETED, component.state.value.queueItems[0].status)
        val uploaded = assertIs<List<StorageFilePrivate>>(uploadedResultFiles)
        assertEquals(1, uploaded.size)
        assertEquals(expectedFile, uploaded[0])
    }
}