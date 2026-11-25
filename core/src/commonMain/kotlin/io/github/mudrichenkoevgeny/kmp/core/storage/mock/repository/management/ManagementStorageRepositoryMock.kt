package io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.management

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.management.ManagementStorageRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.management.list.ManagementStorageFileListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.file.createTestStorageFilePrivate

/**
 * Mock implementation of [ManagementStorageRepository] for testing.
 */
@InternalApi
public class ManagementStorageRepositoryMock : ManagementStorageRepository {

    public var getFilesResult: AppResult<PagedResult<StorageFileSummary>> =
        AppResult.Success(
            PagedResult(
                items = emptyList(),
                totalCount = 0,
                pageNumber = 1,
                pageSize = 20,
                totalPages = 0
            )
        )

    public var getFileDetailResult: AppResult<StorageFilePrivate> =
        AppResult.Success(createTestStorageFilePrivate())

    public var deleteFileResult: AppResult<Unit> =
        AppResult.Success(Unit)

    public var deleteFilesBatchResult: AppResult<Unit> =
        AppResult.Success(Unit)

    public override suspend fun getFiles(
        params: ManagementStorageFileListParams?
    ): AppResult<PagedResult<StorageFileSummary>> = getFilesResult

    public override suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate> =
        getFileDetailResult

    public override suspend fun deleteFile(fileId: String): AppResult<Unit> =
        deleteFileResult

    public override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> =
        deleteFilesBatchResult
}
