package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.management.ManagementStorageRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs

@InternalApi
class ManagementDeleteFilesBatchUseCaseTest {

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val repository = ManagementStorageRepositoryMock().apply { deleteFilesBatchResult = AppResult.Success(Unit) }
        val useCase = ManagementDeleteFilesBatchUseCase(repository)

        val request = DeleteFilesBatchRequest(fileIds = listOf("file_1", "file_2"))
        val result = useCase(request)

        assertIs<AppResult.Success<Unit>>(result)
    }
}
