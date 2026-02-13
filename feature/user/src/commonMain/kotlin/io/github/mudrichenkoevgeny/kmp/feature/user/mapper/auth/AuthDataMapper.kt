package io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth

import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.user.toCurrentUser
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.token.toSessionToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.AuthDataResponse

fun AuthDataResponse.toAuthData(): AuthData = AuthData(
    currentUser = currentUserResponse.toCurrentUser(),
    sessionToken = sessionTokenResponse.toSessionToken()
)