package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.ManagementIdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Administratively removes the password credential for the given user's identifier record.
 *
 * @param managementIdentifierRepository Administrative identifier management API.
 */
class ManagementDeleteIdentifierPasswordUseCase(
    private val managementIdentifierRepository: ManagementIdentifierRepository
) {
    /**
     * @param userId Unique identifier of the record owner.
     * @param identifierId Unique identifier record ID to remove password for.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend operator fun invoke(userId: UserId, identifierId: String): AppResult<Unit> {
        return managementIdentifierRepository.deleteIdentifierPassword(userId, identifierId)
    }
}
