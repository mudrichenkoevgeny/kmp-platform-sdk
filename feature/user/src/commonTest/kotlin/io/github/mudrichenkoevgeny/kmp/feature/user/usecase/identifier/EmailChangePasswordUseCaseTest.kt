package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import kotlinx.coroutines.test.runTest
import kotlin.uuid.Uuid
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class EmailChangePasswordUseCaseTest {

    @Test
    fun invoke_returnsSuccess_whenRepositorySucceeds() = runTest {
        val repository = IdentifierRepositoryMock().apply {
            emailChangePasswordResultProvider = { AppResult.Success(Unit) }
        }
        val useCase = EmailChangePasswordUseCase(repository)

        val id = UserIdentifierId(Uuid.random())
        val result = useCase(id, TEST_OLD_PASSWORD, TEST_NEW_PASSWORD)

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(id, repository.lastIdentifierId)
        assertEquals(TEST_OLD_PASSWORD, repository.lastOldPassword)
        assertEquals(TEST_NEW_PASSWORD, repository.lastNewPassword)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = IdentifierRepositoryMock().apply {
            emailChangePasswordResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = EmailChangePasswordUseCase(repository)

        val id = UserIdentifierId(Uuid.random())
        val result = useCase(id, TEST_OLD_PASSWORD, TEST_NEW_PASSWORD)

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }

    private companion object {
        private const val TEST_EMAIL = "test@example.com"
        private const val TEST_OLD_PASSWORD = "old"
        private const val TEST_NEW_PASSWORD = "new"
    }
}
