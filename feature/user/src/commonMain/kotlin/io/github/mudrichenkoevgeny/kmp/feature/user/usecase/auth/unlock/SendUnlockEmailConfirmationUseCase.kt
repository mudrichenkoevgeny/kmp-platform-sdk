package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.unlock.UnlockRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation

/** Sends an account unlock confirmation code to the target email. */
open class SendUnlockEmailConfirmationUseCase(
    private val unlockRepository: UnlockRepository
) {
    /**
     * Executes sending an email unlock confirmation code.
     *
     * @param email Target email address.
     * @return [OtpConfirmation] on success, or an error result.
     */
    open suspend fun execute(email: String): AppResult<OtpConfirmation> {
        return unlockRepository.sendUnlockEmailConfirmation(email)
    }
}
