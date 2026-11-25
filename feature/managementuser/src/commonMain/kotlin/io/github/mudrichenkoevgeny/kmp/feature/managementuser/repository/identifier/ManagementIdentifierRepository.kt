package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.globallist.ManagementGlobalIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.userlist.ManagementUserIdentifierListParams

/**
 * Administrative repository for managing user identity identifiers across all accounts.
 */
public interface ManagementIdentifierRepository {

    /**
     * Returns a paginated and filtered list of identifiers based on search criteria.
     *
     * @param params Administrative identifier list query and filter parameters DTO.
     * @return Paginated result containing matching user identifier models, or a mapped failure.
     */
    public suspend fun getIdentifiers(
        params: ManagementGlobalIdentifierListParams? = null
    ): AppResult<PagedResult<UserIdentifierSummary>>

    /**
     * Returns a paginated and filtered list of identifiers for a specific target user.
     *
     * @param params Per-user identifier list query and filter parameters DTO.
     * @return Paginated result containing matching user identifier models, or a mapped failure.
     */
    public suspend fun getUserIdentifiers(
        params: ManagementUserIdentifierListParams
    ): AppResult<PagedResult<UserIdentifierSummary>>

    /**
     * Retrieves specific identifier details.
     */
    public suspend fun getIdentifier(identifierId: UserIdentifierId): AppResult<UserIdentifierPrivate>

    /**
     * Removes the identifier record for the given user.
     */
    public suspend fun deleteIdentifier(
        userId: UserId,
        identifierId: UserIdentifierId
    ): AppResult<Unit>

    /**
     * Removes the password credential for the given user's identifier record.
     */
    public suspend fun deleteIdentifierPassword(
        userId: UserId,
        identifierId: UserIdentifierId
    ): AppResult<Unit>
}
