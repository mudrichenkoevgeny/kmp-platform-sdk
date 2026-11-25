package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate

/**
 * Retrieves details for a specific user identifier.
 *
 * @param identifierRepository Identifiers repository.
 */
public open class GetUserIdentifierUseCase(
    private val identifierRepository: IdentifierRepository
) {
    /**
     * @param userIdentifierId Identifier unique ID.
     * @return [UserIdentifierPrivate] on success, or a mapped failure.
     */
    public open suspend operator fun invoke(userIdentifierId: UserIdentifierId): AppResult<UserIdentifierPrivate> =
        identifierRepository.getUserIdentifier(
            userIdentifierId
        )
}
