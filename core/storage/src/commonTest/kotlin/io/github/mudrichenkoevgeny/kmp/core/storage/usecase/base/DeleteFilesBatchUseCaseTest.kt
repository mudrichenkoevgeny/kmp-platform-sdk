package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.base.StorageRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs

@InternalApi
class DeleteFilesBatchUseCaseTest {

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val repository = StorageRepositoryMock().apply { deleteFilesBatchResult = AppResult.Success(Unit) }
        val useCase = DeleteFilesBatchUseCase(repository)

        val request = DeleteFilesBatchRequest(fileIds = listOf("file_1", "file_2"))
        val result = useCase(request)

        assertIs<AppResult.Success<Unit>>(result)
    }
}
