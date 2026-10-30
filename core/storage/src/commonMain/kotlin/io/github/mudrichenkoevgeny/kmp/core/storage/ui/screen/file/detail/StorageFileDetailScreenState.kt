package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate

/**
 * Discriminated union representing the active UI state for [StorageFileDetailScreen].
 */
sealed interface StorageFileDetailScreenState {

    /** Loading state while file details are being loaded. */
    data object Loading : StorageFileDetailScreenState

    /**
     * Error state when loading file details failed.
     *
     * @param error AppError instance describing the failure.
     */
    data class Error(val error: AppError) : StorageFileDetailScreenState

    /**
     * Content state displaying the loaded storage file details.
     *
     * @property file Storage file details domain object.
     * @property isDeleting Whether file deletion operation is currently in progress.
     * @property deleteError Error instance if deleting the file failed.
     */
    data class Content(
        val file: StorageFilePrivate,
        val isDeleting: Boolean = false,
        val deleteError: AppError? = null
    ) : StorageFileDetailScreenState
}
