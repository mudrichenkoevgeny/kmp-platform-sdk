package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.security.password

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.PasswordChangeRequest

interface SecurityPasswordApi {
    suspend fun changePassword(request: PasswordChangeRequest): AppResult<UserIdentifier>
}