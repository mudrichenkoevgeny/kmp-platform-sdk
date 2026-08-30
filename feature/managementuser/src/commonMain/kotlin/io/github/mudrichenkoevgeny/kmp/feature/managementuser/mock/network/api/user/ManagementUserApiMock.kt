package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.ManagementUserApi
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

@InternalApi
class ManagementUserApiMock : ManagementUserApi {

    var createUserResult: AppResult<UserDetailsPayload> = AppResult.Error(CommonError.Unknown())
    var getUsersResult: AppResult<PagedResult<UserDetailsPayload>> = AppResult.Error(CommonError.Unknown())
    var getUserResult: AppResult<UserDetailsPayload> = AppResult.Error(CommonError.Unknown())
    var updateUserResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())
    var deleteUserResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())

    override suspend fun createUser(request: CreateByEmailRequest) = createUserResult

    override suspend fun getUsers(
        pageNumber: Int?, pageSize: Int?, sortBy: UserSortValues.UserSortBy?,
        sortOrder: SortOrder?, roles: List<UserRole>?, accountStatuses: List<UserAccountStatus>?,
        accountStatusesBeforeDeletion: List<UserAccountStatus>?, authorityLevelFrom: Int?,
        authorityLevelTo: Int?, isTotpEnabled: Boolean?, permissionCodes: List<PermissionCode>?
    ) = getUsersResult

    override suspend fun getUser(userId: UserId) = getUserResult

    override suspend fun updateUser(userId: UserId, request: UpdateUserRequest) = updateUserResult

    override suspend fun deleteUser(userId: UserId) = deleteUserResult
}