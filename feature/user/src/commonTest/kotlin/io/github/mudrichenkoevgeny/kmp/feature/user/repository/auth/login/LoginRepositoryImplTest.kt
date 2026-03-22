package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.toAuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.confirmation.toSendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.login.LoginApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireAuthDataResponse
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireSendConfirmationResponse
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.core.common.testsupport.MutableEpochTestClock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.AuthDataResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.confirmation.SendConfirmationResponse
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

class LoginRepositoryImplTest {

    @Test
    fun loginByEmail_forwardsRequest_andMapsAuthData() = runTest {
        val wire = wireAuthDataResponse()
        val api = FakeLoginApi().apply { emailResult = AppResult.Success(wire) }
        val repo = LoginRepositoryImpl(api, confirmationRepo())

        val loginResult = repo.loginByEmail(LOGIN_EMAIL, LOGIN_PASSWORD)

        val success = assertIs<AppResult.Success<AuthData>>(loginResult)
        assertEquals(wire.toAuthData(), success.data)
        assertEquals(LoginByEmailRequest(LOGIN_EMAIL, LOGIN_PASSWORD), api.lastEmail)
    }

    @Test
    fun loginByPhone_forwardsRequest_andMapsAuthData() = runTest {
        val wire = wireAuthDataResponse()
        val api = FakeLoginApi().apply { phoneResult = AppResult.Success(wire) }
        val repo = LoginRepositoryImpl(api, confirmationRepo())

        val loginResult = repo.loginByPhone(LOGIN_PHONE, LOGIN_PHONE_CODE)

        val success = assertIs<AppResult.Success<AuthData>>(loginResult)
        assertEquals(wire.toAuthData(), success.data)
        assertEquals(LoginByPhoneRequest(LOGIN_PHONE, LOGIN_PHONE_CODE), api.lastPhone)
    }

    @Test
    fun loginByExternalAuthProvider_forwardsSerialName_andMapsAuthData() = runTest {
        val wire = wireAuthDataResponse()
        val api = FakeLoginApi().apply { externalResult = AppResult.Success(wire) }
        val repo = LoginRepositoryImpl(api, confirmationRepo())

        val loginResult = repo.loginByExternalAuthProvider(UserAuthProvider.GOOGLE, EXTERNAL_ID_TOKEN)

        val success = assertIs<AppResult.Success<AuthData>>(loginResult)
        assertEquals(wire.toAuthData(), success.data)
        assertEquals(
            LoginByExternalAuthProviderRequest(UserAuthProvider.GOOGLE.serialName, EXTERNAL_ID_TOKEN),
            api.lastExternal
        )
    }

    @Test
    fun sendLoginConfirmationToPhone_usesLoginPhoneTimer_andMapsResponse() = runTest {
        val wire = wireSendConfirmationResponse(retryAfterSeconds = CONFIRMATION_RETRY_AFTER_SECONDS)
        val api = FakeLoginApi().apply { phoneSendResult = AppResult.Success(wire) }
        val repo = LoginRepositoryImpl(api, confirmationRepo())

        val first = repo.sendLoginConfirmationToPhone(PHONE)
        assertIs<AppResult.Success<SendConfirmationData>>(first)
        assertEquals(wire.toSendConfirmationData(), first.data)
        assertEquals(SendConfirmationToPhoneRequest(PHONE), api.lastPhoneSend)

        var apiCalledAgain = false
        api.phoneSendHook = {
            apiCalledAgain = true
            AppResult.Success(wireSendConfirmationResponse(ZERO_RETRY_AFTER_SECONDS))
        }
        val blocked = repo.sendLoginConfirmationToPhone(PHONE)
        assertIs<AppResult.Error>(blocked)
        assertIs<UserError.TooManyConfirmationRequests>(blocked.error)
        assertFalse(apiCalledAgain)
    }

    @Test
    fun getRemainingLoginConfirmationDelayInSeconds_delegatesWithLoginPhoneType() {
        val clock = MutableEpochTestClock(BASE_MS)
        val confirmation = ConfirmationRepositoryImpl(clock)
        val repo = LoginRepositoryImpl(FakeLoginApi(), confirmation)

        assertEquals(0, repo.getRemainingLoginConfirmationDelayInSeconds(PHONE))
    }

    private fun confirmationRepo() = ConfirmationRepositoryImpl(MutableEpochTestClock(BASE_MS))

    private class FakeLoginApi : LoginApi {
        var emailResult: AppResult<AuthDataResponse> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var phoneResult: AppResult<AuthDataResponse> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var externalResult: AppResult<AuthDataResponse> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var phoneSendResult: AppResult<SendConfirmationResponse> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var phoneSendHook: (suspend () -> AppResult<SendConfirmationResponse>)? = null

        var lastEmail: LoginByEmailRequest? = null
        var lastPhone: LoginByPhoneRequest? = null
        var lastExternal: LoginByExternalAuthProviderRequest? = null
        var lastPhoneSend: SendConfirmationToPhoneRequest? = null

        override suspend fun loginByEmail(request: LoginByEmailRequest): AppResult<AuthDataResponse> {
            lastEmail = request
            return emailResult
        }

        override suspend fun loginByPhone(request: LoginByPhoneRequest): AppResult<AuthDataResponse> {
            lastPhone = request
            return phoneResult
        }

        override suspend fun loginByExternalAuthProvider(
            request: LoginByExternalAuthProviderRequest
        ): AppResult<AuthDataResponse> {
            lastExternal = request
            return externalResult
        }

        override suspend fun sendLoginConfirmationToPhone(
            request: SendConfirmationToPhoneRequest
        ): AppResult<SendConfirmationResponse> {
            lastPhoneSend = request
            return phoneSendHook?.invoke() ?: phoneSendResult
        }
    }

    private companion object {
        private const val BASE_MS = 1_000_000L
        private const val PHONE = "+15550001111"
        private const val LOGIN_EMAIL = "u@e.com"
        private const val LOGIN_PASSWORD = "secret"
        private const val LOGIN_PHONE = "+1000"
        private const val LOGIN_PHONE_CODE = "999"
        private const val EXTERNAL_ID_TOKEN = "id-token"
        private const val CONFIRMATION_RETRY_AFTER_SECONDS = 5
        private const val ZERO_RETRY_AFTER_SECONDS = 0
        private const val NOT_RETRYABLE = false
    }
}
