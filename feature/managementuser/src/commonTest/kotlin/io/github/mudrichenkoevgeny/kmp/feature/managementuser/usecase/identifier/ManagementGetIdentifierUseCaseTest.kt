package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.identifier.ManagementIdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementGetIdentifierUseCaseTest {

    @Test
    fun invoke_returnsIdentifier_whenRepositorySucceeds() = runTest {
        val expectedIdentifier = userIdentifierMock()
        val repository = ManagementIdentifierRepositoryMock().apply {
            getIdentifierResultProvider = { AppResult.Success(expectedIdentifier) }
        }
        val useCase = ManagementGetIdentifierUseCase(repository)
        val identifierId = "identifier_id"

        val result = useCase(identifierId)

        assertIs<AppResult.Success<UserIdentifier>>(result)
        assertEquals(expectedIdentifier, result.data)
        assertEquals(identifierId, repository.lastIdentifierId)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = ManagementIdentifierRepositoryMock().apply {
            getIdentifierResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = ManagementGetIdentifierUseCase(repository)
        val identifierId = "identifier_id"

        val result = useCase(identifierId)

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
