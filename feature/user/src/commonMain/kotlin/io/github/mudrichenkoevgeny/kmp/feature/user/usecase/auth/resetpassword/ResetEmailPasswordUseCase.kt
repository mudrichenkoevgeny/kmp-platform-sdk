package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate

/**
 * Resets account password using an email confirmation code via [ResetPasswordRepository].
 *
 * @param resetPasswordRepository Remote reset password API.
 */
open class ResetEmailPasswordUseCase(
    private val resetPasswordRepository: ResetPasswordRepository
) {
    /**
     * @param email Account email.
     * @param newPassword New password string.
     * @param confirmationCode Verification code delivered to email.
     * @return [UserIdentifierPrivate] context on success, or an error result when reset is rejected.
     */
    open suspend fun execute(
        email: String,
        newPassword: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> {
        return resetPasswordRepository.resetPassword(
            email = email,
            newPassword = newPassword,
            confirmationCode = confirmationCode
        )
    }
}