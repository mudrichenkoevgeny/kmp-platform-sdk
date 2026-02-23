package io.github.mudrichenkoevgeny.kmp.feature.user.model.session

import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val id: UserSessionId,
    val identifierId: UserIdentifierId,
    val identifierAuthProvider: UserAuthProvider,
    val expiresAt: Long?,
    val clientType: UserClientType?,
    val userAgent: String?,
    val ipAddress: String?,
    val deviceName: String?,
    val createdAt: Long?,
    val lastAccessedAt: Long?
)