package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.toAuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.confirmation.toSendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.registration.RegistrationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireAuthDataResponse
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireSendConfirmationResponse
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.core.common.testsupport.MutableEpochTestClock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.register.RegisterByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.AuthDataResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.confirmation.SendConfirmationResponse
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

class RegistrationRepositoryImplTest {

    @Test
    fun registerByEmail_forwardsRequest_andMapsAuthData() = runTest {
        val wire = wireAuthDataResponse()
        val api = FakeRegistrationApi().apply { registerResult = AppResult.Success(wire) }
        val repo = RegistrationRepositoryImpl(api, confirmationRepo())

        val registerResult = repo.registerByEmail(REGISTER_EMAIL, REGISTER_PASSWORD, REGISTER_CODE)

        val success = assertIs<AppResult.Success<AuthData>>(registerResult)
        assertEquals(wire.toAuthData(), success.data)
        assertEquals(RegisterByEmailRequest(REGISTER_EMAIL, REGISTER_PASSWORD, REGISTER_CODE), api.lastRegister)
    }

    @Test
    fun sendRegistrationConfirmationToEmail_usesRegistrationEmailTimer() = runTest {
        val wire = wireSendConfirmationResponse(retryAfterSeconds = CONFIRMATION_RETRY_AFTER_SECONDS)
        val api = FakeRegistrationApi().apply { sendResult = AppResult.Success(wire) }
        val repo = RegistrationRepositoryImpl(api, confirmationRepo())

        val first = repo.sendRegistrationConfirmationToEmail(EMAIL)
        assertIs<AppResult.Success<SendConfirmationData>>(first)
        assertEquals(wire.toSendConfirmationData(), first.data)
        assertEquals(SendConfirmationToEmailRequest(EMAIL), api.lastSend)

        var sendAgain = false
        api.sendHook = {
            sendAgain = true
            AppResult.Success(wireSendConfirmationResponse(ZERO_RETRY_AFTER_SECONDS))
        }
        val blocked = repo.sendRegistrationConfirmationToEmail(EMAIL)
        assertIs<AppResult.Error>(blocked)
        assertIs<UserError.TooManyConfirmationRequests>(blocked.error)
        assertFalse(sendAgain)
    }

    @Test
    fun getRemainingRegistrationConfirmationDelayInSeconds_delegatesWithRegistrationEmailType() {
        val repo = RegistrationRepositoryImpl(FakeRegistrationApi(), confirmationRepo())
        assertEquals(0, repo.getRemainingRegistrationConfirmationDelayInSeconds(EMAIL))
    }

    private fun confirmationRepo() = ConfirmationRepositoryImpl(MutableEpochTestClock(BASE_MS))

    private class FakeRegistrationApi : RegistrationApi {
        var registerResult: AppResult<AuthDataResponse> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var sendResult: AppResult<SendConfirmationResponse> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var sendHook: (suspend () -> AppResult<SendConfirmationResponse>)? = null

        var lastRegister: RegisterByEmailRequest? = null
        var lastSend: SendConfirmationToEmailRequest? = null

        override suspend fun registerByEmail(request: RegisterByEmailRequest): AppResult<AuthDataResponse> {
            lastRegister = request
            return registerResult
        }

        override suspend fun sendRegistrationConfirmationToEmail(
            request: SendConfirmationToEmailRequest
        ): AppResult<SendConfirmationResponse> {
            lastSend = request
            return sendHook?.invoke() ?: sendResult
        }
    }

    private companion object {
        private const val BASE_MS = 1_000_000L
        private const val EMAIL = "reg@example.com"
        private const val REGISTER_EMAIL = "n@e.com"
        private const val REGISTER_PASSWORD = "pw"
        private const val REGISTER_CODE = "code"
        private const val CONFIRMATION_RETRY_AFTER_SECONDS = 8
        private const val ZERO_RETRY_AFTER_SECONDS = 0
        private const val NOT_RETRYABLE = false
    }
}
