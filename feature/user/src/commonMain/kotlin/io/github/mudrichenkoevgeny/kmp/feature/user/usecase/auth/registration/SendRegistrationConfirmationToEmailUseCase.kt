package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration.RegistrationRepository

class SendRegistrationConfirmationToEmailUseCase(
    private val registrationRepository: RegistrationRepository
) {
    suspend fun execute(email: String): AppResult<SendConfirmationData> {
        return registrationRepository.sendRegistrationConfirmationToEmail(email)
    }
}