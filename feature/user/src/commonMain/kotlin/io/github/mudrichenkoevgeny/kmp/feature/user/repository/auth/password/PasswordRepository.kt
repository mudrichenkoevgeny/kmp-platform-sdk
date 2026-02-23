package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.password

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier

interface PasswordRepository {
    suspend fun resetPassword(
        email: String,
        newPassword: String,
        confirmationCode: String
    ): AppResult<UserIdentifier>

    suspend fun sendResetPasswordConfirmationToEmail(
        email: String
    ): AppResult<SendConfirmationData>

    fun getRemainingResetPasswordConfirmationDelayInSeconds(email: String): Int
}