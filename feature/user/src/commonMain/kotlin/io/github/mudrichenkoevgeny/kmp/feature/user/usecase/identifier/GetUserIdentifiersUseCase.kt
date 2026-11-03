package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.list.SelfIdentifierListParams

/**
 * Retrieves a paginated list of user identifiers linked to the current account.
 *
 * @param identifierRepository Identifiers repository.
 */
open class GetUserIdentifiersUseCase(
    private val identifierRepository: IdentifierRepository
) {
    /**
     * @param params Query and filter parameters DTO.
     * @return Paginated result containing [UserIdentifierSummary] items on success, or a mapped failure.
     */
    open suspend operator fun invoke(
        params: SelfIdentifierListParams? = null
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        return identifierRepository.getUserIdentifiers(params)
    }
}
