package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation

/**
 * Triggers a confirmation challenge for email association.
 *
 * @param identifierRepository Remote identifier management API.
 */
class SendAddEmailIdentifierConfirmationUseCase(
    private val identifierRepository: IdentifierRepository
) {
    /**
     * @param email Target email address.
     * @return [OtpConfirmation] details on success, or a mapped failure.
     */
    suspend operator fun invoke(email: String): AppResult<OtpConfirmation> {
        return identifierRepository.sendAddEmailIdentifierConfirmation(email)
    }
}
