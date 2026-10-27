package io.github.mudrichenkoevgeny.kmp.core.storage.mock.uploader

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.uploader.DirectFileUploader

@InternalApi
class DirectFileUploaderMock : DirectFileUploader {
    var result: AppResult<Unit> = AppResult.Success(Unit)
    var uploadCallCount = 0
    var lastUploadUrl: String? = null
    var lastBytes: ByteArray? = null

    override suspend fun uploadFile(
        uploadUrl: String,
        bytes: ByteArray,
        mimeType: String,
        onProgress: ((percent: Int) -> Unit)?
    ): AppResult<Unit> {
        uploadCallCount++
        lastUploadUrl = uploadUrl
        lastBytes = bytes
        onProgress?.invoke(100)
        return result
    }
}
