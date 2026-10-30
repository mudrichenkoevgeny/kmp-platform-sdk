package io.github.mudrichenkoevgeny.kmp.core.storage.uploader

import io.github.mudrichenkoevgeny.kmp.core.common.network.httprequest.markAsPublic
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.error.model.StorageError
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.content.ChannelWriterContent
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.copyTo
import kotlinx.coroutines.CancellationException

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
        contentLength: Long,
        openReadChannel: suspend () -> ByteReadChannel,
        mimeType: String,
        onProgress: ((percent: Int) -> Unit)?
    ): AppResult<Unit> {
        return try {
            val response = httpClient.put(uploadUrl) {
                markAsPublic()
                contentType(ContentType.parse(mimeType))
                setBody(
                    ChannelWriterContent(
                        body = {
                            openReadChannel().copyTo(this)
                        },
                        contentType = ContentType.parse(mimeType),
                        contentLength = contentLength
                    )
                )
                if (onProgress != null) {
                    onUpload { bytesSentTotal, totalBytes ->
                        if (totalBytes != null && totalBytes > 0) {
                            val percent = ((bytesSentTotal * 100) / totalBytes).toInt()
                            onProgress(percent.coerceIn(0, 100))
                        }
                    }
                }
            }

            if (response.status.isSuccess()) {
                onProgress?.invoke(100)
                AppResult.Success(Unit)
            } else {
                AppResult.Error(StorageError.S3UploadFailed())
            }
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            AppResult.Error(StorageError.S3UploadFailed(throwable = e, message = e.message))
        }
    }
}
