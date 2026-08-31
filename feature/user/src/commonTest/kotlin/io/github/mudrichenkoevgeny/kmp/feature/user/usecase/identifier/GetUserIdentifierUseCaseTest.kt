package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GetUserIdentifierUseCaseTest {

    @Test
    fun invoke_returnsUserIdentifier_whenRepositorySucceeds() = runTest {
        val expectedIdentifier = userIdentifierMock()
        val repository = IdentifierRepositoryMock().apply {
            getUserIdentifierResultProvider = { AppResult.Success(expectedIdentifier) }
        }
        val useCase = GetUserIdentifierUseCase(repository)
        val identifierId = UserIdentifierId.generate()

        val result = useCase(identifierId)

        assertIs<AppResult.Success<UserIdentifier>>(result)
        assertEquals(expectedIdentifier, result.data)
        assertEquals(identifierId, repository.lastIdentifierId)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = IdentifierRepositoryMock().apply {
            getUserIdentifierResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = GetUserIdentifierUseCase(repository)

        val result = useCase(UserIdentifierId.generate())

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
