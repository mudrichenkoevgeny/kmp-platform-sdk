package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.UserApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserDetailsPayload

@InternalApi
class UserApiMock : UserApi {
    var userResult: AppResult<UserDetailsPayload> = AppResult.Error(CommonError.Unknown())
    var scheduleDeletionResult: AppResult<UserDetailsPayload> = AppResult.Error(CommonError.Unknown())
    var restoreUserResult: AppResult<UserDetailsPayload> = AppResult.Error(CommonError.Unknown())

    override suspend fun getUser(): AppResult<UserDetailsPayload> = userResult
    override suspend fun scheduleUserDeletion(): AppResult<UserDetailsPayload> = scheduleDeletionResult
    override suspend fun restoreUser(): AppResult<UserDetailsPayload> = restoreUserResult
}