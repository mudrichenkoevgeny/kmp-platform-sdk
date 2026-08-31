package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.confirmation.ConfirmationRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.otpconfirmation.otpConfirmationPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.network.api.auth.registration.OpenRegistrationApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.data.authDataPayloadMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.otpconfirmation.toOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.auth.data.toAuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.register.RegisterByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class OpenRegistrationRepositoryImplTest {

    @Test
    fun registerByEmail_forwardsRequest_andMapsAuthData() = runTest {
        val wire = authDataPayloadMock()
        val api = OpenRegistrationApiMock().apply { registerByEmailResult = AppResult.Success(wire) }
        val repo = OpenRegistrationRepositoryImpl(api, ConfirmationRepositoryMock())

        val registerResult = repo.registerByEmail(REGISTER_EMAIL, REGISTER_PASSWORD, REGISTER_CODE)

        val success = assertIs<AppResult.Success<AuthData>>(registerResult)
        assertEquals(wire.toAuthData(), success.data)
        assertEquals(RegisterByEmailRequest(REGISTER_EMAIL, REGISTER_PASSWORD, REGISTER_CODE), api.lastRegisterByEmailRequest)
    }

    @Test
    fun sendRegistrationConfirmationToEmail_usesRegistrationEmailTimer() = runTest {
        val wire = otpConfirmationPayloadMock(retryAfterSeconds = CONFIRMATION_RETRY_AFTER_SECONDS)
        val api = OpenRegistrationApiMock().apply { sendRegistrationConfirmationToEmailResult = AppResult.Success(wire) }
        val confirmationMock = ConfirmationRepositoryMock()
        val repo = OpenRegistrationRepositoryImpl(api, confirmationMock)

        val first = repo.sendRegistrationConfirmationToEmail(EMAIL)

        assertIs<AppResult.Success<OtpConfirmation>>(first)
        assertEquals(wire.toOtpConfirmation(), first.data)
        assertEquals(SendConfirmationToEmailRequest(EMAIL), api.lastSendRegistrationConfirmationToEmailRequest)
        assertEquals(ConfirmationType.REGISTRATION_EMAIL, confirmationMock.lastType)
        assertEquals(EMAIL, confirmationMock.lastIdentifier)

        val expectedError = UserError.TooManyConfirmationRequests(CONFIRMATION_RETRY_AFTER_SECONDS)
        confirmationMock.executeWithTimerResult = AppResult.Error(expectedError)

        val blocked = repo.sendRegistrationConfirmationToEmail(EMAIL)
        assertIs<AppResult.Error>(blocked)
        assertEquals(expectedError, blocked.error)
    }

    @Test
    fun getRemainingRegistrationConfirmationDelayInSeconds_delegatesWithRegistrationEmailType() {
        val confirmationMock = ConfirmationRepositoryMock().apply { delayReturn = 42 }
        val repo = OpenRegistrationRepositoryImpl(OpenRegistrationApiMock(), confirmationMock)

        assertEquals(42, repo.getRemainingRegistrationConfirmationDelayInSeconds(EMAIL))
        assertEquals(ConfirmationType.REGISTRATION_EMAIL, confirmationMock.lastType)
        assertEquals(EMAIL, confirmationMock.lastIdentifier)
    }

    private companion object {
        private const val EMAIL = "reg@example.com"
        private const val REGISTER_EMAIL = "n@e.com"
        private const val REGISTER_PASSWORD = "pw"
        private const val REGISTER_CODE = "code"
        private const val CONFIRMATION_RETRY_AFTER_SECONDS = 8
    }
}
