package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration.RegistrationRepository

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
     * @return [SendConfirmationData] on success, or an error result (including client-side throttling).
     */
    suspend fun execute(email: String): AppResult<SendConfirmationData> {
        return registrationRepository.sendRegistrationConfirmationToEmail(email)
    }
}