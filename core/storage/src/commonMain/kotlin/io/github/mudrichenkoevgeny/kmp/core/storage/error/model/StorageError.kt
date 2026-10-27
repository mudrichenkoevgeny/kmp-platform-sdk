package io.github.mudrichenkoevgeny.kmp.core.storage.error.model

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.error.client.naming.ClientStorageErrorCodes

/**
 * Storage-domain [AppError] variants for file storage and related flows.
 *
 * Each instance carries a stable [code] aligned with [ClientStorageErrorCodes] (or shared foundation codes
 * where applicable for parsing).
 */
sealed class StorageError(
    override val id: ErrorId,
    override val code: String,
    override val args: Map<String, String>? = null,
    override val isRetryable: Boolean
) : AppError {

    /**
     * Direct S3 upload failed due to network error, CORS, or S3 error response.
     */
    class S3UploadFailed : StorageError(
        id = ErrorId.generate(),
        code = ClientStorageErrorCodes.S3_UPLOAD_FAILED,
        isRetryable = true
    )

    /**
     * Direct S3 upload was cancelled by the user or client application.
     */
    class DirectUploadCancelled : StorageError(
        id = ErrorId.generate(),
        code = ClientStorageErrorCodes.DIRECT_UPLOAD_CANCELLED,
        isRetryable = false
    )
}
