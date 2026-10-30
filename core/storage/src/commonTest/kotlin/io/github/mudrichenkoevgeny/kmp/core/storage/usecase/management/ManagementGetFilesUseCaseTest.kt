package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.management.ManagementStorageRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.management.list.ManagementStorageFileListParams
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.file.createTestStorageFileSummary
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementGetFilesUseCaseTest {

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val expectedSummary = createTestStorageFileSummary()
        val pagedResult = PagedResult(
            items = listOf(expectedSummary),
            totalCount = 1,
            pageNumber = 1,
            pageSize = 20,
            totalPages = 1
        )
        val repository = ManagementStorageRepositoryMock().apply { getFilesResult = AppResult.Success(pagedResult) }
        val useCase = ManagementGetFilesUseCase(repository)

        val params = ManagementStorageFileListParams(pageNumber = 1, pageSize = 20)
        val result = useCase(params)

        val success = assertIs<AppResult.Success<PagedResult<StorageFileSummary>>>(result)
        assertEquals(pagedResult, success.data)
    }
}
