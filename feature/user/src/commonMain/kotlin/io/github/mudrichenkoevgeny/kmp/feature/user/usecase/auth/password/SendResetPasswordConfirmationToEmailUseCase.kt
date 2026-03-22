package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.password

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.password.PasswordRepository

/**
 * Sends a password-reset confirmation to the account email via [PasswordRepository].
 *
 * @param passwordRepository Password recovery API surface.
 */
class SendResetPasswordConfirmationToEmailUseCase(
    private val passwordRepository: PasswordRepository
) {
    /**
     * @param email Address that should receive the reset code or link.
     * @return [SendConfirmationData] on success, or an error result from the repository.
     */
    suspend fun execute(email: String): AppResult<SendConfirmationData> {
        return passwordRepository.sendResetPasswordConfirmationToEmail(email)
    }
}