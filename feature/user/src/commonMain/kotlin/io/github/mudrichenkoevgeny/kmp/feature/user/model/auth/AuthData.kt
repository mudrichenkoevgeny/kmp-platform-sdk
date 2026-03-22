package io.github.mudrichenkoevgeny.kmp.feature.user.model.auth

import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.SessionToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.CurrentUser

/** Combined result of a successful auth handshake: who signed in plus fresh session tokens. */
data class AuthData(
    val currentUser: CurrentUser,
    val sessionToken: SessionToken
)