package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.list.SelfIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.EmailPasswordChangeRequest

/** Manage user identifiers (email, phone, external providers) and related confirmations. */
interface SelfManagementIdentifiersApi {
    /**
     * Retrieves specific identifier details by its unique id.
     *
     * @param userIdentifierId Unique identifier payload id.
     * @return Detailed identifier info or a mapped failure.
     */
    suspend fun getUserIdentifier(userIdentifierId: UserIdentifierId): AppResult<UserIdentifierPrivatePayload>

    /**
     * Returns a paginated and filtered list of identifiers linked to the current authenticated management account.
     *
     * @param params Query and filter parameters DTO.
     * @return Paginated result containing matching user identifier payloads, or a mapped failure.
     */
    suspend fun getUserIdentifiers(
        params: SelfIdentifierListParams? = null
    ): AppResult<PagedResult<UserIdentifierSummaryPayload>>

    /**
     * Updates the management account password using current credentials.
     *
     * @param userIdentifierId Target identifier ID.
     * @param request Current and new password payload from the shared contract.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun emailChangePassword(
        userIdentifierId: UserIdentifierId,
        request: EmailPasswordChangeRequest
    ): AppResult<Unit>
}
