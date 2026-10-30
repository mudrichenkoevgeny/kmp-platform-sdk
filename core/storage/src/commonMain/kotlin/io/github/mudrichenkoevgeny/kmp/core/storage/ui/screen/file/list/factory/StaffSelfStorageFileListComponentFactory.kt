package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.factory

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.StorageFileListComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.StorageFileListComponentImpl
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.datasource.ManagementStorageFileListDataSource
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementDeleteFileUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementDeleteFilesBatchUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementGetFilesUseCase

/**
 * Creates a [StorageFileListComponent] for staff self-service storage file list screen (`/self/storage/files/`).
 *
 * @param componentContext Decompose context for lifecycle management.
 * @param getFilesUseCase Use case fetching paged management storage files.
 * @param deleteFileUseCase Use case deleting a single file in management context.
 * @param deleteFilesBatchUseCase Use case deleting files in batch in management context.
 * @param initialUserIdProvider Optional provider resolving current manager user account ID.
 * @param currentUserId Optional explicit manager user ID override.
 * @param initialFilterStates Optional initial filter states override.
 * @param onNavigateToUpload Optional callback to navigate to upload screen.
 * @param onNavigateToFileDetail Optional callback to navigate to file detail screen.
 * @param onNavigateBack Callback for back navigation.
 * @return Configured [StorageFileListComponent] instance.
 */
fun createStaffSelfStorageFileListComponent(
    componentContext: ComponentContext,
    getFilesUseCase: ManagementGetFilesUseCase,
    deleteFileUseCase: ManagementDeleteFileUseCase,
    deleteFilesBatchUseCase: ManagementDeleteFilesBatchUseCase,
    initialUserIdProvider: (suspend () -> String?)? = null,
    currentUserId: String? = null,
    initialFilterStates: Map<String, ListingFilterState>? = null,
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
        includeOwnerFilter = false,
        initialUserId = currentUserId,
        initialUserIdProvider = initialUserIdProvider,
        initialFilterStates = initialFilterStates,
        onNavigateToUpload = onNavigateToUpload,
        onNavigateToFileDetail = onNavigateToFileDetail,
        onNavigateBack = onNavigateBack
    )
}
