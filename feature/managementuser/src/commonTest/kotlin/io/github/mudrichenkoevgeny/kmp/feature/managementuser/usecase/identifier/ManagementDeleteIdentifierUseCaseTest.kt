package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.identifier.ManagementIdentifierRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementDeleteIdentifierUseCaseTest {

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        val repository = ManagementIdentifierRepositoryMock().apply {
            deleteIdentifierResultProvider = { _, _ -> AppResult.Success(Unit) }
        }
        val useCase = ManagementDeleteIdentifierUseCase(repository)
        val userId = UserId.generate()
        val identifierId = "identifier_id"

        val result = useCase(userId, identifierId)

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(userId, repository.lastUserId)
        assertEquals(identifierId, repository.lastIdentifierId)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = ManagementIdentifierRepositoryMock().apply {
            deleteIdentifierResultProvider = { _, _ -> AppResult.Error(expectedError) }
        }
        val useCase = ManagementDeleteIdentifierUseCase(repository)
        val userId = UserId.generate()
        val identifierId = "identifier_id"

        val result = useCase(userId, identifierId)

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
