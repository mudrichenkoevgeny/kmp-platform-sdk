package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.domain.model.user.UpdateUserParams
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.globallist.ManagementUserListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest

/**
 * Administrative repository for user account management operations across all accounts.
 */
public interface ManagementUserRepository {

    /**
     * Creates a new user account administratively.
     *
     * @param request Account creation payload.
     * @return Detailed information of the created user domain model, or a mapped failure.
     */
    public suspend fun createUser(request: CreateByEmailRequest): AppResult<UserPrivate>

    /**
     * Returns a paginated and filtered list of user summaries based on search criteria.
     *
     * @param params Query and filter parameters DTO.
     * @return Paginated result containing user summary domain models, or a mapped failure.
     */
    public suspend fun getUsers(
        params: ManagementUserListParams? = null
    ): AppResult<PagedResult<UserSummary>>

    /**
     * Retrieves full management-level details of a specific user.
     *
     * @param userId Unique account identifier.
     * @return Detailed information of the target user domain model, or a mapped failure.
     */
    public suspend fun getUser(userId: UserId): AppResult<UserPrivate>

    /**
     * Updates user details, status, or permissions for a specific user administratively.
     *
     * @param userId Unique account identifier to update.
     * @param params Domain parameters containing fields to change.
     * @return Empty success indicator, or a mapped failure.
     */
    public suspend fun updateUser(
        userId: UserId,
        params: UpdateUserParams
    ): AppResult<Unit>

    /**
     * Deletes a user account administratively.
     *
     * @param userId Unique account identifier to delete.
     * @return Empty success indicator, or a mapped failure.
     */
    public suspend fun deleteUser(userId: UserId): AppResult<Unit>
}
