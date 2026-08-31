package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.otpConfirmationMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class SendAddEmailIdentifierConfirmationUseCaseTest {

    @Test
    fun invoke_returnsOtpConfirmation_whenRepositorySucceeds() = runTest {
        val expectedConfirmation = otpConfirmationMock()
        val repository = IdentifierRepositoryMock().apply {
            otpConfirmationResultProvider = { AppResult.Success(expectedConfirmation) }
        }
        val useCase = SendAddEmailIdentifierConfirmationUseCase(repository)

        val result = useCase(TEST_EMAIL)

        assertIs<AppResult.Success<OtpConfirmation>>(result)
        assertEquals(expectedConfirmation, result.data)
        assertEquals(TEST_EMAIL, repository.lastEmail)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = IdentifierRepositoryMock().apply {
            otpConfirmationResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = SendAddEmailIdentifierConfirmationUseCase(repository)

        val result = useCase(TEST_EMAIL)

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }

    private companion object {
        private const val TEST_EMAIL = "test@example.com"
    }
}
