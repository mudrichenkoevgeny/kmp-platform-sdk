package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class AddUserIdentifierPhoneUseCaseTest {

    @Test
    fun invoke_returnsUserIdentifier_whenRepositorySucceeds() = runTest {
        val expectedIdentifier = userIdentifierMock()
        val repository = IdentifierRepositoryMock().apply {
            addUserIdentifierResultProvider = { AppResult.Success(expectedIdentifier) }
        }
        val useCase = AddUserIdentifierPhoneUseCase(repository)

        val result = useCase(TEST_PHONE, TEST_CODE)

        assertIs<AppResult.Success<UserIdentifier>>(result)
        assertEquals(expectedIdentifier, result.data)
        assertEquals(TEST_PHONE, repository.lastPhoneNumber)
        assertEquals(TEST_CODE, repository.lastConfirmationCode)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = IdentifierRepositoryMock().apply {
            addUserIdentifierResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = AddUserIdentifierPhoneUseCase(repository)

        val result = useCase(TEST_PHONE, TEST_CODE)

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }

    private companion object {
        private const val TEST_PHONE = "+1234567890"
        private const val TEST_CODE = "123456"
    }
}
