package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.globallist.ManagementGlobalIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.userlist.ManagementUserIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierSummaryPayload

/** Administrative user identifiers management. */
interface ManagementIdentifierApi {

    /**
     * Returns a paginated and filtered list of identifiers based on search criteria.
     *
     * @param params Administrative identifier list query and filter parameters DTO.
     * @return Paginated result containing matching identifier payloads, or a mapped failure.
     */
    suspend fun getIdentifiers(
        params: ManagementGlobalIdentifierListParams? = null
    ): AppResult<PagedResult<UserIdentifierSummaryPayload>>

    /**
     * Returns a paginated and filtered list of identifiers for a specific user.
     *
     * @param params Per-user identifier list query and filter parameters DTO.
     * @return Paginated result containing matching identifier payloads, or a mapped failure.
     */
    suspend fun getUserIdentifiers(
        params: ManagementUserIdentifierListParams
    ): AppResult<PagedResult<UserIdentifierSummaryPayload>>

    /**
     * Retrieves specific identifier details.
     *
     * @param identifierId Unique identifier record ID.
     * @return Detailed information of the target identifier, or a mapped failure.
     */
    suspend fun getIdentifier(identifierId: String): AppResult<UserIdentifierPrivatePayload>

    /**
     * Removes the identifier record for the given user.
     *
     * @param userId Unique identifier of the record owner.
     * @param identifierId Unique identifier record ID to delete.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun deleteIdentifier(userId: UserId, identifierId: String): AppResult<Unit>

    /**
     * Removes the password credential for the given user's identifier record.
     *
     * @param userId Unique identifier of the record owner.
     * @param identifierId Unique identifier record ID to remove password for.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun deleteIdentifierPassword(userId: UserId, identifierId: String): AppResult<Unit>
}
