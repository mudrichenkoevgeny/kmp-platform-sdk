package io.github.mudrichenkoevgeny.kmp.feature.user.mapper.session

import io.github.mudrichenkoevgeny.kmp.feature.user.model.session.UserSession
import io.github.mudrichenkoevgeny.kmp.feature.user.model.session.toUserSessionIdOrThrow
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.toUserIdentifierIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiFields
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.session.UserSessionResponse

/**
 * Maps a session row from the API into [UserSession].
 *
 * @throws IllegalStateException when [UserSessionResponse.identifierAuthProvider] is not a known wire value.
 */
fun UserSessionResponse.toUserSession(): UserSession = UserSession(
    id = id.toUserSessionIdOrThrow(),
    identifierId = identifierId.toUserIdentifierIdOrThrow(),
    identifierAuthProvider = UserAuthProvider.fromValue(identifierAuthProvider)
        ?: throw IllegalStateException(
            "Unknown ${UserApiFields.IDENTIFIER_AUTH_PROVIDER}: $identifierAuthProvider"
        ),
    expiresAt = expiresAt,
    clientType = clientType?.let { clientType -> UserClientType.fromValue(clientType) },
    userAgent = userAgent,
    ipAddress = ipAddress,
    deviceName = deviceName,
    createdAt = createdAt,
    lastAccessedAt = lastAccessedAt
)