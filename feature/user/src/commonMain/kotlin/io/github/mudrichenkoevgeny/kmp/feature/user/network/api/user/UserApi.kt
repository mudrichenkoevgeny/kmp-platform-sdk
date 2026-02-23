package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.user.CurrentUserResponse

interface UserApi {
    suspend fun getUser(): AppResult<CurrentUserResponse>
    suspend fun deleteUser(): AppResult<Unit>
}