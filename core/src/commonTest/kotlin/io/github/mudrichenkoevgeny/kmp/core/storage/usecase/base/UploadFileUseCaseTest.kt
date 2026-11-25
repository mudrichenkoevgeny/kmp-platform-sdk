package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.base.StorageRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.presigned.PresignedUrlRequest
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.file.createTestStorageFilePrivate
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class UploadFileUseCaseTest {

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val expectedFile = createTestStorageFilePrivate()
        val repository = StorageRepositoryMock().apply { uploadFileResult = AppResult.Success(expectedFile) }
        val useCase = UploadFileUseCase(storageRepository = repository)

        val request = PresignedUrlRequest(
            fileName = "test.png",
            fileSizeBytes = 1024L,
            mimeType = "image/png",
            category = "IMAGE",
            visibility = "PUBLIC",
            targetScope = "avatar"
        )
        val testBytes = byteArrayOf(1, 2, 3, 4)

        val result = useCase(
            request = request,
            openReadChannel = { ByteReadChannel(testBytes) }
        )

        val success = assertIs<AppResult.Success<StorageFilePrivate>>(result)
        assertEquals(expectedFile, success.data)
    }
}
