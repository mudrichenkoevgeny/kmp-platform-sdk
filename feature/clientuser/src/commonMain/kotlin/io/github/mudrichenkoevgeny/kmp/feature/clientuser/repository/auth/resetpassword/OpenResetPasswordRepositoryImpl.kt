package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.resetpassword.ResetPasswordApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.otpconfirmation.toOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.ResetPasswordRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.SendResetPasswordConfirmationRequest

/**
 * Implements [ResetPasswordRepository] via [ResetPasswordApi], using [ConfirmationRepository] for send-code
 * cooldown state keyed by email.
 *
 * @param resetPasswordApi HTTP endpoints for reset and confirmation send.
 * @param confirmationRepository Rate limiting for password-reset email confirmation sends.
 */
class OpenResetPasswordRepositoryImpl(
    private val resetPasswordApi: ResetPasswordApi,
    private val confirmationRepository: ConfirmationRepository
) : ResetPasswordRepository {

    override suspend fun resetPassword(
        email: String,
        newPassword: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        return resetPasswordApi.resetPassword(
            ResetPasswordRequest(
                email = email,
                newPassword = newPassword,
                confirmationCode = confirmationCode
            )
        ).mapSuccess { response ->
            response.toUserIdentifier()
        }
    }

    override suspend fun sendResetPasswordConfirmationToEmail(
        email: String
    ): AppResult<OtpConfirmation> {
        return resetPasswordApi.sendResetPasswordConfirmationToEmail(
            SendResetPasswordConfirmationRequest(email = email)
        ).mapSuccess { response ->
            response.toOtpConfirmation()
        }
    }

    override fun getRemainingResetPasswordConfirmationDelayInSeconds(email: String): Int {
        return confirmationRepository.getRemainingDelay(
            type = ConfirmationType.PASSWORD_RESET_EMAIL,
            identifier = email
        )
    }
}