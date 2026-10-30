package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.management.ManagementStorageRepositoryMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs

@InternalApi
class ManagementDeleteFileUseCaseTest {

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val repository = ManagementStorageRepositoryMock().apply { deleteFileResult = AppResult.Success(Unit) }
        val useCase = ManagementDeleteFileUseCase(repository)

        val result = useCase("file_123")

        assertIs<AppResult.Success<Unit>>(result)
    }
}
