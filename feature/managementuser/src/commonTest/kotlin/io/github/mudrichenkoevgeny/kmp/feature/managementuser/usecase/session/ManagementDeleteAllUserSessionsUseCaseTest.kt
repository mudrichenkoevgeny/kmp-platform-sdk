package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.session.ManagementSessionRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementDeleteAllUserSessionsUseCaseTest {

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        val repository = ManagementSessionRepositoryMock().apply {
            deleteAllUserSessionsResultProvider = { AppResult.Success(Unit) }
        }
        val useCase = ManagementDeleteAllUserSessionsUseCase(repository)
        val userId = UserId.generate()

        val result = useCase(userId)

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(userId, repository.lastUserId)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = ManagementSessionRepositoryMock().apply {
            deleteAllUserSessionsResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = ManagementDeleteAllUserSessionsUseCase(repository)
        val userId = UserId.generate()

        val result = useCase(userId)

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
