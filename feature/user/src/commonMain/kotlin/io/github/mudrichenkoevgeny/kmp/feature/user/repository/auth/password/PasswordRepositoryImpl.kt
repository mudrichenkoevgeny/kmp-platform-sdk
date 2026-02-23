package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.password

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.confirmation.toSendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.useridentifier.toUserIdentifier
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.password.PasswordApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.ResetPasswordRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.SendResetPasswordConfirmationRequest

class PasswordRepositoryImpl(
    private val passwordApi: PasswordApi,
    private val confirmationRepository: ConfirmationRepository
) : PasswordRepository {

    override suspend fun resetPassword(
        email: String,
        newPassword: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        return passwordApi.resetPassword(
            ResetPasswordRequest(
                email = email,
                newPassword = newPassword,
                confirmationCode = confirmationCode
            )
        ).mapSuccess { response ->
            response.toUserIdentifier()
        }
    }

    override suspend fun sendResetPasswordConfirmationToEmail(
        email: String
    ): AppResult<SendConfirmationData> {
        return passwordApi.sendResetPasswordConfirmationToEmail(
            SendResetPasswordConfirmationRequest(email = email)
        ).mapSuccess { response ->
            response.toSendConfirmationData()
        }
    }

    override fun getRemainingResetPasswordConfirmationDelayInSeconds(email: String): Int {
        return confirmationRepository.getRemainingDelay(
            type = ConfirmationType.PASSWORD_RESET_EMAIL,
            identifier = email
        )
    }
}