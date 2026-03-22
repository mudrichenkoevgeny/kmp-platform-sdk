package io.github.mudrichenkoevgeny.kmp.feature.user.mapper.user

import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.CurrentUser
import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.toUserIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiFields
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.user.CurrentUserResponse
import kotlin.time.Instant

/**
 * Maps the authenticated user snapshot from the API into [CurrentUser], converting epoch-ms fields to [Instant].
 *
 * @throws IllegalStateException when [CurrentUserResponse.role] or [CurrentUserResponse.accountStatus] is not a known wire value.
 */
fun CurrentUserResponse.toCurrentUser(): CurrentUser = CurrentUser(
    id = id.toUserIdOrThrow(),
    role = UserRole.fromValue(role) ?: throw IllegalStateException(
        "Unknown ${UserApiFields.ROLE}: $role"
    ),
    accountStatus = UserAccountStatus.fromValue(accountStatus)
        ?: throw IllegalStateException(
            "Unknown ${UserApiFields.ACCOUNT_STATUS}: $accountStatus"
        ),
    lastLoginAt = lastLoginAt?.let { lastLoginAt ->
        Instant.fromEpochMilliseconds(lastLoginAt)
    },
    lastActiveAt = lastActiveAt?.let { lastActiveAt ->
        Instant.fromEpochMilliseconds(lastActiveAt)
    },
    createdAt = Instant.fromEpochMilliseconds(createdAt),
    updatedAt = updatedAt?.let { updatedAt ->
        Instant.fromEpochMilliseconds(updatedAt)
    }
)