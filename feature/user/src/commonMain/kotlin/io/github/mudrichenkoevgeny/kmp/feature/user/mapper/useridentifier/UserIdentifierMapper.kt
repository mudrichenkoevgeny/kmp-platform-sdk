package io.github.mudrichenkoevgeny.kmp.feature.user.mapper.useridentifier

import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.toUserIdOrThrow
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.toUserIdentifierIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiFields
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.useridentifier.UserIdentifierResponse
import kotlin.time.Instant

fun UserIdentifierResponse.toUserIdentifier(): UserIdentifier = UserIdentifier(
    id = id.toUserIdentifierIdOrThrow(),
    userId = userId.toUserIdOrThrow(),
    userAuthProvider = UserAuthProvider.fromValue(userAuthProvider)
        ?: throw IllegalStateException(
            "Unknown ${UserApiFields.USER_AUTH_PROVIDER}: $userAuthProvider"
        ),
    identifier = identifier,
    createdAt = Instant.fromEpochMilliseconds(createdAt),
    updatedAt = updatedAt?.let { updatedAt ->
        Instant.fromEpochMilliseconds(updatedAt)
    }
)