package io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.repository.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.otpConfirmationMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

@InternalApi
class ResetPasswordRepositoryMock : ResetPasswordRepository {
    var remainingDelaySeconds: Int = 10
    var sendResult: AppResult<OtpConfirmation> =
        AppResult.Success(otpConfirmationMock(retryAfterSeconds = remainingDelaySeconds))
    var resetResult: AppResult<UserIdentifier> =
        AppResult.Success(userIdentifierMock())

    var lastSendEmail: String? = null
    var lastResetEmail: String? = null
    var lastResetPassword: String? = null
    var lastResetCode: String? = null

    override fun getRemainingResetPasswordConfirmationDelayInSeconds(email: String): Int =
        remainingDelaySeconds

    override suspend fun sendResetPasswordConfirmationToEmail(email: String): AppResult<OtpConfirmation> {
        lastSendEmail = email
        return sendResult
    }

    override suspend fun resetPassword(
        email: String,
        newPassword: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        lastResetEmail = email
        lastResetPassword = newPassword
        lastResetCode = confirmationCode
        return resetResult
    }
}