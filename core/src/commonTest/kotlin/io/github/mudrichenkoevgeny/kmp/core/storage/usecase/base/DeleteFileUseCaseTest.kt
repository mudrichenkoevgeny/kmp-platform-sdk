package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.base.StorageRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs

@InternalApi
class DeleteFileUseCaseTest {

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val repository = StorageRepositoryMock().apply { deleteFileResult = AppResult.Success(Unit) }
        val useCase = DeleteFileUseCase(repository)

        val result = useCase("file_123")

        assertIs<AppResult.Success<Unit>>(result)
    }
}
