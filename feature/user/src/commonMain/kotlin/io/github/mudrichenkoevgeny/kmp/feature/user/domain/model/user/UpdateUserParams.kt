package io.github.mudrichenkoevgeny.kmp.feature.user.domain.model.user

import io.github.mudrichenkoevgeny.kmp.core.common.model.UpdateField
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.permission.PermissionCode
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.FileId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserStorageConfigurationPayload
import kotlin.time.Instant

/**
 * Domain-layer parameter holder for updating a target user's profile and administrative settings.
 *
 * Uses [UpdateField] for each property to distinguish between omitting an update ([UpdateField.Ignore]),
 * setting a new value ([UpdateField.Set]), or explicitly clearing a field ([UpdateField.Set] with `null`).
 *
 * @property accountStatus target user account status update ([UpdateField])
 * @property accountStatusOnRestore status assigned when user restores account from deletion ([UpdateField])
 * @property authorityLevel target numerical authority rank update ([UpdateField])
 * @property permissions target set of explicit permission codes update ([UpdateField])
 * @property isTotpEnabled TOTP two-factor authentication enablement status update ([UpdateField])
 * @property scheduledPermanentDeletionAt timestamp for the final removal of the account update ([UpdateField])
 * @property lockoutType target account lockout type update ([UpdateField])
 * @property temporaryLockoutUntil target temporary lockout expiration timestamp update ([UpdateField])
 * @property storageConfiguration target user storage quota and limit configuration payload update ([UpdateField])
 * @property nickname target handle nickname update ([UpdateField])
 * @property displayName target profile display name update ([UpdateField])
 * @property avatarFileId target profile avatar image file identifier update ([UpdateField])
 * @property birthdate target user birthdate timestamp update ([UpdateField])
 */
public data class UpdateUserParams(
    public val accountStatus: UpdateField<UserAccountStatus> = UpdateField.Ignore,
    public val accountStatusOnRestore: UpdateField<UserAccountStatus> = UpdateField.Ignore,
    public val authorityLevel: UpdateField<Int> = UpdateField.Ignore,
    public val permissions: UpdateField<Set<PermissionCode>> = UpdateField.Ignore,
    public val isTotpEnabled: UpdateField<Boolean> = UpdateField.Ignore,
    public val scheduledPermanentDeletionAt: UpdateField<Instant> = UpdateField.Ignore,
    public val lockoutType: UpdateField<AccountLockoutType> = UpdateField.Ignore,
    public val temporaryLockoutUntil: UpdateField<Instant> = UpdateField.Ignore,
    public val storageConfiguration: UpdateField<UserStorageConfigurationPayload> = UpdateField.Ignore,
    public val nickname: UpdateField<String> = UpdateField.Ignore,
    public val displayName: UpdateField<String> = UpdateField.Ignore,
    public val avatarFileId: UpdateField<FileId> = UpdateField.Ignore,
    public val birthdate: UpdateField<Instant> = UpdateField.Ignore
)
