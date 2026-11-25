package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.globallist.ManagementUserListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.ManagementUpdateUserRequest

/** Administrative user account management. */
public interface ManagementUserApi {

    /**
     * Creates a new user account administratively.
     *
     * @param request Account creation payload.
     * @return Detailed information of the created user, or a mapped failure.
     */
    public suspend fun createUser(request: CreateByEmailRequest): AppResult<UserPrivatePayload>

    /**
     * Returns a paginated and filtered list of user summaries based on search criteria.
     *
     * @param params Query and filter parameters DTO.
     * @return Paginated result containing user details payloads, or a mapped failure.
     */
    public suspend fun getUsers(
        params: ManagementUserListParams? = null
    ): AppResult<PagedResult<UserSummaryPayload>>

    /**
     * Retrieves full management-level details of a specific user.
     *
     * @param userId Unique account identifier.
     * @return Detailed information of the target user, or a mapped failure.
     */
    public suspend fun getUser(userId: UserId): AppResult<UserPrivatePayload>

    /**
     * Updates user details, status, or permissions for a specific user administratively.
     *
     * @param userId Unique account identifier to update.
     * @param request Patch payload containing fields to change.
     * @return Empty success indicator, or a mapped failure.
     */
    public suspend fun updateUser(
        userId: UserId,
        request: ManagementUpdateUserRequest
    ): AppResult<Unit>

    /**
     * Deletes a user account administratively.
     *
     * @param userId Unique account identifier to delete.
     * @return Empty success indicator, or a mapped failure.
     */
    public suspend fun deleteUser(userId: UserId): AppResult<Unit>
}
