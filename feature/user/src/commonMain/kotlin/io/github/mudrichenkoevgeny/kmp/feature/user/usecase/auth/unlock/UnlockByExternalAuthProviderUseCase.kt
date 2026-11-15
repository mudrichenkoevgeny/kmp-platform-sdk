package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.unlock.UnlockRepository

/** Unlocks an account using an external authentication provider token. */
public open class UnlockByExternalAuthProviderUseCase(
    private val unlockRepository: UnlockRepository
) {
    /**
     * Unlocks an account via external provider (Google / Apple).
     *
     * @param authProvider External provider identifier.
     * @param externalProviderToken OAuth token.
     * @return Unit result on success, or an error result.
     */
    public open suspend fun execute(
        authProvider: String,
        externalProviderToken: String
    ): AppResult<Unit> = unlockRepository.unlockByExternalAuthProvider(authProvider, externalProviderToken)
}
