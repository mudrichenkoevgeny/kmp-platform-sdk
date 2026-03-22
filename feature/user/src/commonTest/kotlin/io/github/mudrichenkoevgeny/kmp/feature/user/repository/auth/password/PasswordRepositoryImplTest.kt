package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.password

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.confirmation.toSendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.useridentifier.toUserIdentifier
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.password.PasswordApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireSendConfirmationResponse
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireUserIdentifierResponse
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.ResetPasswordRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.SendResetPasswordConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.confirmation.SendConfirmationResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.useridentifier.UserIdentifierResponse
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PasswordRepositoryImplTest {

    @Test
    fun resetPassword_forwardsRequest_andMapsUserIdentifier() = runTest {
        val wire = wireUserIdentifierResponse(identifier = RESET_EMAIL)
        val api = FakePasswordApi().apply { resetResult = AppResult.Success(wire) }
        val confirmation = FakeConfirmationRepository()
        val repo = PasswordRepositoryImpl(api, confirmation)

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
    fun sendResetPasswordConfirmationToEmail_mapsSendConfirmationData() = runTest {
        val wire = wireSendConfirmationResponse(retryAfterSeconds = SEND_RETRY_AFTER_SECONDS)
        val api = FakePasswordApi().apply { sendResult = AppResult.Success(wire) }
        val repo = PasswordRepositoryImpl(api, FakeConfirmationRepository())

        val sendResult = repo.sendResetPasswordConfirmationToEmail(SEND_CONFIRMATION_EMAIL)

        val success = assertIs<AppResult.Success<SendConfirmationData>>(sendResult)
        assertEquals(wire.toSendConfirmationData(), success.data)
        assertEquals(SendResetPasswordConfirmationRequest(email = SEND_CONFIRMATION_EMAIL), api.lastSend)
    }

    @Test
    fun getRemainingResetPasswordConfirmationDelay_delegatesWithPasswordResetEmailType() {
        val confirmation = FakeConfirmationRepository().apply { delayReturn = REMAINING_DELAY_SECONDS }
        val repo = PasswordRepositoryImpl(FakePasswordApi(), confirmation)

        assertEquals(REMAINING_DELAY_SECONDS, repo.getRemainingResetPasswordConfirmationDelayInSeconds(REMAINING_DELAY_EMAIL))
        assertEquals(ConfirmationType.PASSWORD_RESET_EMAIL, confirmation.lastType)
        assertEquals(REMAINING_DELAY_EMAIL, confirmation.lastIdentifier)
    }

    private class FakePasswordApi : PasswordApi {
        var resetResult: AppResult<UserIdentifierResponse> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var sendResult: AppResult<SendConfirmationResponse> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var lastReset: ResetPasswordRequest? = null
        var lastSend: SendResetPasswordConfirmationRequest? = null

        override suspend fun resetPassword(request: ResetPasswordRequest): AppResult<UserIdentifierResponse> {
            lastReset = request
            return resetResult
        }

        override suspend fun sendResetPasswordConfirmationToEmail(
            request: SendResetPasswordConfirmationRequest
        ): AppResult<SendConfirmationResponse> {
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
        private const val SEND_CONFIRMATION_EMAIL = "x@y.z"
        private const val SEND_RETRY_AFTER_SECONDS = 15
        private const val REMAINING_DELAY_SECONDS = 11
        private const val REMAINING_DELAY_EMAIL = "e@mail.com"
        private const val NOT_RETRYABLE = false
        private const val STUB_NOT_USED = "not used in PasswordRepositoryImpl tests"
    }
}
