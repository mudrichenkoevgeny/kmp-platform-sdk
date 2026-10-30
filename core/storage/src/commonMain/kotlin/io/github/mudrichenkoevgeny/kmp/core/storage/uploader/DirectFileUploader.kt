package io.github.mudrichenkoevgeny.kmp.core.storage.uploader

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.ktor.http.ContentType
import io.ktor.utils.io.ByteReadChannel

/**
 * Uploader interface for executing direct binary uploads to S3 or object storage via presigned URLs.
 */
interface DirectFileUploader {
    /**
     * Uploads binary content directly to storage via a presigned URL.
     *
     * @param uploadUrl Presigned upload URL.
     * @param contentLength Size of the file payload in bytes.
     * @param openReadChannel Suspending function opening a [ByteReadChannel] stream to read payload bytes.
     * @param mimeType MIME type of the file (defaults to [DEFAULT_MIME_TYPE]).
     * @param onProgress Optional callback tracking upload completion percentage (0 to 100).
     * @return [AppResult.Success] containing [Unit] on success, or [AppResult.Error] on failure.
     */
    suspend fun uploadFile(
        uploadUrl: String,
        contentLength: Long,
        openReadChannel: suspend () -> ByteReadChannel,
        mimeType: String = DEFAULT_MIME_TYPE,
        onProgress: ((percent: Int) -> Unit)? = null
    ): AppResult<Unit>

    companion object {
        /** Default fallback MIME type when no specific type is provided. */
        val DEFAULT_MIME_TYPE: String = ContentType.Application.OctetStream.toString()
    }
}
