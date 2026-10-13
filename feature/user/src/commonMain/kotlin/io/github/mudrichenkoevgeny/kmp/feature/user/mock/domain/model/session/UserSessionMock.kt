package io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.client.clientDeviceInfoMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

@InternalApi
fun userSessionSummaryMock(
    id: UserSessionId = UserSessionId.generate(),
    userId: UserId = UserId.generate(),
    identifierDisplayName: String = "user@example.com",
    identifierAuthProvider: UserAuthProvider = UserAuthProvider.EMAIL
) = UserSessionSummary(
    id = id,
    userId = userId,
    clientDeviceInfo = clientDeviceInfoMock(),
    identifierDisplayName = identifierDisplayName,
    identifierAuthProvider = identifierAuthProvider,
    lastAccessedAt = Clock.System.now(),
    expiresAt = Clock.System.now().plus(30.days)
)

@InternalApi
fun userSessionPrivateMock(
    id: UserSessionId = UserSessionId.generate(),
    userId: UserId = UserId.generate(),
    userRole: UserRole = UserRole.USER,
    identifier: String = "user@example.com",
    identifierDisplayName: String = "user@example.com",
    identifierId: UserIdentifierId = UserIdentifierId.generate(),
    identifierAuthProvider: UserAuthProvider = UserAuthProvider.EMAIL
) = UserSessionPrivate(
    id = id,
    userId = userId,
    userRole = userRole,
    identifier = identifier,
    identifierId = identifierId,
    identifierDisplayName = identifierDisplayName,
    identifierAuthProvider = identifierAuthProvider,
    clientDeviceInfo = clientDeviceInfoMock(),
    userAgent = "MockUserAgent/1.0",
    ipAddress = "127.0.0.1",
    expiresAt = Clock.System.now().plus(30.days),
    lastAccessedAt = Clock.System.now(),
    lastReauthenticatedAt = Clock.System.now(),
    isSensitiveValuesMasked = false,
    createdAt = Clock.System.now(),
    updatedAt = null
)

@InternalApi
fun userSessionMock() = userSessionSummaryMock()