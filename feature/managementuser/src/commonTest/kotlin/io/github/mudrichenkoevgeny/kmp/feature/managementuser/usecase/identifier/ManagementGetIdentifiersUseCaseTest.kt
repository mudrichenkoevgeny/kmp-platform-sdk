package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.identifier.ManagementIdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementGetIdentifiersUseCaseTest {

    @Test
    fun invoke_returnsIdentifiers_whenRepositorySucceeds() = runTest {
        val expectedIdentifiers = pagedResultMock(listOf(userIdentifierMock()))
        val repository = ManagementIdentifierRepositoryMock().apply {
            getIdentifiersResultProvider = { AppResult.Success(expectedIdentifiers) }
        }
        val useCase = ManagementGetIdentifiersUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Success<PagedResult<UserIdentifier>>>(result)
        assertEquals(expectedIdentifiers, result.data)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = ManagementIdentifierRepositoryMock().apply {
            getIdentifiersResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = ManagementGetIdentifiersUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
