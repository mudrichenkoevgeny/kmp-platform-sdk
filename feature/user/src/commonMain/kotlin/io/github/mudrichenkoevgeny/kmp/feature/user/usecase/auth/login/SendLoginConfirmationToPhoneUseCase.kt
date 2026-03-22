package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository

/**
 * Requests a login confirmation code to be sent to the given phone (subject to repository rate limits).
 *
 * @param loginRepository Login repository that performs the send and throttling.
 */
class SendLoginConfirmationToPhoneUseCase(
    private val loginRepository: LoginRepository
) {
    /**
     * @param phoneNumber Target phone for the OTP or SMS challenge.
     * @return [SendConfirmationData] on success, or an error result (including client-side throttling).
     */
    suspend fun execute(phoneNumber: String): AppResult<SendConfirmationData> {
        return loginRepository.sendLoginConfirmationToPhone(phoneNumber)
    }
}