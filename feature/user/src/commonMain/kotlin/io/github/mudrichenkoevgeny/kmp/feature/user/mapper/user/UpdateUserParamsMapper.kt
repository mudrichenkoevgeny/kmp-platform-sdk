package io.github.mudrichenkoevgeny.kmp.feature.user.mapper.user

import io.github.mudrichenkoevgeny.kmp.core.common.model.toPatchField
import io.github.mudrichenkoevgeny.kmp.feature.user.domain.model.user.UpdateUserParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.ManagementUpdateUserRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.user.SelfUpdateUserRequest

/**
 * Converts a domain-layer [UpdateUserParams] into a network-layer [SelfUpdateUserRequest].
 */
fun UpdateUserParams.toSelfUpdateUserRequest(): SelfUpdateUserRequest {
    return SelfUpdateUserRequest(
        nickname = nickname.toPatchField(),
        displayName = displayName.toPatchField(),
        avatarFileId = avatarFileId.toPatchField { it.value.toHexDashString() },
        birthdate = birthdate.toPatchField { it.toEpochMilliseconds() }
    )
}

/**
 * Converts a domain-layer [UpdateUserParams] into a network-layer [ManagementUpdateUserRequest].
 */
fun UpdateUserParams.toManagementUpdateUserRequest(): ManagementUpdateUserRequest {
    return ManagementUpdateUserRequest(
        accountStatus = accountStatus.toPatchField { it.value },
        authorityLevel = authorityLevel.toPatchField(),
        permissionCodes = permissions.toPatchField { set -> set.map { it.value }.toSet() },
        lockoutType = lockoutType.toPatchField { it.value },
        temporaryLockoutUntil = temporaryLockoutUntil.toPatchField { it.toEpochMilliseconds() },
        storageConfiguration = storageConfiguration.toPatchField(),
        nickname = nickname.toPatchField(),
        displayName = displayName.toPatchField(),
        avatarFileId = avatarFileId.toPatchField { it.value.toHexDashString() },
        birthdate = birthdate.toPatchField { it.toEpochMilliseconds() }
    )
}
