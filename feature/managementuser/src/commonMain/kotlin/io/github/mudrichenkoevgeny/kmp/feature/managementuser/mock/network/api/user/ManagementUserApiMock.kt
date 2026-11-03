package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.ManagementUserApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.globallist.ManagementUserListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.ManagementUpdateUserRequest

@InternalApi
class ManagementUserApiMock : ManagementUserApi {

    var createUserResult: AppResult<UserPrivatePayload> = AppResult.Error(CommonError.Unknown())
    var getUsersResult: AppResult<PagedResult<UserSummaryPayload>> = AppResult.Error(CommonError.Unknown())
    var getUserResult: AppResult<UserPrivatePayload> = AppResult.Error(CommonError.Unknown())
    var updateUserResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())
    var deleteUserResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())

    override suspend fun createUser(request: CreateByEmailRequest) = createUserResult

    override suspend fun getUsers(
        params: ManagementUserListParams?
    ) = getUsersResult

    override suspend fun getUser(userId: UserId) = getUserResult

    override suspend fun updateUser(userId: UserId, request: ManagementUpdateUserRequest) = updateUserResult

    override suspend fun deleteUser(userId: UserId) = deleteUserResult
}
