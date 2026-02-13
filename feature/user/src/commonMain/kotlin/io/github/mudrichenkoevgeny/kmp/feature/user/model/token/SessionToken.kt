package io.github.mudrichenkoevgeny.kmp.feature.user.model.token

data class SessionToken(
    val accessToken: AccessToken,
    val refreshToken: RefreshToken,
    val expiresAt: Long,
    val tokenType: String
)