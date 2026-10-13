package io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.user.OpenUserApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserPrivatePayload

@InternalApi
class OpenUserApiMock : OpenUserApi {
    var getUserResult: AppResult<UserPrivatePayload> = AppResult.Error(CommonError.Unknown())
    var scheduleUserDeletionResult: AppResult<UserPrivatePayload> = AppResult.Error(CommonError.Unknown())
    var restoreUserResult: AppResult<UserPrivatePayload> = AppResult.Error(CommonError.Unknown())

    override suspend fun getUser(): AppResult<UserPrivatePayload> = getUserResult
    override suspend fun scheduleUserDeletion(): AppResult<UserPrivatePayload> = scheduleUserDeletionResult
    override suspend fun restoreUser(): AppResult<UserPrivatePayload> = restoreUserResult
}