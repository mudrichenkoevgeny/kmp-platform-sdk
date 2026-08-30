package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.auth.login.SelfManagementLoginApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.data.authDataPayloadMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class SelfManagementLoginRepositoryImplTest {

    private val api = SelfManagementLoginApiMock()
    private val repository = SelfManagementLoginRepositoryImpl(api)
    private val mockPayload = authDataPayloadMock()

    @Test
    fun `loginByEmail success maps data`() = runTest {
        api.loginByEmailResult = AppResult.Success(mockPayload)

        val result = repository.loginByEmail("test@test.com", "pass")

        val success = assertIs<AppResult.Success<AuthData>>(result)
        assertEquals(mockPayload.toAuthData(), success.data)
        assertEquals(LoginByEmailRequest("test@test.com", "pass"), api.lastLoginByEmailRequest)
    }

    @Test
    fun `loginByEmail error returns error`() = runTest {
        api.loginByEmailResult = AppResult.Error(CommonError.Unknown())

        val result = repository.loginByEmail("test@test.com", "pass")

        assertIs<AppResult.Error>(result)
    }

    @Test
    fun `loginByTotp success maps data`() = runTest {
        api.loginByTotpResult = AppResult.Success(mockPayload)

        val result = repository.loginByTotp("mfa", "123456")

        val success = assertIs<AppResult.Success<AuthData>>(result)
        assertEquals(mockPayload.toAuthData(), success.data)
        assertEquals(VerifyTotpPayload("mfa", "123456"), api.lastLoginByTotpRequest)
    }

    @Test
    fun `unsupported methods return ContractViolation`() = runTest {
        val phoneResult = repository.loginByPhone("123", "456")
        val externalResult = repository.loginByExternalAuthProvider(UserAuthProvider.GOOGLE, "token")
        val sendResult = repository.sendLoginConfirmationToPhone("123")

        val phoneError = assertIs<AppResult.Error>(phoneResult).error
        assertIs<CommonError.ContractViolation>(phoneError)

        val externalError = assertIs<AppResult.Error>(externalResult).error
        assertIs<CommonError.ContractViolation>(externalError)

        val sendError = assertIs<AppResult.Error>(sendResult).error
        assertIs<CommonError.ContractViolation>(sendError)
    }

    @Test
    fun `getRemainingLoginConfirmationDelayInSeconds returns zero`() {
        assertEquals(0, repository.getRemainingLoginConfirmationDelayInSeconds("123"))
    }
}