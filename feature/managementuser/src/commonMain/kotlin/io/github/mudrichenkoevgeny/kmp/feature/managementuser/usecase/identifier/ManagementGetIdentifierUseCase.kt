package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.ManagementIdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

/**
 * Administratively retrieves specific identifier details.
 *
 * @param managementIdentifierRepository Administrative identifier management API.
 */
class ManagementGetIdentifierUseCase(
    private val managementIdentifierRepository: ManagementIdentifierRepository
) {
    /**
     * @param identifierId Unique identifier record ID.
     * @return Detailed information of the target identifier, or a mapped failure.
     */
    suspend operator fun invoke(identifierId: String): AppResult<UserIdentifier> {
        return managementIdentifierRepository.getIdentifier(identifierId)
    }
}
