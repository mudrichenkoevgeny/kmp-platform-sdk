package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.ManagementIdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.userlist.ManagementUserIdentifierListParams

/**
 * Administrative retrieval of user identity identifiers for a specific target user.
 *
 * @param managementIdentifierRepository Administrative identifier management API.
 */
class ManagementGetUserIdentifiersUseCase(
    private val managementIdentifierRepository: ManagementIdentifierRepository
) {
    /**
     * @param params Per-user identifier list query and filter parameters DTO.
     * @return Paginated result containing matching user identifier models, or a mapped failure.
     */
    suspend operator fun invoke(
        params: ManagementUserIdentifierListParams
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        return managementIdentifierRepository.getUserIdentifiers(params)
    }
}
