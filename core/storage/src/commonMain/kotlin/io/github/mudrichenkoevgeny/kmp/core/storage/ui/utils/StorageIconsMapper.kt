package io.github.mudrichenkoevgeny.kmp.core.storage.ui.utils

import io.github.mudrichenkoevgeny.kmp.core.storage.Res
import io.github.mudrichenkoevgeny.kmp.core.storage.ic_file_audio
import io.github.mudrichenkoevgeny.kmp.core.storage.ic_file_document
import io.github.mudrichenkoevgeny.kmp.core.storage.ic_file_image
import io.github.mudrichenkoevgeny.kmp.core.storage.ic_file_other
import io.github.mudrichenkoevgeny.kmp.core.storage.ic_file_status_deleted
import io.github.mudrichenkoevgeny.kmp.core.storage.ic_file_status_pending_upload
import io.github.mudrichenkoevgeny.kmp.core.storage.ic_file_status_processing
import io.github.mudrichenkoevgeny.kmp.core.storage.ic_file_status_ready
import io.github.mudrichenkoevgeny.kmp.core.storage.ic_file_video
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.status.StorageFileStatus
import org.jetbrains.compose.resources.DrawableResource

/**
 * Mappers for resolving Compose [DrawableResource] icons for storage categories and lifecycle statuses.
 */
object StorageIconsMapper {
    /**
     * Returns the [DrawableResource] icon associated with the specified [StorageCategory].
     */
    fun getCategoryIconRes(category: StorageCategory): DrawableResource {
        return when (category) {
            StorageCategory.IMAGE -> Res.drawable.ic_file_image
            StorageCategory.VIDEO -> Res.drawable.ic_file_video
            StorageCategory.AUDIO -> Res.drawable.ic_file_audio
            StorageCategory.DOCUMENT -> Res.drawable.ic_file_document
            StorageCategory.OTHER -> Res.drawable.ic_file_other
        }
    }

    /**
     * Returns the [DrawableResource] icon associated with the specified [StorageFileStatus].
     */
    fun getStatusIconRes(status: StorageFileStatus): DrawableResource {
        return when (status) {
            StorageFileStatus.PENDING_UPLOAD -> Res.drawable.ic_file_status_pending_upload
            StorageFileStatus.PROCESSING -> Res.drawable.ic_file_status_processing
            StorageFileStatus.READY -> Res.drawable.ic_file_status_ready
            StorageFileStatus.DELETED -> Res.drawable.ic_file_status_deleted
        }
    }
}
