package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.SessionToken

interface RefreshTokenRepository {
    suspend fun refreshToken(refreshToken: String): AppResult<SessionToken>
}