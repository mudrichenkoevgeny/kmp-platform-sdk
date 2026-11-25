package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate

/**
 * Discriminated union representing the active UI state for [StorageFileDetailScreen].
 */
public sealed interface StorageFileDetailScreenState {

    /** Loading state while file details are being loaded. */
    public data object Loading : StorageFileDetailScreenState

    /**
     * Error state when loading file details failed.
     *
     * @param error AppError instance describing the failure.
     */
    public data class Error(public val error: AppError) : StorageFileDetailScreenState

    /**
     * Content state displaying the loaded storage file details.
     *
     * @property file Storage file details domain object.
     * @property isDeleting Whether file deletion operation is currently in progress.
     * @property deleteError Error instance if deleting the file failed.
     */
    public data class Content(
        public val file: StorageFilePrivate,
        public val isDeleting: Boolean = false,
        public val deleteError: AppError? = null
    ) : StorageFileDetailScreenState
}
