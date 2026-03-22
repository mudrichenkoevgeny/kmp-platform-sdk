package io.github.mudrichenkoevgeny.kmp.feature.user.model.token

/** Session credentials returned by auth endpoints: bearer pair, expiry, and token type metadata. */
data class SessionToken(
    val accessToken: AccessToken,
    val refreshToken: RefreshToken,
    val expiresAt: Long,
    val tokenType: String
)