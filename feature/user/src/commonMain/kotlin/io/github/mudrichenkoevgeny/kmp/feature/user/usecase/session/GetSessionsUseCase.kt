package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession

/**
 * Returns a paginated and filtered list of active sessions for the current authenticated account.
 *
 * @param sessionRepository Remote session management API.
 */
class GetSessionsUseCase(
    private val sessionRepository: SessionRepository
) {
    /**
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by.
     * @param sortOrder Sorting direction.
     * @param identifiers Filters by server-defined free-text identifier values.
     * @param identifierIds Filters by unique credential record IDs.
     * @param userAuthProviders Filters by authentication provider types.
     * @param clientTypes Filters by client category types.
     * @param userAgents Filters by server-defined user agent substrings.
     * @param ipAddresses Filters by server-defined IP address substrings.
     * @param languages Filters by server-defined language tags.
     * @param deviceIds Filters by opaque unique device IDs.
     * @param deviceNames Filters by server-defined device name substrings.
     * @param appVersions Filters by application version strings.
     * @param operationSystemVersions Filters by server-defined operating system version substrings.
     * @return Paginated result containing matching active session models, or a mapped failure.
     */
    suspend operator fun invoke(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: UserSortValues.UserSessionSortBy? = null,
        sortOrder: SortOrder? = null,
        identifiers: List<String>? = null,
        identifierIds: List<String>? = null,
        userAuthProviders: List<UserAuthProvider>? = null,
        clientTypes: List<ClientType>? = null,
        userAgents: List<String>? = null,
        ipAddresses: List<String>? = null,
        languages: List<String>? = null,
        deviceIds: List<String>? = null,
        deviceNames: List<String>? = null,
        appVersions: List<String>? = null,
        operationSystemVersions: List<String>? = null
    ): AppResult<PagedResult<UserSession>> {
        return sessionRepository.getSessions(
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            sortOrder = sortOrder,
            identifiers = identifiers,
            identifierIds = identifierIds,
            userAuthProviders = userAuthProviders,
            clientTypes = clientTypes,
            userAgents = userAgents,
            ipAddresses = ipAddresses,
            languages = languages,
            deviceIds = deviceIds,
            deviceNames = deviceNames,
            appVersions = appVersions,
            operationSystemVersions = operationSystemVersions
        )
    }
}
