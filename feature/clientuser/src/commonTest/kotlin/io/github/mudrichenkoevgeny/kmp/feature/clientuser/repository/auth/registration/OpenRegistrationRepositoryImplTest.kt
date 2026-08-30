package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.registration.RegistrationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.core.common.testsupport.MutableEpochTestClock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.otpconfirmation.otpConfirmationPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.data.authDataPayloadMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.otpconfirmation.toOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.data.AuthDataPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.register.RegisterByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

@InternalApi
class OpenRegistrationRepositoryImplTest {

    @Test
    fun registerByEmail_forwardsRequest_andMapsAuthData() = runTest {
        val wire = authDataPayloadMock()
        val api = FakeRegistrationApi().apply { registerResult = AppResult.Success(wire) }
        val repo = OpenRegistrationRepositoryImpl(api, confirmationRepo())

        val registerResult = repo.registerByEmail(REGISTER_EMAIL, REGISTER_PASSWORD, REGISTER_CODE)

        val success = assertIs<AppResult.Success<AuthData>>(registerResult)
        assertEquals(wire.toAuthData(), success.data)
        assertEquals(RegisterByEmailRequest(REGISTER_EMAIL, REGISTER_PASSWORD, REGISTER_CODE), api.lastRegister)
    }

    @Test
    fun sendRegistrationConfirmationToEmail_usesRegistrationEmailTimer() = runTest {
        val wire = otpConfirmationPayloadMock(retryAfterSeconds = CONFIRMATION_RETRY_AFTER_SECONDS)
        val api = FakeRegistrationApi().apply { sendResult = AppResult.Success(wire) }
        val repo = OpenRegistrationRepositoryImpl(api, confirmationRepo())

        val first = repo.sendRegistrationConfirmationToEmail(EMAIL)

        assertIs<AppResult.Success<OtpConfirmation>>(first)
        assertEquals(wire.toOtpConfirmation(), first.data)
        assertEquals(SendConfirmationToEmailRequest(EMAIL), api.lastSend)

        var sendAgain = false
        api.sendHook = {
            sendAgain = true
            AppResult.Success(otpConfirmationPayloadMock(ZERO_RETRY_AFTER_SECONDS))
        }
        val blocked = repo.sendRegistrationConfirmationToEmail(EMAIL)
        assertIs<AppResult.Error>(blocked)
        assertIs<UserError.TooManyConfirmationRequests>(blocked.error)
        assertFalse(sendAgain)
    }

    @Test
    fun getRemainingRegistrationConfirmationDelayInSeconds_delegatesWithRegistrationEmailType() {
        val repo = OpenRegistrationRepositoryImpl(FakeRegistrationApi(), confirmationRepo())
        assertEquals(0, repo.getRemainingRegistrationConfirmationDelayInSeconds(EMAIL))
    }

    private fun confirmationRepo() = ConfirmationRepositoryImpl(MutableEpochTestClock(BASE_MS))

    private class FakeRegistrationApi : RegistrationApi {
        var registerResult: AppResult<AuthDataPayload> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var sendResult: AppResult<OtpConfirmationPayload> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var sendHook: (suspend () -> AppResult<OtpConfirmationPayload>)? = null

        var lastRegister: RegisterByEmailRequest? = null
        var lastSend: SendConfirmationToEmailRequest? = null

        override suspend fun registerByEmail(request: RegisterByEmailRequest): AppResult<AuthDataPayload> {
            lastRegister = request
            return registerResult
        }

        override suspend fun sendRegistrationConfirmationToEmail(
            request: SendConfirmationToEmailRequest
        ): AppResult<OtpConfirmationPayload> {
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
