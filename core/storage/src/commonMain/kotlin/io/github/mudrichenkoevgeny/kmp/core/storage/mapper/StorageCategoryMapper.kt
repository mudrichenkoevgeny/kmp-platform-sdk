package io.github.mudrichenkoevgeny.kmp.core.storage.mapper

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory

/**
 * Utility for inferring storage categories from MIME types.
 */
@InternalApi
object StorageCategoryMapper {

    /**
     * Infers the [StorageCategory] from a given MIME type string.
     *
     * @param mimeType MIME type string (e.g. "image/png").
     * @return Inferred [StorageCategory].
     */
    fun inferFromMimeType(mimeType: String): StorageCategory {
        return when {
            mimeType.startsWith("image/") -> StorageCategory.IMAGE
            mimeType.startsWith("video/") -> StorageCategory.VIDEO
            mimeType.startsWith("audio/") -> StorageCategory.AUDIO
            mimeType.contains("pdf") || mimeType.contains("document") -> StorageCategory.DOCUMENT
            else -> StorageCategory.OTHER
        }
    }
}
