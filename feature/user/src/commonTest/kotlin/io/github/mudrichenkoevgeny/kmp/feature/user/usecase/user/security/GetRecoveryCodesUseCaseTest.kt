package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.totpRecoveryCodesMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.security.UserSecurityRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class GetRecoveryCodesUseCaseTest {

    @Test
    fun invoke_returnsRecoveryCodes_whenRepositorySucceeds() = runTest {
        val expectedCodes = totpRecoveryCodesMock()
        val repository = UserSecurityRepositoryMock().apply {
            recoveryCodesResultProvider = { AppResult.Success(expectedCodes) }
        }
        val useCase = GetRecoveryCodesUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Success<TotpRecoveryCodes>>(result)
        assertEquals(expectedCodes, result.data)
    }

    @Test
    fun invoke_returnsError_whenRepositoryFails() = runTest {
        val expectedError = CommonError.Unknown()
        val repository = UserSecurityRepositoryMock().apply {
            recoveryCodesResultProvider = { AppResult.Error(expectedError) }
        }
        val useCase = GetRecoveryCodesUseCase(repository)

        val result = useCase()

        assertIs<AppResult.Error>(result)
        assertEquals(expectedError, result.error)
    }
}
