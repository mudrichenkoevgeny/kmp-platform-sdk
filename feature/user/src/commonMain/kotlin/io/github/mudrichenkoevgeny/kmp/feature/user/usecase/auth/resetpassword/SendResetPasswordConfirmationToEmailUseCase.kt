package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation

/**
 * Sends a password-reset confirmation to the account email via [ResetPasswordRepository].
 *
 * @param resetPasswordRepository Password recovery API surface.
 */
open class SendResetPasswordConfirmationToEmailUseCase(
    private val resetPasswordRepository: ResetPasswordRepository
) {
    /**
     * @param email Address that should receive the reset code or link.
     * @return [OtpConfirmation] on success, or an error result from the repository.
     */
    open suspend fun execute(email: String): AppResult<OtpConfirmation> {
        return resetPasswordRepository.sendResetPasswordConfirmationToEmail(email)
    }
}