package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.factory

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.StorageFileListComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.StorageFileListComponentImpl
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.datasource.ManagementStorageFileListDataSource
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementDeleteFileUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementDeleteFilesBatchUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementGetFilesUseCase

/**
 * Creates a [StorageFileListComponent] for management global storage file list screen (`/storage/files/`).
 *
 * @param componentContext Decompose context for lifecycle management.
 * @param getFilesUseCase Use case fetching paged management storage files.
 * @param deleteFileUseCase Use case deleting a single file in management context.
 * @param deleteFilesBatchUseCase Use case deleting files in batch in management context.
 * @param ownerUserId Optional initial owner user ID filter for scope-constrained administrative views.
 * @param initialFilterStates Optional initial filter states override.
 * @param isSelfView Whether the global view is rendered as self view allowing upload actions.
 * @param onNavigateToUpload Optional callback to navigate to upload screen.
 * @param onNavigateToFileDetail Optional callback to navigate to file detail screen.
 * @param onNavigateBack Callback for back navigation.
 * @return Configured [StorageFileListComponent] instance.
 */
fun createManagementGlobalStorageFileListComponent(
    componentContext: ComponentContext,
    getFilesUseCase: ManagementGetFilesUseCase,
    deleteFileUseCase: ManagementDeleteFileUseCase,
    deleteFilesBatchUseCase: ManagementDeleteFilesBatchUseCase,
    ownerUserId: String? = null,
    initialFilterStates: Map<String, ListingFilterState>? = null,
    isSelfView: Boolean = false,
    onNavigateToUpload: (() -> Unit)? = null,
    onNavigateToFileDetail: ((fileId: String) -> Unit)? = null,
    onNavigateBack: () -> Unit
): StorageFileListComponent {
    val dataSource = ManagementStorageFileListDataSource(
        getFilesUseCase = getFilesUseCase,
        deleteFileUseCase = deleteFileUseCase,
        deleteFilesBatchUseCase = deleteFilesBatchUseCase
    )
    return StorageFileListComponentImpl(
        componentContext = componentContext,
        dataSource = dataSource,
        includeOwnerFilter = ownerUserId == null,
        initialUserId = ownerUserId,
        initialFilterStates = initialFilterStates,
        onNavigateToUpload = if (isSelfView) onNavigateToUpload else null,
        onNavigateToFileDetail = onNavigateToFileDetail,
        onNavigateBack = onNavigateBack
    )
}
