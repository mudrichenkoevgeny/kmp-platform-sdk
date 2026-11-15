package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.otpconfirmation.createTestOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierPrivate

/**
 * Mock implementation of [ResetPasswordRepository] for testing.
 */
@InternalApi
public class ResetPasswordRepositoryMock : ResetPasswordRepository {
    public var remainingDelaySeconds: Int = 10
    public var sendResult: AppResult<OtpConfirmation> =
        AppResult.Success(createTestOtpConfirmation(retryAfterSeconds = remainingDelaySeconds))
    public var resetResult: AppResult<UserIdentifierPrivate> =
        AppResult.Success(createTestUserIdentifierPrivate())

    public var lastSendEmail: String? = null
    public var lastResetEmail: String? = null
    public var lastResetPassword: String? = null
    public var lastResetCode: String? = null

    public override fun getRemainingResetPasswordConfirmationDelayInSeconds(email: String): Int =
        remainingDelaySeconds

    public override suspend fun sendResetPasswordConfirmationToEmail(email: String): AppResult<OtpConfirmation> {
        lastSendEmail = email
        return sendResult
    }

    public override suspend fun resetPassword(
        email: String,
        newPassword: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> {
        lastResetEmail = email
        lastResetPassword = newPassword
        lastResetCode = confirmationCode
        return resetResult
    }
}
