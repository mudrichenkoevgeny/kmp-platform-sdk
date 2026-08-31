package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.permission.PermissionCode
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.UpdateUserRequest

@InternalApi
open class ManagementUserRepositoryMock : ManagementUserRepository {

    var createUserResultProvider: (CreateByEmailRequest) -> AppResult<UserDetails> = {
        AppResult.Success(userDetailsMock())
    }

    var getUsersResultProvider: () -> AppResult<PagedResult<UserDetails>> = {
        AppResult.Success(pagedResultMock(listOf(userDetailsMock())))
    }

    var getUserResultProvider: (UserId) -> AppResult<UserDetails> = {
        AppResult.Success(userDetailsMock())
    }

    var updateUserResultProvider: (UserId, UpdateUserRequest) -> AppResult<Unit> = { _, _ ->
        AppResult.Success(Unit)
    }

    var deleteUserResultProvider: (UserId) -> AppResult<Unit> = { _ ->
        AppResult.Success(Unit)
    }

    var lastCreateRequest: CreateByEmailRequest? = null
    var lastUpdateUserId: UserId? = null
    var lastUpdateRequest: UpdateUserRequest? = null
    var lastDeleteUserId: UserId? = null
    var lastGetUserId: UserId? = null

    override suspend fun createUser(request: CreateByEmailRequest): AppResult<UserDetails> {
        lastCreateRequest = request
        return createUserResultProvider(request)
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
    ): AppResult<PagedResult<UserDetails>> = getUsersResultProvider()

    override suspend fun getUser(userId: UserId): AppResult<UserDetails> {
        lastGetUserId = userId
        return getUserResultProvider(userId)
    }

    override suspend fun updateUser(userId: UserId, request: UpdateUserRequest): AppResult<Unit> {
        lastUpdateUserId = userId
        lastUpdateRequest = request
        return updateUserResultProvider(userId, request)
    }

    override suspend fun deleteUser(userId: UserId): AppResult<Unit> {
        lastDeleteUserId = userId
        return deleteUserResultProvider(userId)
    }
}
