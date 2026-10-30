package io.github.mudrichenkoevgeny.kmp.core.storage.error.parser

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.error.client.naming.ClientStorageErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.error.naming.StorageErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.error.naming.StorageErrorCodes
import kotlin.test.Test
import kotlin.test.assertNotNull

class StorageErrorParserTest {

    private val parser = StorageErrorParser()

    private fun createError(code: String, args: Map<String, String>? = null): AppError {
        return object : AppError {
            override val id = ErrorId.generate()
            override val code = code
            override val args = args
            override val isRetryable = false
        }
    }

    @Test
    fun parse_fileSizeExceedsLimit_returnsFormattedString() {
        val parsedWithArgs = parser.parse(createError(
            StorageErrorCodes.FILE_SIZE_EXCEEDS_LIMIT,
            mapOf(StorageErrorArgs.MAX_FILE_SIZE_BYTES to "10485760")
        ))
        assertNotNull(parsedWithArgs)

        val parsedWithoutArgs = parser.parse(createError(StorageErrorCodes.FILE_SIZE_EXCEEDS_LIMIT))
        assertNotNull(parsedWithoutArgs)
    }

    @Test
    fun parse_fileSizeBelowMinimum_returnsFormattedString() {
        val parsedWithArgs = parser.parse(createError(
            StorageErrorCodes.FILE_SIZE_BELOW_MINIMUM,
            mapOf(StorageErrorArgs.MIN_FILE_SIZE_BYTES to "1024")
        ))
        assertNotNull(parsedWithArgs)

        val parsedWithoutArgs = parser.parse(createError(StorageErrorCodes.FILE_SIZE_BELOW_MINIMUM))
        assertNotNull(parsedWithoutArgs)
    }

    @Test
    fun parse_storageQuotaExceeded_returnsFormattedString() {
        val parsedWithArgs = parser.parse(createError(
            StorageErrorCodes.STORAGE_QUOTA_EXCEEDED,
            mapOf(
                StorageErrorArgs.QUOTA_BYTES to "1073741824",
                StorageErrorArgs.USED_BYTES to "996147200"
            )
        ))
        assertNotNull(parsedWithArgs)

        val parsedWithoutArgs = parser.parse(createError(StorageErrorCodes.STORAGE_QUOTA_EXCEEDED))
        assertNotNull(parsedWithoutArgs)
    }

    @Test
    fun parse_tooManyPendingUploads_returnsFormattedString() {
        val parsedWithArgs = parser.parse(createError(
            StorageErrorCodes.TOO_MANY_PENDING_UPLOADS,
            mapOf(StorageErrorArgs.MAX_PENDING_UPLOADS to "5")
        ))
        assertNotNull(parsedWithArgs)

        val parsedWithoutArgs = parser.parse(createError(StorageErrorCodes.TOO_MANY_PENDING_UPLOADS))
        assertNotNull(parsedWithoutArgs)
    }

    @Test
    fun parse_maxBatchUploadCountExceeded_returnsFormattedString() {
        val parsedWithArgs = parser.parse(createError(
            StorageErrorCodes.MAX_BATCH_UPLOAD_COUNT_EXCEEDED,
            mapOf(StorageErrorArgs.MAX_BATCH_UPLOAD_COUNT to "10")
        ))
        assertNotNull(parsedWithArgs)

        val parsedWithoutArgs = parser.parse(createError(StorageErrorCodes.MAX_BATCH_UPLOAD_COUNT_EXCEEDED))
        assertNotNull(parsedWithoutArgs)
    }

    @Test
    fun parse_clientAndServerStorageErrors_returnsStrings() {
        val codes = listOf(
            StorageErrorCodes.UNSUPPORTED_MIME_TYPE,
            StorageErrorCodes.UNSUPPORTED_STORAGE_CATEGORY,
            StorageErrorCodes.PRESIGNED_URL_EXPIRED,
            StorageErrorCodes.INVALID_UPLOAD_TOKEN,
            StorageErrorCodes.UPLOAD_ALREADY_COMPLETED,
            StorageErrorCodes.STORAGE_UPLOAD_FAILED,
            ClientStorageErrorCodes.S3_UPLOAD_FAILED,
            ClientStorageErrorCodes.DIRECT_UPLOAD_CANCELLED,
            StorageErrorCodes.STORAGE_ERROR,
            StorageErrorCodes.FILE_NOT_FOUND,
            StorageErrorCodes.FILE_NOT_READY,
            StorageErrorCodes.FILE_ALREADY_EXISTS,
            StorageErrorCodes.FILE_DELETE_FAILED
        )

        codes.forEach { code ->
            val parsed = parser.parse(createError(code))
            assertNotNull(parsed, "Expected non-null parsed message for code: $code")
        }
    }
}
