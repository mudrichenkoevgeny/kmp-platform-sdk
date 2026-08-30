package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.client.ClientDeviceInfoPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload
import kotlin.uuid.Uuid

@InternalApi
fun userSessionPayloadMock(
    id: String = Uuid.random().toHexDashString(),
    userId: String = Uuid.random().toHexDashString(),
    userRole: String = "admin",
    identifier: String = "user@example.com",
    identifierId: String = Uuid.random().toHexDashString(),
    identifierAuthProvider: String = "email",
    expiresAt: Long = 1717244400000L,
    lastAccessedAt: Long = 1717240800000L,
    lastReauthenticatedAt: Long = 1717240800000L,
    isSensitiveValuesMasked: Boolean = false,
    createdAt: Long = 1717200000000L
): UserSessionPayload = UserSessionPayload(
    id = id,
    userId = userId,
    userRole = userRole,
    identifier = identifier,
    identifierId = identifierId,
    identifierAuthProvider = identifierAuthProvider,
    clientDeviceInfo = ClientDeviceInfoPayload(),
    userAgent = "Mozilla/5.0",
    ipAddress = "127.0.0.1",
    expiresAt = expiresAt,
    lastAccessedAt = lastAccessedAt,
    lastReauthenticatedAt = lastReauthenticatedAt,
    isSensitiveValuesMasked = isSensitiveValuesMasked,
    createdAt = createdAt,
    updatedAt = createdAt
)