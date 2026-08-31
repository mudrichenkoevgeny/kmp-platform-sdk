package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation

/**
 * Triggers a confirmation challenge for phone association.
 *
 * @param identifierRepository Remote identifier management API.
 */
class SendAddPhoneIdentifierConfirmationUseCase(
    private val identifierRepository: IdentifierRepository
) {
    /**
     * @param phoneNumber Target phone number.
     * @return [OtpConfirmation] details on success, or a mapped failure.
     */
    suspend operator fun invoke(phoneNumber: String): AppResult<OtpConfirmation> {
        return identifierRepository.sendAddPhoneIdentifierConfirmation(phoneNumber)
    }
}
