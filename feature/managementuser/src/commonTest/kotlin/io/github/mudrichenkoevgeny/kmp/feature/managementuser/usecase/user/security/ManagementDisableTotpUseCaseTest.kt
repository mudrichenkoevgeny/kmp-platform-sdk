package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user.security.ManagementUserSecurityRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementDisableTotpUseCaseTest {

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        val repository = ManagementUserSecurityRepositoryMock().apply {
            disableTotpResultProvider = { AppResult.Success(Unit) }
        }
        val useCase = ManagementDisableTotpUseCase(repository)
        val userId = UserId.generate()

        val result = useCase(userId)

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(userId, repository.lastUserId)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = ManagementUserSecurityRepositoryMock().apply {
            disableTotpResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = ManagementDisableTotpUseCase(repository)
        val userId = UserId.generate()

        val result = useCase(userId)

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
