package io.github.mudrichenkoevgeny.kmp.core.storage.uploader

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.error.model.StorageError
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

/**
 * Ktor-backed implementation of [DirectFileUploader].
 *
 * @param httpClient Shared Ktor [HttpClient] for executing direct S3 `PUT` requests.
 */
class KtorDirectFileUploader(
    private val httpClient: HttpClient
) : DirectFileUploader {

    override suspend fun uploadFile(
        uploadUrl: String,
        bytes: ByteArray,
        mimeType: String,
        onProgress: ((percent: Int) -> Unit)?
    ): AppResult<Unit> {
        return try {
            val response = httpClient.put(uploadUrl) {
                contentType(ContentType.parse(mimeType))
                setBody(bytes)
                if (onProgress != null) {
                    onUpload { bytesSentTotal, contentLength ->
                        if (contentLength != null && contentLength > 0) {
                            val percent = ((bytesSentTotal * 100) / contentLength).toInt()
                            onProgress(percent.coerceIn(0, 100))
                        }
                    }
                }
            }

            if (response.status.isSuccess()) {
                AppResult.Success(Unit)
            } else {
                AppResult.Error(StorageError.S3UploadFailed())
            }
        } catch (_: Exception) {
            AppResult.Error(StorageError.S3UploadFailed())
        }
    }
}
