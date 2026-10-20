package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionSummary
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GetSessionsUseCaseTest {

    @Test
    fun invoke_returnsPagedResult_whenRepositorySucceeds() = runTest {
        val expectedResult = createTestPagedResult(listOf(createTestUserSessionSummary()))
        val repository = SessionRepositoryMock().apply {
            getSessionsResultProvider = { AppResult.Success(expectedResult) }
        }
        val useCase = GetSessionsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Success<PagedResult<UserSessionSummary>>>(result)
        assertEquals(expectedResult, result.data)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = SessionRepositoryMock().apply {
            getSessionsResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = GetSessionsUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
