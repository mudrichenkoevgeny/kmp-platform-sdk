package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration.RegistrationRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation

/**
 * Sends a registration confirmation code to the sign-up email address (subject to repository rate limits).
 *
 * @param registrationRepository Registration repository that performs the send.
 */
class SendRegistrationConfirmationToEmailUseCase(
    private val registrationRepository: RegistrationRepository
) {
    /**
     * @param email Destination address for the registration code.
     * @return [OtpConfirmation] on success, or an error result (including client-side throttling).
     */
    suspend fun execute(email: String): AppResult<OtpConfirmation> {
        return registrationRepository.sendRegistrationConfirmationToEmail(email)
    }
}
