package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user.ManagementUserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GetUsersUseCaseTest {

    @Test
    fun invoke_returnsPagedResult_whenRepositorySucceeds() = runTest {
        val expectedResult = pagedResultMock(listOf(userDetailsMock()))
        val repository = ManagementUserRepositoryMock().apply {
            getUsersResultProvider = { AppResult.Success(expectedResult) }
        }
        val useCase = GetUsersUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Success<PagedResult<UserDetails>>>(result)
        assertEquals(expectedResult, result.data)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = ManagementUserRepositoryMock().apply {
            getUsersResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = GetUsersUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
