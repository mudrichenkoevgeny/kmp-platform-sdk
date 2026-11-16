package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceFilterPresentationStyle
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterChoiceOption
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.NumberListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.option.ListingOptionsConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortDefinition
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.storage.localization.LocalStorageStrings
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortBy
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole

/**
 * Creates the listing options configuration for user sorting and filtering.
 *
 * @return [ListingOptionsConfig] populated with available user sort definitions and filter controls.
 */
@Composable
public fun getGlobalUserListingOptionsConfig(): ListingOptionsConfig {
    val roleOptions = createRoleOptions()
    val accountStatusOptions = createAccountStatusOptions()
    val lockoutTypeOptions = createLockoutTypeOptions()

    return ListingOptionsConfig(
        sortOptions = createUserSortDefinitions(),
        filters = createUserFilterDefinitions(roleOptions, accountStatusOptions, lockoutTypeOptions)
    )
}

@Composable
private fun createRoleOptions(): List<ListingFilterChoiceOption> {
    val commonUi = LocalCommonStrings.current.ui
    return listOf(
        ListingFilterChoiceOption(UserRole.STAFF.value, commonUi.staff),
        ListingFilterChoiceOption(UserRole.ADMIN.value, commonUi.admin)
    )
}

@Composable
private fun createAccountStatusOptions(): List<ListingFilterChoiceOption> {
    val commonUi = LocalCommonStrings.current.ui
    return listOf(
        ListingFilterChoiceOption(UserAccountStatus.ACTIVE.value, commonUi.active),
        ListingFilterChoiceOption(UserAccountStatus.READ_ONLY.value, commonUi.readOnly),
        ListingFilterChoiceOption(UserAccountStatus.BANNED.value, commonUi.banned),
        ListingFilterChoiceOption(UserAccountStatus.SECURITY_HOLD.value, commonUi.securityHold),
        ListingFilterChoiceOption(UserAccountStatus.PENDING_DELETION.value, commonUi.pendingDeletion)
    )
}

@Composable
private fun createLockoutTypeOptions(): List<ListingFilterChoiceOption> {
    val commonUi = LocalCommonStrings.current.ui
    return listOf(
        ListingFilterChoiceOption(AccountLockoutType.NONE.value, commonUi.lockoutNone),
        ListingFilterChoiceOption(AccountLockoutType.INDEFINITE.value, commonUi.lockoutIndefinite),
        ListingFilterChoiceOption(AccountLockoutType.TEMPORARY.value, commonUi.lockoutTemporary)
    )
}

@Composable
private fun createUserSortDefinitions(): List<ListingSortDefinition> {
    val commonUi = LocalCommonStrings.current.ui
    val userUi = LocalUserStrings.current.ui
    return listOf(
        ListingSortDefinition(
            id = UserSortBy.AUTHORITY_LEVEL.value,
            title = userUi.authorityLevel
        ),
        ListingSortDefinition(
            id = UserSortBy.LAST_LOGIN_AT.value,
            title = commonUi.lastLoginAt
        ),
        ListingSortDefinition(
            id = UserSortBy.LAST_ACTIVE_AT.value,
            title = commonUi.lastActiveAt
        ),
        ListingSortDefinition(
            id = UserSortBy.SCHEDULED_PERMANENT_DELETION_AT.value,
            title = commonUi.scheduledPermanentDeletionAt
        ),
        ListingSortDefinition(
            id = UserSortBy.TEMPORARY_LOCKOUT_UNTIL.value,
            title = commonUi.lockoutUntil
        ),
        ListingSortDefinition(
            id = UserSortBy.NICKNAME.value,
            title = userUi.userLabel
        ),
        ListingSortDefinition(
            id = UserSortBy.DISPLAY_NAME.value,
            title = userUi.user
        ),
        ListingSortDefinition(
            id = UserSortBy.BIRTHDATE.value,
            title = userUi.birthdate
        ),
        ListingSortDefinition(
            id = UserSortBy.CREATED_AT.value,
            title = commonUi.createdAt
        ),
        ListingSortDefinition(
            id = UserSortBy.UPDATED_AT.value,
            title = commonUi.updatedAt
        )
    )
}

@Composable
private fun createUserFilterDefinitions(
    roleOptions: List<ListingFilterChoiceOption>,
    accountStatusOptions: List<ListingFilterChoiceOption>,
    lockoutTypeOptions: List<ListingFilterChoiceOption>
): List<ListingFilterDefinition> = createIdentityFilterDefinitions() +
    createRoleAndStatusFilterDefinitions(roleOptions, accountStatusOptions) +
    createAuthorityAndPermissionFilterDefinitions() +
    createLockoutFilterDefinitions(lockoutTypeOptions) +
    createTimestampFilterDefinitions() +
    createStorageFilterDefinitions() +
    createPersonalFilterDefinitions()

@Composable
private fun createIdentityFilterDefinitions(): List<ListingFilterDefinition> {
    val commonUi = LocalCommonStrings.current.ui
    val userUi = LocalUserStrings.current.ui
    return listOf(
        TextListingFilterDefinition(
            id = UserFilterValues.USER_ID,
            title = commonUi.userId,
            placeholder = commonUi.searchPlaceholder
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.NICKNAME,
            title = userUi.userLabel,
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_NICKNAME_SET,
            title = "${userUi.userLabel} (${commonUi.isSet})"
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.DISPLAY_NAME,
            title = userUi.user,
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_DISPLAY_NAME_SET,
            title = "${userUi.user} (${commonUi.isSet})"
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_AVATAR_SET,
            title = "${userUi.avatar} (${commonUi.isSet})"
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.SEARCH_QUERY,
            title = commonUi.searchPlaceholder,
            placeholder = commonUi.searchPlaceholder
        )
    )
}

@Composable
private fun createRoleAndStatusFilterDefinitions(
    roleOptions: List<ListingFilterChoiceOption>,
    accountStatusOptions: List<ListingFilterChoiceOption>
): List<ListingFilterDefinition> {
    val commonUi = LocalCommonStrings.current.ui
    val userUi = LocalUserStrings.current.ui
    return listOf(
        ChoiceListingFilterDefinition(
            id = UserFilterValues.ROLE,
            title = commonUi.role,
            options = roleOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = UserFilterValues.EXCLUDE_ROLE,
            title = "${commonUi.role} (${commonUi.exclude})",
            options = roleOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = UserFilterValues.ACCOUNT_STATUS,
            title = commonUi.status,
            options = accountStatusOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = UserFilterValues.EXCLUDE_ACCOUNT_STATUS,
            title = "${commonUi.status} (${commonUi.exclude})",
            options = accountStatusOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = UserFilterValues.ACCOUNT_STATUS_ON_RESTORE,
            title = userUi.accountStatusActive,
            options = accountStatusOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = UserFilterValues.EXCLUDE_ACCOUNT_STATUS_ON_RESTORE,
            title = "${userUi.accountStatusActive} (${commonUi.exclude})",
            options = accountStatusOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_ACCOUNT_STATUS_ON_RESTORE_SET,
            title = "${userUi.accountStatusActive} (${commonUi.isSet})"
        )
    )
}

@Composable
private fun createAuthorityAndPermissionFilterDefinitions(): List<ListingFilterDefinition> {
    val commonUi = LocalCommonStrings.current.ui
    return listOf(
        NumberListingFilterDefinition(
            id = UserFilterValues.AUTHORITY_LEVEL_FROM,
            title = commonUi.authorityLevelFrom,
            placeholder = "0",
            minValue = AuthorityLevel.MIN_AUTHORITY_LEVEL,
            maxValue = AuthorityLevel.MAX_AUTHORITY_LEVEL,
            defaultValueOnFocusLost = AuthorityLevel.MIN_AUTHORITY_LEVEL
        ),
        NumberListingFilterDefinition(
            id = UserFilterValues.AUTHORITY_LEVEL_TO,
            title = commonUi.authorityLevelTo,
            placeholder = "100",
            minValue = AuthorityLevel.MIN_AUTHORITY_LEVEL,
            maxValue = AuthorityLevel.MAX_AUTHORITY_LEVEL,
            defaultValueOnFocusLost = AuthorityLevel.MAX_AUTHORITY_LEVEL
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.PERMISSION_CODES,
            title = commonUi.include,
            placeholder = commonUi.searchPlaceholder
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.EXCLUDE_PERMISSION_CODES,
            title = commonUi.exclude,
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_TOTP_ENABLED,
            title = commonUi.totpEnabled
        )
    )
}

@Composable
private fun createLockoutFilterDefinitions(
    lockoutTypeOptions: List<ListingFilterChoiceOption>
): List<ListingFilterDefinition> {
    val commonUi = LocalCommonStrings.current.ui
    return listOf(
        ChoiceListingFilterDefinition(
            id = UserFilterValues.ACCOUNT_LOCKOUT_TYPE,
            title = commonUi.lockoutType,
            options = lockoutTypeOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        ChoiceListingFilterDefinition(
            id = UserFilterValues.EXCLUDE_ACCOUNT_LOCKOUT_TYPE,
            title = "${commonUi.lockoutType} (${commonUi.exclude})",
            options = lockoutTypeOptions,
            isMultiple = true,
            presentationStyle = ChoiceFilterPresentationStyle.DROPDOWN
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.TEMPORARY_LOCKOUT_UNTIL_FROM,
            title = "${commonUi.lockoutUntil} (${commonUi.from})",
            placeholder = commonUi.searchPlaceholder
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.TEMPORARY_LOCKOUT_UNTIL_TO,
            title = "${commonUi.lockoutUntil} (${commonUi.to})",
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_TEMPORARY_LOCKOUT_UNTIL_SET,
            title = "${commonUi.lockoutUntil} (${commonUi.isSet})"
        )
    )
}

@Composable
private fun createTimestampFilterDefinitions(): List<ListingFilterDefinition> {
    val commonUi = LocalCommonStrings.current.ui
    return listOf(
        TextListingFilterDefinition(
            id = UserFilterValues.LAST_LOGIN_AT_FROM,
            title = "${commonUi.lastLoginAt} (${commonUi.from})",
            placeholder = commonUi.searchPlaceholder
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.LAST_LOGIN_AT_TO,
            title = "${commonUi.lastLoginAt} (${commonUi.to})",
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_LAST_LOGIN_AT_SET,
            title = "${commonUi.lastLoginAt} (${commonUi.isSet})"
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.LAST_ACTIVE_AT_FROM,
            title = "${commonUi.lastActiveAt} (${commonUi.from})",
            placeholder = commonUi.searchPlaceholder
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.LAST_ACTIVE_AT_TO,
            title = "${commonUi.lastActiveAt} (${commonUi.to})",
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_LAST_ACTIVE_AT_SET,
            title = "${commonUi.lastActiveAt} (${commonUi.isSet})"
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.CREATED_AT_FROM,
            title = "${commonUi.createdAt} (${commonUi.from})",
            placeholder = commonUi.searchPlaceholder
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.CREATED_AT_TO,
            title = "${commonUi.createdAt} (${commonUi.to})",
            placeholder = commonUi.searchPlaceholder
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UPDATED_AT_FROM,
            title = "${commonUi.updatedAt} (${commonUi.from})",
            placeholder = commonUi.searchPlaceholder
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.UPDATED_AT_TO,
            title = "${commonUi.updatedAt} (${commonUi.to})",
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_UPDATED_AT_SET,
            title = "${commonUi.updatedAt} (${commonUi.isSet})"
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.SCHEDULED_PERMANENT_DELETION_AT_FROM,
            title = "${commonUi.scheduledPermanentDeletionAt} (${commonUi.from})",
            placeholder = commonUi.searchPlaceholder
        ),
        TextListingFilterDefinition(
            id = UserFilterValues.SCHEDULED_PERMANENT_DELETION_AT_TO,
            title = "${commonUi.scheduledPermanentDeletionAt} (${commonUi.to})",
            placeholder = commonUi.searchPlaceholder
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_SCHEDULED_PERMANENT_DELETION_AT_SET,
            title = "${commonUi.scheduledPermanentDeletionAt} (${commonUi.isSet})"
        )
    )
}

@Composable
private fun createStorageFilterDefinitions(): List<ListingFilterDefinition> {
    val commonUi = LocalCommonStrings.current.ui
    val storageUi = LocalStorageStrings.current.ui
    val userUi = LocalUserStrings.current.ui
    return listOf(
        NumberListingFilterDefinition(
            id = UserFilterValues.STORAGE_QUOTA_BYTES_FROM,
            title = "${storageUi.storageQuotaBytes} (${commonUi.from})",
            placeholder = "0"
        ),
        NumberListingFilterDefinition(
            id = UserFilterValues.STORAGE_QUOTA_BYTES_TO,
            title = "${storageUi.storageQuotaBytes} (${commonUi.to})",
            placeholder = "100000000"
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_STORAGE_QUOTA_BYTES_SET,
            title = "${storageUi.storageQuotaBytes} (${commonUi.isSet})"
        ),
        NumberListingFilterDefinition(
            id = UserFilterValues.STORAGE_MAX_UPLOAD_SIZE_BYTES_FROM,
            title = "${storageUi.storageMaxUploadSizeBytes} (${commonUi.from})",
            placeholder = "0"
        ),
        NumberListingFilterDefinition(
            id = UserFilterValues.STORAGE_MAX_UPLOAD_SIZE_BYTES_TO,
            title = "${storageUi.storageMaxUploadSizeBytes} (${commonUi.to})",
            placeholder = "100000000"
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_STORAGE_MAX_UPLOAD_SIZE_BYTES_SET,
            title = "${storageUi.storageMaxUploadSizeBytes} (${commonUi.isSet})"
        ),
        NumberListingFilterDefinition(
            id = UserFilterValues.STORAGE_MAX_PENDING_UPLOADS_FROM,
            title = "${storageUi.storageMaxPendingUploads} (${commonUi.from})",
            placeholder = "0"
        ),
        NumberListingFilterDefinition(
            id = UserFilterValues.STORAGE_MAX_PENDING_UPLOADS_TO,
            title = "${storageUi.storageMaxPendingUploads} (${commonUi.to})",
            placeholder = "100"
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_STORAGE_MAX_PENDING_UPLOADS_SET,
            title = "${storageUi.storageMaxPendingUploads} (${commonUi.isSet})"
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_STORAGE_CONFIGURATION_SET,
            title = "${userUi.customStorageConfiguration} (${commonUi.isSet})"
        )
    )
}

@Composable
private fun createPersonalFilterDefinitions(): List<ListingFilterDefinition> {
    val commonUi = LocalCommonStrings.current.ui
    val userUi = LocalUserStrings.current.ui
    return listOf(
        NumberListingFilterDefinition(
            id = UserFilterValues.BIRTHDATE_FROM,
            title = "${userUi.birthdate} (${commonUi.from})",
            placeholder = "0"
        ),
        NumberListingFilterDefinition(
            id = UserFilterValues.BIRTHDATE_TO,
            title = "${userUi.birthdate} (${commonUi.to})",
            placeholder = "0"
        ),
        BooleanListingFilterDefinition(
            id = UserFilterValues.IS_BIRTHDATE_SET,
            title = "${userUi.birthdate} (${commonUi.isSet})"
        )
    )
}

/**
 * Constants for user authority level bounds.
 */
internal object AuthorityLevel {
    const val MIN_AUTHORITY_LEVEL: Long = 0L
    const val MAX_AUTHORITY_LEVEL: Long = 100L
}
