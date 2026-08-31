package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

/**
 * Removes an existing identifier from the current user profile.
 *
 * @param identifierRepository Remote identifier management API.
 */
class DeleteUserIdentifierUseCase(
    private val identifierRepository: IdentifierRepository
) {
    /**
     * @param identifierId Unique identifier id to delete.
     * @return Success unit, or a mapped failure.
     */
    suspend operator fun invoke(identifierId: UserIdentifierId): AppResult<Unit> {
        return identifierRepository.deleteUserIdentifier(identifierId)
    }
}
