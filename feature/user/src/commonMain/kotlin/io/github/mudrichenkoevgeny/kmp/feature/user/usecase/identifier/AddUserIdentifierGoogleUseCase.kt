package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.flatMap
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

/**
 * Google Sign-In flow for associating a Google identity record with the current account.
 *
 * @param authService Platform Google auth helper that returns a credential string.
 * @param identifierRepository Remote identifier management API.
 */
class AddUserIdentifierGoogleUseCase(
    private val authService: GoogleAuthService,
    private val identifierRepository: IdentifierRepository
) {
    /**
     * Executes interactive Google sign-in and links the returned provider token with the account.
     *
     * @return [UserIdentifier] on success, or a mapped failure.
     */
    suspend fun execute(): AppResult<UserIdentifier> {
        return authService.signIn()
            .flatMap { externalProviderToken ->
                identifierRepository.addUserIdentifierExternalAuthProvider(
                    UserAuthProvider.GOOGLE.serialName,
                    externalProviderToken
                )
            }
    }
}
