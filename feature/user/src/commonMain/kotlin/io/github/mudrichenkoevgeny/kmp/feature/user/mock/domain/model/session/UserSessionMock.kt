package io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

@InternalApi
fun userSessionMock() = UserSession(
    userId = UserId.generate(),
    userRole = UserRole.USER,
    identifier = "user@example.com",
    identifierId = UserIdentifierId.generate(),
    identifierAuthProvider = UserAuthProvider.EMAIL,
    deviceInfo = ClientDeviceInfo(),
    userAgent = "MockUserAgent/1.0",
    ipAddress = "127.0.0.1",
    expiresAt = Clock.System.now().plus(30.days),
    lastAccessedAt = Clock.System.now(),
    lastReauthenticatedAt = Clock.System.now(),
    isSensitiveValuesMasked = false,
    createdAt = Clock.System.now(),
    updatedAt = null
)