package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.factory

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.StorageFileListComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.StorageFileListComponentImpl
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.datasource.SelfStorageFileListDataSource
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.DeleteFileUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.DeleteFilesBatchUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.GetFilesUseCase

/**
 * Factory function creating a [StorageFileListComponent] for client user self-service storage file list screen (`/storage/files/`).
 *
 * @param componentContext Decompose context for lifecycle management.
 * @param getFilesUseCase Use case fetching paged self-service files.
 * @param deleteFileUseCase Use case deleting a single file.
 * @param deleteFilesBatchUseCase Use case deleting files in batch.
 * @param initialFilterStates Optional initial filter states override.
 * @param onNavigateToUpload Optional callback to navigate to upload screen.
 * @param onNavigateToFileDetail Optional callback to navigate to file detail screen.
 * @param onNavigateBack Callback for back navigation.
 * @return Configured [StorageFileListComponent] instance.
 */
fun createClientStorageFileListComponent(
    componentContext: ComponentContext,
    getFilesUseCase: GetFilesUseCase,
    deleteFileUseCase: DeleteFileUseCase,
    deleteFilesBatchUseCase: DeleteFilesBatchUseCase,
    initialFilterStates: Map<String, ListingFilterState>? = null,
    onNavigateToUpload: (() -> Unit)? = null,
    onNavigateToFileDetail: ((fileId: String) -> Unit)? = null,
    onNavigateBack: () -> Unit
): StorageFileListComponent {
    val dataSource = SelfStorageFileListDataSource(
        getFilesUseCase = getFilesUseCase,
        deleteFileUseCase = deleteFileUseCase,
        deleteFilesBatchUseCase = deleteFilesBatchUseCase
    )
    return StorageFileListComponentImpl(
        componentContext = componentContext,
        dataSource = dataSource,
        includeOwnerFilter = false,
        initialUserId = null,
        initialFilterStates = initialFilterStates,
        onNavigateToUpload = onNavigateToUpload,
        onNavigateToFileDetail = onNavigateToFileDetail,
        onNavigateBack = onNavigateBack
    )
}
