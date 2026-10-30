package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.base.StorageRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.presigned.PresignedUrlRequest
import io.ktor.utils.io.ByteReadChannel

/**
 * Executes the complete direct S3 upload flow for the authenticated user.
 *
 * @param storageRepository Storage repository for file operations.
 */
class UploadFileUseCase(
    private val storageRepository: StorageRepository
) {
    /**
     * Executes the use case.
     *
     * @param request Presigned URL request parameters DTO (file name, size, mime type, category, visibility, target scope).
     * @param openReadChannel Suspending function opening a [ByteReadChannel] stream to read payload bytes.
     * @param onProgress Optional callback tracking upload completion percentage (0 to 100).
     * @return [AppResult.Success] containing confirmed [StorageFilePrivate], or [AppResult.Error] on failure.
     */
    suspend operator fun invoke(
        request: PresignedUrlRequest,
        openReadChannel: suspend () -> ByteReadChannel,
        onProgress: ((percent: Int) -> Unit)? = null
    ): AppResult<StorageFilePrivate> {
        return storageRepository.uploadFile(
            request = request,
            openReadChannel = openReadChannel,
            onProgress = onProgress
        )
    }
}
