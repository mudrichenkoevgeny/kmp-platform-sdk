package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.session.ManagementSessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementGetSessionsUseCaseTest {

    @Test
    fun invoke_returnsSessions_whenRepositorySucceeds() = runTest {
        val expectedSessions = pagedResultMock(listOf(userSessionMock()))
        val repository = ManagementSessionRepositoryMock().apply {
            getSessionsResultProvider = { AppResult.Success(expectedSessions) }
        }
        val useCase = ManagementGetSessionsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Success<PagedResult<UserSession>>>(result)
        assertEquals(expectedSessions, result.data)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = ManagementSessionRepositoryMock().apply {
            getSessionsResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = ManagementGetSessionsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
