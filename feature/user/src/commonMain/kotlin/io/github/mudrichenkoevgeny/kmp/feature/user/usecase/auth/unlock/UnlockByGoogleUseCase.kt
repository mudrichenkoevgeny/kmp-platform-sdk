package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.flatMap
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.unlock.UnlockRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

/**
 * Google unlock flow: obtains a Google ID credential string via [GoogleAuthService],
 * then submits it to [UnlockRepository] to unlock the account.
 *
 * @param authService Platform Google auth helper.
 * @param unlockRepository Repository for unlock requests.
 */
open class UnlockByGoogleUseCase(
    private val authService: GoogleAuthService,
    private val unlockRepository: UnlockRepository
) {
    /**
     * Executes Google sign-in and sends the resulting token to unlock the account.
     *
     * @return Unit result on success, or an error result from the auth service or unlock repository.
     */
    open suspend fun execute(): AppResult<Unit> {
        return authService.signIn().flatMap { externalProviderToken ->
            unlockRepository.unlockByExternalAuthProvider(
                authProvider = UserAuthProvider.GOOGLE.serialName,
                externalProviderToken = externalProviderToken
            )
        }
    }
}
