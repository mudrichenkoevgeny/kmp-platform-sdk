package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.otpconfirmation.otpConfirmationPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.auth.resetpassword.SelfManagementResetPasswordApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.identifier.userIdentifierPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.confirmation.ConfirmationRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.otpconfirmation.toOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.ResetPasswordRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.SendResetPasswordConfirmationRequest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class SelfManagementResetPasswordRepositoryImplTest {

    private val api = SelfManagementResetPasswordApiMock()
    private val confirmation = ConfirmationRepositoryMock()
    private val repo = SelfManagementResetPasswordRepositoryImpl(api, confirmation)

    @Test
    fun `resetPassword forwards request and maps user identifier`() = runTest {
        val wire = userIdentifierPayloadMock(identifier = RESET_EMAIL)
        api.resetPasswordResult = AppResult.Success(wire)

        val result = repo.resetPassword(RESET_EMAIL, NEW_PASSWORD, CONFIRMATION_CODE)

        val success = assertIs<AppResult.Success<UserIdentifier>>(result)
        assertEquals(wire.toUserIdentifier(), success.data)
        assertEquals(
            ResetPasswordRequest(
                email = RESET_EMAIL,
                newPassword = NEW_PASSWORD,
                confirmationCode = CONFIRMATION_CODE
            ),
            api.lastResetPasswordRequest
        )
    }

    @Test
    fun `sendResetPasswordConfirmationToEmail forwards request and maps response`() = runTest {
        val wire = otpConfirmationPayloadMock()
        api.sendResetPasswordConfirmationResult = AppResult.Success(wire)

        val result = repo.sendResetPasswordConfirmationToEmail(RESET_EMAIL)

        val success = assertIs<AppResult.Success<*>>(result)
        assertEquals(wire.toOtpConfirmation(), success.data)
        assertEquals(
            SendResetPasswordConfirmationRequest(RESET_EMAIL),
            api.lastSendResetPasswordConfirmationRequest
        )
    }

    @Test
    fun `getRemainingResetPasswordConfirmationDelay delegates with correct type`() {
        confirmation.delayReturn = REMAINING_DELAY_SECONDS

        val result = repo.getRemainingResetPasswordConfirmationDelayInSeconds(REMAINING_DELAY_EMAIL)

        assertEquals(REMAINING_DELAY_SECONDS, result)
        assertEquals(ConfirmationType.PASSWORD_RESET_EMAIL, confirmation.lastType)
        assertEquals(REMAINING_DELAY_EMAIL, confirmation.lastIdentifier)
    }

    @Test
    fun `resetPassword propagates api error`() = runTest {
        api.resetPasswordResult = AppResult.Error(CommonError.Unknown())

        val result = repo.resetPassword(RESET_EMAIL, NEW_PASSWORD, CONFIRMATION_CODE)

        assertIs<AppResult.Error>(result)
    }

    private companion object {
        private const val RESET_EMAIL = "who@example.com"
        private const val NEW_PASSWORD = "new-secret"
        private const val CONFIRMATION_CODE = "123456"
        private const val REMAINING_DELAY_SECONDS = 11
        private const val REMAINING_DELAY_EMAIL = "e@mail.com"
    }
}