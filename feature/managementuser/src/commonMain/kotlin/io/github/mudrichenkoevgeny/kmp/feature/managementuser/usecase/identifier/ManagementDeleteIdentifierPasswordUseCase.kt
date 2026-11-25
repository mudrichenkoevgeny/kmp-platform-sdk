package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.ManagementIdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserIdentifierId

/**
 * Administratively removes the password credential for the given user's identifier record.
 *
 * @param managementIdentifierRepository Administrative identifier management API.
 */
public class ManagementDeleteIdentifierPasswordUseCase(
    private val managementIdentifierRepository: ManagementIdentifierRepository
) {
    /**
     * @param userId Unique identifier of the record owner.
     * @param identifierId Unique identifier record ID to remove password for.
     * @return Empty success indicator, or a mapped failure.
     */
    public suspend operator fun invoke(
        userId: UserId,
        identifierId: UserIdentifierId
    ): AppResult<Unit> = managementIdentifierRepository.deleteIdentifierPassword(userId, identifierId)
}
