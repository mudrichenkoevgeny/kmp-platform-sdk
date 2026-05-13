package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.identifier.userIdentifierPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.resetpassword.ResetPasswordApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.ResetPasswordRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.SendResetPasswordConfirmationRequest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ResetPasswordRepositoryImplTest {

    @Test
    fun resetPassword_forwardsRequest_andMapsUserIdentifier() = runTest {
        val wire = userIdentifierPayloadMock(identifier = RESET_EMAIL)
        val api = FakeResetPasswordApi().apply { resetResult = AppResult.Success(wire) }
        val confirmation = FakeConfirmationRepository()
        val repo = ResetPasswordRepositoryImpl(api, confirmation)

        val resetResult = repo.resetPassword(RESET_EMAIL, NEW_PASSWORD, CONFIRMATION_CODE)

        val success = assertIs<AppResult.Success<UserIdentifier>>(resetResult)
        assertEquals(wire.toUserIdentifier(), success.data)
        assertEquals(
            ResetPasswordRequest(
                email = RESET_EMAIL,
                newPassword = NEW_PASSWORD,
                confirmationCode = CONFIRMATION_CODE
            ),
            api.lastReset
        )
    }

    @Test
    fun getRemainingResetPasswordConfirmationDelay_delegatesWithPasswordResetEmailType() {
        val confirmation = FakeConfirmationRepository().apply { delayReturn = REMAINING_DELAY_SECONDS }
        val repo = ResetPasswordRepositoryImpl(FakeResetPasswordApi(), confirmation)

        assertEquals(REMAINING_DELAY_SECONDS, repo.getRemainingResetPasswordConfirmationDelayInSeconds(REMAINING_DELAY_EMAIL))
        assertEquals(ConfirmationType.PASSWORD_RESET_EMAIL, confirmation.lastType)
        assertEquals(REMAINING_DELAY_EMAIL, confirmation.lastIdentifier)
    }

    private class FakeResetPasswordApi : ResetPasswordApi {
        var resetResult: AppResult<UserIdentifierPayload> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var sendResult: AppResult<OtpConfirmationPayload> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var lastReset: ResetPasswordRequest? = null
        var lastSend: SendResetPasswordConfirmationRequest? = null

        override suspend fun resetPassword(request: ResetPasswordRequest): AppResult<UserIdentifierPayload> {
            lastReset = request
            return resetResult
        }

        override suspend fun sendResetPasswordConfirmationToEmail(
            request: SendResetPasswordConfirmationRequest
        ): AppResult<OtpConfirmationPayload> {
            lastSend = request
            return sendResult
        }
    }

    private class FakeConfirmationRepository : ConfirmationRepository {
        var delayReturn: Int = 0
        var lastType: ConfirmationType? = null
        var lastIdentifier: String? = null

        override suspend fun <T> executeWithTimer(
            type: ConfirmationType,
            identifier: String,
            action: suspend () -> AppResult<T>
        ): AppResult<T> = error(STUB_NOT_USED)

        override fun getRemainingDelay(type: ConfirmationType, identifier: String): Int {
            lastType = type
            lastIdentifier = identifier
            return delayReturn
        }
    }

    private companion object {
        private const val RESET_EMAIL = "who@example.com"
        private const val NEW_PASSWORD = "new-secret"
        private const val CONFIRMATION_CODE = "123456"
        private const val REMAINING_DELAY_SECONDS = 11
        private const val REMAINING_DELAY_EMAIL = "e@mail.com"
        private const val NOT_RETRYABLE = false
        private const val STUB_NOT_USED = "not used in PasswordRepositoryImpl tests"
    }
}
