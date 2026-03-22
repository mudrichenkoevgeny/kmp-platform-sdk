package io.github.mudrichenkoevgeny.kmp.feature.user.mapper.user

import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.PublicUser
import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.toUserIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiFields
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.user.PublicUserResponse

/**
 * Maps a public user payload from the HTTP API into [PublicUser].
 *
 * @throws IllegalStateException when [PublicUserResponse.role] or [PublicUserResponse.accountStatus] is not a known wire value.
 */
fun PublicUserResponse.toPublicUser(): PublicUser = PublicUser(
    id = id.toUserIdOrThrow(),
    role = UserRole.fromValue(role) ?: throw IllegalStateException(
        "Unknown ${UserApiFields.ROLE}: $role"
    ),
    accountStatus = UserAccountStatus.fromValue(accountStatus)
        ?: throw IllegalStateException(
            "Unknown ${UserApiFields.ACCOUNT_STATUS}: $accountStatus"
        )
)