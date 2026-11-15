package io.github.mudrichenkoevgeny.kmp.core.storage.error.model

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.error.naming.CommonErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.error.client.naming.ClientStorageErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.error.naming.StorageErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.error.naming.StorageErrorCodes

/**
 * Storage-domain [AppError] variants for client-detected failures and direct S3 upload errors.
 *
 * [code] values follow shared [StorageErrorCodes] or [ClientStorageErrorCodes].
 */
public sealed class StorageError(
    public override val id: ErrorId,
    public override val code: String,
    public override val args: Map<String, String>? = null,
    public override val isRetryable: Boolean
) : AppError {

    /**
     * Direct S3 upload failed due to network error, CORS, or S3 error response.
     *
     * @param throwable Optional underlying cancellation or network cause for logging; not shown directly to users.
     * @param message Diagnostic error detail message string.
     */
    public class S3UploadFailed(
        public val throwable: Throwable? = null,
        message: String? = null
    ) : StorageError(
        id = ErrorId.generate(),
        code = ClientStorageErrorCodes.S3_UPLOAD_FAILED,
        args = message?.let { mapOf(CommonErrorArgs.MESSAGE to it) },
        isRetryable = true
    )

    /** Direct S3 upload was cancelled by the user or client application. */
    public class DirectUploadCancelled : StorageError(
        id = ErrorId.generate(),
        code = ClientStorageErrorCodes.DIRECT_UPLOAD_CANCELLED,
        isRetryable = false
    )

    /**
     * Error indicating file size exceeds maximum permitted limit during client pre-flight check.
     *
     * @param maxFileSizeBytes Maximum permitted file size in bytes.
     * @param fileSizeBytes Actual file size provided in bytes.
     * @param category Target storage category identifier.
     * @param fileName Target file name.
     */
    public class FileSizeExceedsLimit(
        maxFileSizeBytes: Long? = null,
        fileSizeBytes: Long? = null,
        category: String? = null,
        fileName: String? = null
    ) : StorageError(
        id = ErrorId.generate(),
        code = StorageErrorCodes.FILE_SIZE_EXCEEDS_LIMIT,
        args = buildMap {
            if (maxFileSizeBytes != null) put(StorageErrorArgs.MAX_FILE_SIZE_BYTES, maxFileSizeBytes.toString())
            if (fileSizeBytes != null) put(StorageErrorArgs.FILE_SIZE_BYTES, fileSizeBytes.toString())
            if (category != null) put(StorageErrorArgs.CATEGORY, category)
            if (fileName != null) put(StorageErrorArgs.FILE_NAME, fileName)
        }.ifEmpty { null },
        isRetryable = false
    )

    /**
     * Error indicating file MIME type is unsupported during client pre-flight check.
     *
     * @param mimeType Rejected MIME type string.
     * @param category Target storage category identifier.
     * @param fileName Target file name.
     */
    public class UnsupportedMimeType(
        mimeType: String? = null,
        category: String? = null,
        fileName: String? = null
    ) : StorageError(
        id = ErrorId.generate(),
        code = StorageErrorCodes.UNSUPPORTED_MIME_TYPE,
        args = buildMap {
            if (mimeType != null) put(StorageErrorArgs.MIME_TYPE, mimeType)
            if (category != null) put(StorageErrorArgs.CATEGORY, category)
            if (fileName != null) put(StorageErrorArgs.FILE_NAME, fileName)
        }.ifEmpty { null },
        isRetryable = false
    )

    /**
     * Error indicating maximum permitted batch upload count was exceeded during client pre-flight check.
     *
     * @param maxBatchUploadCount Maximum permitted files in a single batch.
     */
    public class MaxBatchUploadCountExceeded(
        maxBatchUploadCount: Int? = null
    ) : StorageError(
        id = ErrorId.generate(),
        code = StorageErrorCodes.MAX_BATCH_UPLOAD_COUNT_EXCEEDED,
        args = maxBatchUploadCount?.let { mapOf(StorageErrorArgs.MAX_BATCH_UPLOAD_COUNT to it.toString()) },
        isRetryable = false
    )
}
