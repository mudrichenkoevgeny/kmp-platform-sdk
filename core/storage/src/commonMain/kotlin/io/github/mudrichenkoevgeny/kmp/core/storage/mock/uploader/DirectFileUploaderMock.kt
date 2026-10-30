package io.github.mudrichenkoevgeny.kmp.core.storage.mock.uploader

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.uploader.DirectFileUploader
import io.ktor.utils.io.ByteReadChannel

@InternalApi
class DirectFileUploaderMock : DirectFileUploader {
    var result: AppResult<Unit> = AppResult.Success(Unit)
    var uploadCallCount = 0
    var lastUploadUrl: String? = null
    var lastContentLength: Long? = null
    var lastOpenReadChannel: (suspend () -> ByteReadChannel)? = null

    override suspend fun uploadFile(
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
