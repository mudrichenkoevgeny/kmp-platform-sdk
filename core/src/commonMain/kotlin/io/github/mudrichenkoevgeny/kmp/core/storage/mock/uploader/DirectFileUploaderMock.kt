package io.github.mudrichenkoevgeny.kmp.core.storage.mock.uploader

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.uploader.DirectFileUploader
import io.ktor.utils.io.ByteReadChannel

/**
 * Mock implementation of [DirectFileUploader] for testing.
 */
@InternalApi
public class DirectFileUploaderMock : DirectFileUploader {
    public var result: AppResult<Unit> = AppResult.Success(Unit)
    public var uploadCallCount: Int = 0
    public var lastUploadUrl: String? = null
    public var lastContentLength: Long? = null
    public var lastOpenReadChannel: (suspend () -> ByteReadChannel)? = null

    public override suspend fun uploadFile(
        uploadUrl: String,
        contentLength: Long,
        openReadChannel: suspend () -> ByteReadChannel,
        mimeType: String,
        onProgress: ((percent: Int) -> Unit)?
    ): AppResult<Unit> {
        uploadCallCount++
        lastUploadUrl = uploadUrl
        lastContentLength = contentLength
        lastOpenReadChannel = openReadChannel
        onProgress?.invoke(100)
        return result
    }
}
