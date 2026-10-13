package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate

@InternalApi
class SelfManagementResetPasswordRepositoryMock : ResetPasswordRepository {

    var resetResult: AppResult<UserIdentifierPrivate> = AppResult.Error(
        CommonError.ContractViolation(
            throwable = IllegalStateException("No mock result provided for resetPassword.")
        )
    )

    var sendConfirmationResult: AppResult<OtpConfirmation> = AppResult.Error(
        CommonError.ContractViolation(
            throwable = IllegalStateException("No mock result provided for sendResetPasswordConfirmationToEmail.")
        )
    )

    var remainingDelaySeconds: Int = 0

    var lastResetEmail: String? = null
    var lastResetPassword: String? = null
    var lastResetConfirmationCode: String? = null
    var lastSendConfirmationEmail: String? = null

    override suspend fun resetPassword(
        email: String,
        newPassword: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> {
        lastResetEmail = email
        lastResetPassword = newPassword
        lastResetConfirmationCode = confirmationCode
        return resetResult
    }

    override suspend fun sendResetPasswordConfirmationToEmail(email: String): AppResult<OtpConfirmation> {
        lastSendConfirmationEmail = email
        return sendConfirmationResult
    }

    override fun getRemainingResetPasswordConfirmationDelayInSeconds(email: String): Int {
        return remainingDelaySeconds
    }
}