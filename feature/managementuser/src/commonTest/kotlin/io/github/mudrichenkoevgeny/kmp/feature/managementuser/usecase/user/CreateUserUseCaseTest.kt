package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user.ManagementUserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class CreateUserUseCaseTest {

    @Test
    fun invoke_returnsUserDetails_whenRepositorySucceeds() = runTest {
        val expectedUser = userDetailsMock()
        val repository = ManagementUserRepositoryMock().apply {
            createUserResultProvider = { AppResult.Success(expectedUser) }
        }
        val useCase = CreateUserUseCase(repository)
        val request = CreateByEmailRequest(
            email = "test@example.com",
            password = "password",
            role = "USER",
            status = "ACTIVE",
            authorityLevel = 0,
            permissionCodes = emptySet<String>()
        )

        val result = useCase(request)

        assertIs<AppResult.Success<UserDetails>>(result)
        assertEquals(expectedUser, result.data)
        assertEquals(request, repository.lastCreateRequest)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = ManagementUserRepositoryMock().apply {
            createUserResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = CreateUserUseCase(repository)
        val request = CreateByEmailRequest(
            email = "test@example.com",
            password = "password",
            role = "USER",
            status = "ACTIVE",
            authorityLevel = 0,
            permissionCodes = emptySet<String>()
        )

        val result = useCase(request)

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
