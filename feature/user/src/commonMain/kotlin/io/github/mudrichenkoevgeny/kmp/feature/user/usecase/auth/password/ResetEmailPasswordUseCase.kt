package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.password

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.password.PasswordRepository

class ResetEmailPasswordUseCase(
    private val passwordRepository: PasswordRepository
) {
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