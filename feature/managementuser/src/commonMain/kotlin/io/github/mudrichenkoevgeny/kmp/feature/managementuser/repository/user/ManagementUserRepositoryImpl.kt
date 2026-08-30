package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.ManagementUserApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.permission.PermissionCode
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.UpdateUserRequest

/**
 * Implements [ManagementUserRepository] by forwarding all administrative requests to [ManagementUserApi].
 *
 * @param managementUserApi Administrative HTTP endpoints for system-wide user profile configurations.
 */
class ManagementUserRepositoryImpl(
    private val managementUserApi: ManagementUserApi
) : ManagementUserRepository {

    override suspend fun createUser(request: CreateByEmailRequest): AppResult<UserDetails> {
        return managementUserApi.createUser(request).mapSuccess { payload ->
            payload.toUserDetails()
        }
    }

    override suspend fun getUsers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserSortBy?,
        sortOrder: SortOrder?,
        roles: List<UserRole>?,
        accountStatuses: List<UserAccountStatus>?,
        accountStatusesBeforeDeletion: List<UserAccountStatus>?,
        authorityLevelFrom: Int?,
        authorityLevelTo: Int?,
        isTotpEnabled: Boolean?,
        permissionCodes: List<PermissionCode>?
    ): AppResult<PagedResult<UserDetails>> {
        return managementUserApi.getUsers(
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            sortOrder = sortOrder,
            roles = roles,
            accountStatuses = accountStatuses,
            accountStatusesBeforeDeletion = accountStatusesBeforeDeletion,
            authorityLevelFrom = authorityLevelFrom,
            authorityLevelTo = authorityLevelTo,
            isTotpEnabled = isTotpEnabled,
            permissionCodes = permissionCodes
        ).mapSuccess { pagedPayload ->
            pagedPayload.mapItems { payload ->
                payload.toUserDetails()
            }
        }
    }

    override suspend fun getUser(userId: UserId): AppResult<UserDetails> {
        return managementUserApi.getUser(userId).mapSuccess { payload ->
            payload.toUserDetails()
        }
    }

    override suspend fun updateUser(userId: UserId, request: UpdateUserRequest): AppResult<Unit> {
        return managementUserApi.updateUser(userId, request)
    }

    override suspend fun deleteUser(userId: UserId): AppResult<Unit> {
        return managementUserApi.deleteUser(userId)
    }
}