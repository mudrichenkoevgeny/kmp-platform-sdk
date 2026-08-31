package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

/**
 * Completes password reset for an email-based flow using a server-issued confirmation code.
 *
 * @param resetPasswordRepository Password recovery API surface.
 */
open class ResetEmailPasswordUseCase(
    private val resetPasswordRepository: ResetPasswordRepository
) {
    /**
     * @param email Account email used in the reset flow.
     * @param newPassword Replacement password after verification.
     * @param confirmationCode One-time code from the confirmation email.
     * @return [UserIdentifier] context on success, or an error result when reset is rejected.
     */
    open suspend fun execute(
        email: String,
        newPassword: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        return resetPasswordRepository.resetPassword(
            email = email,
            newPassword = newPassword,
            confirmationCode = confirmationCode
        )
    }
}