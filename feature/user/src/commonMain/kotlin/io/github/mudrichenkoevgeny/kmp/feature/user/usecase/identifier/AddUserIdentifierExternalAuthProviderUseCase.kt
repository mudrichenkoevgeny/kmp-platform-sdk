package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

/**
 * Associates an external authentication provider identifier (e.g., OAuth) with the current account.
 *
 * @param identifierRepository Remote identifier management API.
 */
class AddUserIdentifierExternalAuthProviderUseCase(
    private val identifierRepository: IdentifierRepository
) {
    /**
     * @param authProvider External provider name.
     * @param token Provider-issued identity token.
     * @return New [UserIdentifier] on success, or a mapped failure.
     */
    suspend operator fun invoke(
        authProvider: String,
        token: String
    ): AppResult<UserIdentifier> {
        return identifierRepository.addUserIdentifierExternalAuthProvider(authProvider, token)
    }
}
