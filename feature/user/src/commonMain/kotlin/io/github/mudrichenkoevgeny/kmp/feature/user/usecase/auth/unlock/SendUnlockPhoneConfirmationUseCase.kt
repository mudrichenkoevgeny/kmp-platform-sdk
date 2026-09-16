package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.unlock.UnlockRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation

/** Sends an account unlock confirmation code to the target phone number. */
open class SendUnlockPhoneConfirmationUseCase(
    private val unlockRepository: UnlockRepository
) {
    /**
     * Executes sending a phone unlock confirmation code.
     *
     * @param phoneNumber Target phone number.
     * @return [OtpConfirmation] on success, or an error result.
     */
    open suspend fun execute(phoneNumber: String): AppResult<OtpConfirmation> {
        return unlockRepository.sendUnlockPhoneConfirmation(phoneNumber)
    }
}
