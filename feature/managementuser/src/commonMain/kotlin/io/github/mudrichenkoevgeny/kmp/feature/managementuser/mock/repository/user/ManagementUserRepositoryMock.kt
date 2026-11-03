package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.domain.model.user.UpdateUserParams
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.globallist.ManagementUserListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.create.CreateByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserSummary

@InternalApi
class ManagementUserRepositoryMock : ManagementUserRepository {

    var createUserResultProvider: (CreateByEmailRequest) -> AppResult<UserPrivate> = {
        AppResult.Success(createTestUserPrivate())
    }

    var getUsersResultProvider: () -> AppResult<PagedResult<UserSummary>> = {
        AppResult.Success(createTestPagedResult(listOf(createTestUserSummary())))
    }

    var getUserResultProvider: (UserId) -> AppResult<UserPrivate> = {
        AppResult.Success(createTestUserPrivate())
    }

    var updateUserResultProvider: (UserId, UpdateUserParams) -> AppResult<Unit> = { _, _ ->
        AppResult.Success(Unit)
    }

    var deleteUserResultProvider: (UserId) -> AppResult<Unit> = { _ ->
        AppResult.Success(Unit)
    }

    var lastCreateRequest: CreateByEmailRequest? = null
    var lastUpdateUserId: UserId? = null
    var lastUpdateParams: UpdateUserParams? = null
    var lastDeleteUserId: UserId? = null
    var lastGetUserId: UserId? = null
    var lastUserListParams: ManagementUserListParams? = null

    override suspend fun createUser(request: CreateByEmailRequest): AppResult<UserPrivate> {
        lastCreateRequest = request
        return createUserResultProvider(request)
    }

    override suspend fun getUsers(
        params: ManagementUserListParams?
    ): AppResult<PagedResult<UserSummary>> {
        lastUserListParams = params
        return getUsersResultProvider()
    }

    override suspend fun getUser(userId: UserId): AppResult<UserPrivate> {
        lastGetUserId = userId
        return getUserResultProvider(userId)
    }

    override suspend fun updateUser(userId: UserId, params: UpdateUserParams): AppResult<Unit> {
        lastUpdateUserId = userId
        lastUpdateParams = params
        return updateUserResultProvider(userId, params)
    }

    override suspend fun deleteUser(userId: UserId): AppResult<Unit> {
        lastDeleteUserId = userId
        return deleteUserResultProvider(userId)
    }
}
