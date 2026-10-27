package io.github.mudrichenkoevgeny.kmp.core.storage.error.parser

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.shared.foundation.core.common.formatting.ByteSizeFormatter
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.error.client.naming.ClientStorageErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.error.naming.StorageErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.error.naming.StorageErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.localization.StorageModuleStrings

/**
 * [AppErrorParser] for storage-related error codes using `shared-foundation` localization.
 */
open class StorageErrorParser(
    private val localeProvider: () -> String = { KmpLocalization.currentLocale }
) : AppErrorParser {

    companion object : StorageErrorParser()

    override fun parse(appError: AppError): String? {
        val args = appError.args ?: emptyMap()
        val locale = localeProvider()
        val strings = StorageModuleStrings(KmpLocalization.resolver, locale)

        return when (appError.code) {
            StorageErrorCodes.FILE_SIZE_EXCEEDS_LIMIT -> {
                val maxFileSize = args[StorageErrorArgs.MAX_FILE_SIZE_BYTES]
                if (!maxFileSize.isNullOrBlank()) {
                    val formatted = ByteSizeFormatter.format(
                        bytesRaw = maxFileSize,
                        resolver = KmpLocalization.resolver,
                        locale = locale
                    ) ?: maxFileSize
                    strings.errors.fileSizeExceedsLimit(formatted)
                } else {
                    strings.errors.fileSizeExceedsLimitWithoutArgs
                }
            }

            StorageErrorCodes.FILE_SIZE_BELOW_MINIMUM -> {
                val minFileSize = args[StorageErrorArgs.MIN_FILE_SIZE_BYTES]
                if (!minFileSize.isNullOrBlank()) {
                    val formatted = ByteSizeFormatter.format(
                        bytesRaw = minFileSize,
                        resolver = KmpLocalization.resolver,
                        locale = locale
                    ) ?: minFileSize
                    strings.errors.fileSizeBelowMinimum(formatted)
                } else {
                    strings.errors.fileSizeBelowMinimumWithoutArgs
                }
            }

            StorageErrorCodes.UNSUPPORTED_MIME_TYPE -> strings.errors.unsupportedMimeType

            StorageErrorCodes.UNSUPPORTED_STORAGE_CATEGORY -> strings.errors.unsupportedStorageCategory

            StorageErrorCodes.STORAGE_QUOTA_EXCEEDED -> {
                val quota = args[StorageErrorArgs.QUOTA_BYTES]
                val used = args[StorageErrorArgs.USED_BYTES]
                if (!quota.isNullOrBlank() && !used.isNullOrBlank()) {
                    val formattedQuota = ByteSizeFormatter.format(
                        bytesRaw = quota,
                        resolver = KmpLocalization.resolver,
                        locale = locale
                    ) ?: quota
                    val formattedUsed = ByteSizeFormatter.format(
                        bytesRaw = used,
                        resolver = KmpLocalization.resolver,
                        locale = locale
                    ) ?: used
                    strings.errors.storageQuotaExceeded(
                        formattedUsed = formattedUsed,
                        formattedQuota = formattedQuota
                    )
                } else {
                    strings.errors.storageQuotaExceededWithoutArgs
                }
            }

            StorageErrorCodes.TOO_MANY_PENDING_UPLOADS -> {
                val maxPending = args[StorageErrorArgs.MAX_PENDING_UPLOADS]
                if (!maxPending.isNullOrBlank()) {
                    strings.errors.tooManyPendingUploads(maxPending)
                } else {
                    strings.errors.tooManyPendingUploadsWithoutArgs
                }
            }

            StorageErrorCodes.MAX_BATCH_UPLOAD_COUNT_EXCEEDED -> {
                val maxBatch = args[StorageErrorArgs.MAX_BATCH_UPLOAD_COUNT]
                if (!maxBatch.isNullOrBlank()) {
                    strings.errors.maxBatchUploadCountExceeded(maxBatch)
                } else {
                    strings.errors.maxBatchUploadCountExceededWithoutArgs
                }
            }

            StorageErrorCodes.PRESIGNED_URL_EXPIRED -> strings.errors.presignedUrlExpired

            StorageErrorCodes.UPLOAD_ALREADY_COMPLETED -> strings.errors.uploadAlreadyCompleted

            StorageErrorCodes.STORAGE_UPLOAD_FAILED,
            ClientStorageErrorCodes.S3_UPLOAD_FAILED -> strings.errors.storageUploadFailed

            ClientStorageErrorCodes.DIRECT_UPLOAD_CANCELLED -> strings.errors.uploadCancelled

            StorageErrorCodes.STORAGE_ERROR -> strings.errors.storageError

            StorageErrorCodes.FILE_NOT_FOUND -> strings.errors.fileNotFound

            StorageErrorCodes.FILE_NOT_READY -> strings.errors.fileNotReady

            StorageErrorCodes.FILE_ALREADY_EXISTS -> strings.errors.fileAlreadyExists

            StorageErrorCodes.FILE_DELETE_FAILED -> strings.errors.fileDeleteFailed

            else -> null
        }
    }
}
