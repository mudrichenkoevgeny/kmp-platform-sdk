package io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.repository.management.ManagementStorageRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.file.createTestStorageFilePrivate
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementGetFileDetailUseCaseTest {

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val expectedFile = createTestStorageFilePrivate()
        val repository = ManagementStorageRepositoryMock().apply { getFileDetailResult = AppResult.Success(expectedFile) }
        val useCase = ManagementGetFileDetailUseCase(repository)

        val result = useCase("file_123")

        val success = assertIs<AppResult.Success<StorageFilePrivate>>(result)
        assertEquals(expectedFile, success.data)
    }
}
