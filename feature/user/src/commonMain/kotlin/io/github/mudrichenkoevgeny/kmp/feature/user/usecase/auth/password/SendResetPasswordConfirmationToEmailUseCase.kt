package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.password

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.password.PasswordRepository

class SendResetPasswordConfirmationToEmailUseCase(
    private val passwordRepository: PasswordRepository
) {
    suspend fun execute(email: String): AppResult<SendConfirmationData> {
        return passwordRepository.sendResetPasswordConfirmationToEmail(email)
    }
}