package io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier

import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class UserIdentifier(
    val id: UserIdentifierId,
    val userId: UserId,
    val userAuthProvider: UserAuthProvider,
    val identifier: String,
    val createdAt: Instant,
    val updatedAt: Instant?
)