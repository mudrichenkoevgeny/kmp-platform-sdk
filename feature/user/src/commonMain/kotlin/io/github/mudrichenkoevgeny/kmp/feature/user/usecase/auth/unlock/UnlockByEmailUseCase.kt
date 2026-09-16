package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.unlock.UnlockRepository

/** Unlocks an account using an email confirmation code. */
open class UnlockByEmailUseCase(
    private val unlockRepository: UnlockRepository
) {
    /**
     * Unlocks an account with an email code.
     *
     * @param email Account email address.
     * @param confirmationCode Verification code.
     * @return Unit result on success, or an error result.
     */
    open suspend fun execute(email: String, confirmationCode: String): AppResult<Unit> {
        return unlockRepository.unlockByEmail(email, confirmationCode)
    }
}
