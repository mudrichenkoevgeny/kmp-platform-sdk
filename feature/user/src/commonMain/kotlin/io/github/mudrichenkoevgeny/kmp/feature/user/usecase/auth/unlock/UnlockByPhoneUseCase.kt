package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.unlock.UnlockRepository

/** Unlocks an account using a phone confirmation code. */
open class UnlockByPhoneUseCase(
    private val unlockRepository: UnlockRepository
) {
    /**
     * Unlocks an account with a phone code.
     *
     * @param phoneNumber Account phone number.
     * @param confirmationCode Verification code.
     * @return Unit result on success, or an error result.
     */
    open suspend fun execute(phoneNumber: String, confirmationCode: String): AppResult<Unit> {
        return unlockRepository.unlockByPhone(phoneNumber, confirmationCode)
    }
}
