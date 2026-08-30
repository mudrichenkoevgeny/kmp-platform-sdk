package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation

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
     * @return [OtpConfirmation] on success, or an error result (including client-side throttling).
     */
    suspend fun execute(phoneNumber: String): AppResult<OtpConfirmation> {
        return loginRepository.sendLoginConfirmationToPhone(phoneNumber)
    }
}
