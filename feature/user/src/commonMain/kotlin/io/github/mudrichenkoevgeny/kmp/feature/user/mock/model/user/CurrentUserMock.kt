package io.github.mudrichenkoevgeny.kmp.feature.user.mock.model.user

import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.CurrentUser
import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserRole
import kotlin.time.Clock

/**
 * Builds a non-persistent [CurrentUser] with a fresh [UserId] and timestamps anchored to [Clock.System.now].
 *
 * @param role Wire role for the mock user (default [UserRole.USER]).
 * @param accountStatus Account state (default [UserAccountStatus.ACTIVE]).
 */
fun mockCurrentUser(
    role: UserRole = UserRole.USER,
    accountStatus: UserAccountStatus = UserAccountStatus.ACTIVE
): CurrentUser {
    val now = Clock.System.now()
    return CurrentUser(
        id = UserId.generate(),
        role = role,
        accountStatus = accountStatus,
        lastLoginAt = now,
        lastActiveAt = now,
        createdAt = now,
        updatedAt = null
    )
}