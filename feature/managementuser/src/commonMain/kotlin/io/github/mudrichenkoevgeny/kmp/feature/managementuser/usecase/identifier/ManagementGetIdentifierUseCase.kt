package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.ManagementIdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate

/**
 * Administratively retrieves specific identifier details.
 *
 * @param managementIdentifierRepository Administrative identifier management API.
 */
public class ManagementGetIdentifierUseCase(
    private val managementIdentifierRepository: ManagementIdentifierRepository
) {
    /**
     * @param identifierId Unique identifier record ID.
     * @return Detailed information of the target identifier, or a mapped failure.
     */
    public suspend operator fun invoke(identifierId: UserIdentifierId): AppResult<UserIdentifierPrivate> =
        managementIdentifierRepository.getIdentifier(
            identifierId
        )
}
