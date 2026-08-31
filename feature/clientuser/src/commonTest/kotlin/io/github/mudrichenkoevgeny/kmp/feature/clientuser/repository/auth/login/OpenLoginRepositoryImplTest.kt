package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.confirmation.ConfirmationRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.otpconfirmation.otpConfirmationPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.network.api.auth.login.OpenLoginApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.data.authDataPayloadMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.otpconfirmation.toOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class OpenLoginRepositoryImplTest {

    @Test
    fun loginByEmail_forwardsRequest_andMapsAuthData() = runTest {
        val wire = authDataPayloadMock()
        val api = OpenLoginApiMock().apply { loginByEmailResult = AppResult.Success(wire) }
        val repo = OpenLoginRepositoryImpl(api, ConfirmationRepositoryMock())

        val loginResult = repo.loginByEmail(LOGIN_EMAIL, LOGIN_PASSWORD)

        val success = assertIs<AppResult.Success<AuthData>>(loginResult)
        assertEquals(wire.toAuthData(), success.data)
        assertEquals(LoginByEmailRequest(LOGIN_EMAIL, LOGIN_PASSWORD), api.lastLoginByEmailRequest)
    }

    @Test
    fun loginByPhone_forwardsRequest_andMapsAuthData() = runTest {
        val wire = authDataPayloadMock()
        val api = OpenLoginApiMock().apply { loginByPhoneResult = AppResult.Success(wire) }
        val repo = OpenLoginRepositoryImpl(api, ConfirmationRepositoryMock())

        val loginResult = repo.loginByPhone(LOGIN_PHONE, LOGIN_PHONE_CODE)

        val success = assertIs<AppResult.Success<AuthData>>(loginResult)
        assertEquals(wire.toAuthData(), success.data)
        assertEquals(LoginByPhoneRequest(LOGIN_PHONE, LOGIN_PHONE_CODE), api.lastLoginByPhoneRequest)
    }

    @Test
    fun loginByExternalAuthProvider_forwardsSerialName_andMapsAuthData() = runTest {
        val wire = authDataPayloadMock()
        val api = OpenLoginApiMock().apply { loginByExternalAuthProviderResult = AppResult.Success(wire) }
        val repo = OpenLoginRepositoryImpl(api, ConfirmationRepositoryMock())

        val loginResult = repo.loginByExternalAuthProvider(UserAuthProvider.GOOGLE, EXTERNAL_ID_TOKEN)

        val success = assertIs<AppResult.Success<AuthData>>(loginResult)
        assertEquals(wire.toAuthData(), success.data)
        assertEquals(
            LoginByExternalAuthProviderRequest(UserAuthProvider.GOOGLE.serialName, EXTERNAL_ID_TOKEN),
            api.lastLoginByExternalAuthProviderRequest
        )
    }

    @Test
    fun sendLoginConfirmationToPhone_usesLoginPhoneTimer_andMapsResponse() = runTest {
        val wire = otpConfirmationPayloadMock(retryAfterSeconds = CONFIRMATION_RETRY_AFTER_SECONDS)
        val api = OpenLoginApiMock().apply { sendLoginConfirmationToPhoneResult = AppResult.Success(wire) }
        val confirmationMock = ConfirmationRepositoryMock()
        val repo = OpenLoginRepositoryImpl(api, confirmationMock)

        val first = repo.sendLoginConfirmationToPhone(PHONE)

        assertIs<AppResult.Success<OtpConfirmation>>(first)
        assertEquals(wire.toOtpConfirmation(), first.data)
        assertEquals(SendConfirmationToPhoneRequest(PHONE), api.lastSendLoginConfirmationToPhoneRequest)
        assertEquals(ConfirmationType.LOGIN_PHONE, confirmationMock.lastType)
        assertEquals(PHONE, confirmationMock.lastIdentifier)

        val expectedError = UserError.TooManyConfirmationRequests(CONFIRMATION_RETRY_AFTER_SECONDS)
        confirmationMock.executeWithTimerResult = AppResult.Error(expectedError)
        
        val blocked = repo.sendLoginConfirmationToPhone(PHONE)
        assertIs<AppResult.Error>(blocked)
        assertEquals(expectedError, blocked.error)
    }

    @Test
    fun getRemainingLoginConfirmationDelayInSeconds_delegatesWithLoginPhoneType() {
        val confirmationMock = ConfirmationRepositoryMock().apply { delayReturn = 42 }
        val repo = OpenLoginRepositoryImpl(OpenLoginApiMock(), confirmationMock)

        assertEquals(42, repo.getRemainingLoginConfirmationDelayInSeconds(PHONE))
        assertEquals(ConfirmationType.LOGIN_PHONE, confirmationMock.lastType)
        assertEquals(PHONE, confirmationMock.lastIdentifier)
    }

    private companion object {
        private const val PHONE = "+15550001111"
        private const val LOGIN_EMAIL = "u@e.com"
        private const val LOGIN_PASSWORD = "secret"
        private const val LOGIN_PHONE = "+1000"
        private const val LOGIN_PHONE_CODE = "999"
        private const val EXTERNAL_ID_TOKEN = "id-token"
        private const val CONFIRMATION_RETRY_AFTER_SECONDS = 5
        private const val ZERO_RETRY_AFTER_SECONDS = 0
    }
}
