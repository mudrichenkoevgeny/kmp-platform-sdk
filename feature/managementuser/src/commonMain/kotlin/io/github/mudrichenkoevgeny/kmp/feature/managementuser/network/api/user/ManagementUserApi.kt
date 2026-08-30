package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.permission.PermissionCode
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserDetailsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.UpdateUserRequest

/** Administrator and staff endpoints for full user management. */
interface ManagementUserApi {

    /**
     * Creates a new user account.
     *
     * @param request Payload details for creating an account via email.
     * @return Detailed information of the newly created user, or a mapped failure.
     */
    suspend fun createUser(request: CreateByEmailRequest): AppResult<UserDetailsPayload>

    /**
     * Returns a paginated and filtered list of users based on search criteria.
     *
     * @param pageNumber One-based page index.
     * @param pageSize Maximum items returned per page.
     * @param sortBy Field to sort by (last_login_at, last_active_at, scheduled_permanent_deletion_at, created_at, updated_at).
     * @param sortOrder Sorting direction (ASC, DESC).
     * @param roles Filters users by their assigned role types.
     * @param accountStatuses Filters users by current account statuses.
     * @param accountStatusesBeforeDeletion Filters users by status held prior to scheduled deletion.
     * @param authorityLevelFrom Lower bound filter for authority level.
     * @param authorityLevelTo Upper bound filter for authority level.
     * @param isTotpEnabled Filters users by whether TOTP second-factor authentication is active.
     * @param permissionCodes Filters users possessing specific permission codes.
     * @return Paginated result containing user details payloads, or a mapped failure.
     */
    suspend fun getUsers(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        sortBy: UserSortValues.UserSortBy? = null,
        sortOrder: SortOrder? = null,
        roles: List<UserRole>? = null,
        accountStatuses: List<UserAccountStatus>? = null,
        accountStatusesBeforeDeletion: List<UserAccountStatus>? = null,
        authorityLevelFrom: Int? = null,
        authorityLevelTo: Int? = null,
        isTotpEnabled: Boolean? = null,
        permissionCodes: List<PermissionCode>? = null
    ): AppResult<PagedResult<UserDetailsPayload>>

    /**
     * Retrieves full management-level details of a specific user.
     *
     * @param userId Unique account identifier.
     * @return Detailed profile information of the target user, or a mapped failure.
     */
    suspend fun getUser(userId: UserId): AppResult<UserDetailsPayload>

    /**
     * Updates profile details, status, or permissions for a specific user.
     *
     * @param userId Unique account identifier to update.
     * @param request Patch payload containing fields to change.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun updateUser(userId: UserId, request: UpdateUserRequest): AppResult<Unit>

    /**
     * Completely deletes a specified user account.
     *
     * @param userId Unique account identifier to remove.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun deleteUser(userId: UserId): AppResult<Unit>
}