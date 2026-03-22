package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.password

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.password.PasswordRepository

/**
 * Completes password reset for an email-based flow using a server-issued confirmation code.
 *
 * @param passwordRepository Password recovery API surface.
 */
class ResetEmailPasswordUseCase(
    private val passwordRepository: PasswordRepository
) {
    /**
     * @param email Account email used in the reset flow.
     * @param newPassword Replacement password after verification.
     * @param confirmationCode One-time code from the confirmation email.
     * @return [UserIdentifier] context on success, or an error result when reset is rejected.
     */
    suspend fun execute(
        email: String,
        newPassword: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        return passwordRepository.resetPassword(
            email = email,
            newPassword = newPassword,
            confirmationCode = confirmationCode
        )
    }
}