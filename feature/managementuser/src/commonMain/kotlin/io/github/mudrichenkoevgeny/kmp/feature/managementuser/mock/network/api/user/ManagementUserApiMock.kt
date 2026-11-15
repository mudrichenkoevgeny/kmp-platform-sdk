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

/**
 * Mock implementation of [ManagementUserApi] for testing.
 */
@InternalApi
public class ManagementUserApiMock : ManagementUserApi {

    public var createUserResult: AppResult<UserPrivatePayload> = AppResult.Error(CommonError.Unknown())
    public var getUsersResult: AppResult<PagedResult<UserSummaryPayload>> = AppResult.Error(CommonError.Unknown())
    public var getUserResult: AppResult<UserPrivatePayload> = AppResult.Error(CommonError.Unknown())
    public var updateUserResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())
    public var deleteUserResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())

    public override suspend fun createUser(request: CreateByEmailRequest): AppResult<UserPrivatePayload> =
        createUserResult

    public override suspend fun getUsers(
        params: ManagementUserListParams?
    ): AppResult<PagedResult<UserSummaryPayload>> = getUsersResult

    public override suspend fun getUser(userId: UserId): AppResult<UserPrivatePayload> = getUserResult

    public override suspend fun updateUser(
        userId: UserId,
        request: ManagementUpdateUserRequest
    ): AppResult<Unit> = updateUserResult

    public override suspend fun deleteUser(userId: UserId): AppResult<Unit> = deleteUserResult
}
