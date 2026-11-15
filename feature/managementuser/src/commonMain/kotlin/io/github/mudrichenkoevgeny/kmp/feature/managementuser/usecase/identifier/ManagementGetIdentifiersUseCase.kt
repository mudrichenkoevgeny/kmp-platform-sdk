package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.ManagementIdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.globallist.ManagementGlobalIdentifierListParams

/**
 * Administrative retrieval of user identity identifiers based on filters.
 *
 * @param managementIdentifierRepository Administrative identifier management API.
 */
public class ManagementGetIdentifiersUseCase(
    private val managementIdentifierRepository: ManagementIdentifierRepository
) {
    /**
     * @param params Administrative identifier list query and filter parameters DTO.
     * @return Paginated result containing matching user identifier models, or a mapped failure.
     */
    public suspend operator fun invoke(
        params: ManagementGlobalIdentifierListParams? = null
    ): AppResult<PagedResult<UserIdentifierSummary>> = managementIdentifierRepository.getIdentifiers(params)
}
