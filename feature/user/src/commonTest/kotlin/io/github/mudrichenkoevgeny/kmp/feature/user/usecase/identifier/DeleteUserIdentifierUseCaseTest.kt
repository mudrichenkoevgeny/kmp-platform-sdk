package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class DeleteUserIdentifierUseCaseTest {

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        val repository = IdentifierRepositoryMock().apply {
            deleteUserIdentifierResultProvider = { AppResult.Success(Unit) }
        }
        val useCase = DeleteUserIdentifierUseCase(repository)
        val identifierId = UserIdentifierId.generate()

        val result = useCase(identifierId)

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(identifierId, repository.lastIdentifierId)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = IdentifierRepositoryMock().apply {
            deleteUserIdentifierResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = DeleteUserIdentifierUseCase(repository)

        val result = useCase(UserIdentifierId.generate())

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
