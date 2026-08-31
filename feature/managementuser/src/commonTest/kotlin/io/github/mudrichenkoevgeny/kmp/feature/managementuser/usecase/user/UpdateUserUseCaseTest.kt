package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user.ManagementUserRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.UpdateUserRequest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class UpdateUserUseCaseTest {

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        val repository = ManagementUserRepositoryMock().apply {
            updateUserResultProvider = { _, _ -> AppResult.Success(Unit) }
        }
        val useCase = UpdateUserUseCase(repository)
        val userId = UserId.generate()
        val request = UpdateUserRequest()

        val result = useCase(userId, request)

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(userId, repository.lastUpdateUserId)
        assertEquals(request, repository.lastUpdateRequest)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = ManagementUserRepositoryMock().apply {
            updateUserResultProvider = { _, _ -> AppResult.Error(expectedError) }
        }
        val useCase = UpdateUserUseCase(repository)
        val userId = UserId.generate()
        val request = UpdateUserRequest()

        val result = useCase(userId, request)

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
