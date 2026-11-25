package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.globallist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.appendResult
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.NumberListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUsersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortBy
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.globallist.ManagementUserListParams
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * Default [GlobalUserListComponent] implementation managing administrative system-wide user accounts list.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Navigation parameters containing filter state and page size.
 * @param getUsersUseCase Use case to fetch paged users.
 * @param deleteUserUseCase Use case to delete user account.
 * @param userRepository Optional user repository to detect current self user account.
 * @param onNavigateToUserDetail Callback invoked when a user card is selected.
 * @param onNavigateToCreateUser Callback invoked to navigate to user creation screen.
 * @param onBack Callback invoked to pop the screen.
 */
public class GlobalUserListComponentImpl(
    componentContext: ComponentContext,
    private val params: ManagementUserListParams,
    private val getUsersUseCase: GetUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val userRepository: UserRepository? = null,
    private val onNavigateToUserDetail: (UserId) -> Unit,
    private val onNavigateToCreateUser: () -> Unit,
    private val onBack: () -> Unit
) : GlobalUserListComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<GlobalUserListScreenState>(GlobalUserListScreenState.Loading)
    override val state: Value<GlobalUserListScreenState> = _state

    init {
        fetchInitialPage()
    }

    override fun onRefresh() {
        val currentContent = _state.value as? GlobalUserListScreenState.Content
        fetchPage(
            pageNumber = 1,
            sortState = currentContent?.sortState,
            filterStates = currentContent?.filterStates
        )
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? GlobalUserListScreenState.Content ?: return
        if (currentContent.paging.isNextPageLoading || !currentContent.paging.hasMorePages) {
            return
        }
        val nextPage = currentContent.paging.nextPageNumber
        fetchPage(
            pageNumber = nextPage,
            sortState = currentContent.sortState,
            filterStates = currentContent.filterStates
        )
    }

    override fun onUserClick(userId: UserId) {
        onNavigateToUserDetail(userId)
    }

    override fun onCreateUserClick() {
        onNavigateToCreateUser()
    }

    override fun onUserDeleted(userId: UserId) {
        val currentContent = _state.value as? GlobalUserListScreenState.Content ?: return
        val updatedItems = currentContent.paging.items.filterNot { it.id == userId }
        val newPaging = currentContent.paging.copy(items = updatedItems)
        _state.value = currentContent.copy(paging = newPaging)
    }

    override fun onUserCreated() {
        onRefresh()
    }

    override fun onBackClick() {
        onBack()
    }

    override fun onToggleFilterPanel() {
        val currentContent = _state.value as? GlobalUserListScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = !currentContent.isFilterPanelExpanded)
    }

    override fun onSortChanged(sortState: ListingSortState) {
        val currentContent = _state.value as? GlobalUserListScreenState.Content ?: return
        _state.value = currentContent.copy(sortState = sortState)
        fetchPage(
            pageNumber = 1,
            sortState = sortState,
            filterStates = currentContent.filterStates
        )
    }

    override fun onFilterChanged(
        filterId: String,
        filterState: ListingFilterState?
    ) {
        val currentContent = _state.value as? GlobalUserListScreenState.Content ?: return
        val updatedFilters = currentContent.filterStates.toMutableMap()
        if (filterState != null) {
            updatedFilters[filterId] = filterState
        } else {
            updatedFilters.remove(filterId)
        }
        _state.value = currentContent.copy(filterStates = updatedFilters)
    }

    override fun onApplyFilters() {
        val currentContent = _state.value as? GlobalUserListScreenState.Content ?: return
        fetchPage(
            pageNumber = 1,
            sortState = currentContent.sortState,
            filterStates = currentContent.filterStates
        )
    }

    private fun fetchInitialPage() {
        fetchPage(
            pageNumber = params.pageNumber,
            sortState = null,
            filterStates = null
        )
    }

    private fun fetchPage(
        pageNumber: Int,
        sortState: ListingSortState?,
        filterStates: Map<String, ListingFilterState>?
    ) {
        val requestParams = buildManagementUserListParams(pageNumber, sortState, filterStates)
        scope.launch {
            getUsersUseCase(params = requestParams)
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? GlobalUserListScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserSummary>()).appendResult(pagedResult)
                    val currentUserId = userRepository?.currentUser?.firstOrNull()?.id
                    _state.value = currentContent?.copy(
                        paging = newPaging,
                        currentUserId = currentUserId
                    ) ?: GlobalUserListScreenState.Content(
                        paging = newPaging,
                        sortState = sortState,
                        filterStates = filterStates ?: emptyMap(),
                        currentUserId = currentUserId
                    )
                }
                .onError { error ->
                    val currentContent = _state.value as? GlobalUserListScreenState.Content
                    if (currentContent != null) {
                        _state.value = currentContent.copy(
                            paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                        )
                    } else {
                        _state.value = GlobalUserListScreenState.Error(error)
                    }
                }
        }
    }

    private fun buildManagementUserListParams(
        pageNumber: Int,
        sortState: ListingSortState?,
        filterStates: Map<String, ListingFilterState>?
    ): ManagementUserListParams {
        val sortOrder = if (sortState?.isAscending == true) SortOrder.ASC else SortOrder.DESC
        val sortBy = sortState?.optionId?.let { optionId ->
            UserSortBy.entries.firstOrNull { it.name == optionId || it.value == optionId }
        } ?: params.sortBy

        val identityFilters = parseIdentityFilterParams(filterStates)
        val roleAndStatusFilters = parseRoleAndStatusFilterParams(filterStates)
        val authorityAndSecurityFilters = parseAuthorityAndSecurityFilterParams(filterStates)
        val timestampFilters = parseTimestampFilterParams(filterStates)
        val storageFilters = parseStorageFilterParams(filterStates)

        return ManagementUserListParams(
            pageNumber = pageNumber,
            sortBy = sortBy,
            sortOrder = sortOrder,
            userIds = identityFilters.userIds,
            userRoles = roleAndStatusFilters.userRoles,
            excludeUserRoles = roleAndStatusFilters.excludeUserRoles,
            accountStatuses = roleAndStatusFilters.accountStatuses,
            excludeAccountStatuses = roleAndStatusFilters.excludeAccountStatuses,
            accountStatusesOnRestore = roleAndStatusFilters.accountStatusesOnRestore,
            excludeAccountStatusesOnRestore = roleAndStatusFilters.excludeAccountStatusesOnRestore,
            isAccountStatusOnRestoreSet = roleAndStatusFilters.isAccountStatusOnRestoreSet,
            authorityLevelFrom = authorityAndSecurityFilters.authorityLevelFrom,
            authorityLevelTo = authorityAndSecurityFilters.authorityLevelTo,
            permissionCodes = authorityAndSecurityFilters.permissionCodes,
            excludePermissionCodes = authorityAndSecurityFilters.excludePermissionCodes,
            isTotpEnabled = authorityAndSecurityFilters.isTotpEnabled,
            lastLoginAtFrom = timestampFilters.lastLoginAtFrom,
            lastLoginAtTo = timestampFilters.lastLoginAtTo,
            isLastLoginAtSet = timestampFilters.isLastLoginAtSet,
            lastActiveAtFrom = timestampFilters.lastActiveAtFrom,
            lastActiveAtTo = timestampFilters.lastActiveAtTo,
            isLastActiveAtSet = timestampFilters.isLastActiveAtSet,
            createdAtFrom = timestampFilters.createdAtFrom,
            createdAtTo = timestampFilters.createdAtTo,
            updatedAtFrom = timestampFilters.updatedAtFrom,
            updatedAtTo = timestampFilters.updatedAtTo,
            isUpdatedAtSet = timestampFilters.isUpdatedAtSet,
            scheduledPermanentDeletionAtFrom = timestampFilters.scheduledPermanentDeletionAtFrom,
            scheduledPermanentDeletionAtTo = timestampFilters.scheduledPermanentDeletionAtTo,
            isScheduledPermanentDeletionAtSet = timestampFilters.isScheduledPermanentDeletionAtSet,
            accountLockoutTypes = authorityAndSecurityFilters.accountLockoutTypes,
            excludeAccountLockoutTypes = authorityAndSecurityFilters.excludeAccountLockoutTypes,
            temporaryLockoutUntilFrom = authorityAndSecurityFilters.temporaryLockoutUntilFrom,
            temporaryLockoutUntilTo = authorityAndSecurityFilters.temporaryLockoutUntilTo,
            isTemporaryLockoutUntilSet = authorityAndSecurityFilters.isTemporaryLockoutUntilSet,
            storageQuotaBytesFrom = storageFilters.storageQuotaBytesFrom,
            storageQuotaBytesTo = storageFilters.storageQuotaBytesTo,
            isStorageQuotaBytesSet = storageFilters.isStorageQuotaBytesSet,
            storageMaxUploadSizeBytesFrom = storageFilters.storageMaxUploadSizeBytesFrom,
            storageMaxUploadSizeBytesTo = storageFilters.storageMaxUploadSizeBytesTo,
            isStorageMaxUploadSizeBytesSet = storageFilters.isStorageMaxUploadSizeBytesSet,
            storageMaxPendingUploadsFrom = storageFilters.storageMaxPendingUploadsFrom,
            storageMaxPendingUploadsTo = storageFilters.storageMaxPendingUploadsTo,
            isStorageMaxPendingUploadsSet = storageFilters.isStorageMaxPendingUploadsSet,
            isStorageConfigurationSet = storageFilters.isStorageConfigurationSet,
            nickname = identityFilters.nickname,
            isNicknameSet = identityFilters.isNicknameSet,
            displayName = identityFilters.displayName,
            isDisplayNameSet = identityFilters.isDisplayNameSet,
            isAvatarSet = identityFilters.isAvatarSet,
            birthdateFrom = timestampFilters.birthdateFrom,
            birthdateTo = timestampFilters.birthdateTo,
            isBirthdateSet = timestampFilters.isBirthdateSet,
            searchQuery = identityFilters.searchQuery
        )
    }

    private fun parseIdentityFilterParams(filterStates: Map<String, ListingFilterState>?): IdentityFilterParams {
        val userIds = (filterStates?.get(UserFilterValues.USER_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.userIds
        val nickname = (filterStates?.get(UserFilterValues.NICKNAME) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.nickname
        val isNicknameSet = (filterStates?.get(UserFilterValues.IS_NICKNAME_SET) as? BooleanListingFilterState)
            ?.value ?: params.isNicknameSet
        val displayName = (filterStates?.get(UserFilterValues.DISPLAY_NAME) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.displayName
        val isDisplayNameSet = (filterStates?.get(UserFilterValues.IS_DISPLAY_NAME_SET) as? BooleanListingFilterState)
            ?.value ?: params.isDisplayNameSet
        val isAvatarSet = (filterStates?.get(UserFilterValues.IS_AVATAR_SET) as? BooleanListingFilterState)
            ?.value ?: params.isAvatarSet
        val searchQuery = (filterStates?.get(UserFilterValues.SEARCH_QUERY) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: (params.searchQuery?.takeIf { it.isNotBlank() } ?: "")

        return IdentityFilterParams(
            userIds = userIds,
            nickname = nickname ?: "",
            isNicknameSet = isNicknameSet,
            displayName = displayName ?: "",
            isDisplayNameSet = isDisplayNameSet,
            isAvatarSet = isAvatarSet,
            searchQuery = searchQuery
        )
    }

    private fun parseRoleAndStatusFilterParams(
        filterStates: Map<String, ListingFilterState>?
    ): RoleAndStatusFilterParams {
        val roles = (filterStates?.get(UserFilterValues.ROLE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserRole.fromValueOrNull(it) } ?: params.userRoles
        val excludeRoles = (filterStates?.get(UserFilterValues.EXCLUDE_ROLE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserRole.fromValueOrNull(it) } ?: params.excludeUserRoles
        val accountStatuses = (filterStates?.get(UserFilterValues.ACCOUNT_STATUS) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAccountStatus.fromValueOrNull(it) } ?: params.accountStatuses
        val excludeAccountStatuses =
            (filterStates?.get(UserFilterValues.EXCLUDE_ACCOUNT_STATUS) as? ChoiceListingFilterState)
                ?.selectedIds?.mapNotNull { UserAccountStatus.fromValueOrNull(it) } ?: params.excludeAccountStatuses
        val accountStatusesOnRestore =
            (filterStates?.get(UserFilterValues.ACCOUNT_STATUS_ON_RESTORE) as? ChoiceListingFilterState)
                ?.selectedIds?.mapNotNull { UserAccountStatus.fromValueOrNull(it) } ?: params.accountStatusesOnRestore
        val excludeAccountStatusesOnRestore =
            (filterStates?.get(UserFilterValues.EXCLUDE_ACCOUNT_STATUS_ON_RESTORE) as? ChoiceListingFilterState)
                ?.selectedIds?.mapNotNull { UserAccountStatus.fromValueOrNull(it) }
                ?: params.excludeAccountStatusesOnRestore
        val isAccountStatusOnRestoreSet =
            (filterStates?.get(UserFilterValues.IS_ACCOUNT_STATUS_ON_RESTORE_SET) as? BooleanListingFilterState)
                ?.value ?: params.isAccountStatusOnRestoreSet

        return RoleAndStatusFilterParams(
            userRoles = roles,
            excludeUserRoles = excludeRoles,
            accountStatuses = accountStatuses,
            excludeAccountStatuses = excludeAccountStatuses,
            accountStatusesOnRestore = accountStatusesOnRestore,
            excludeAccountStatusesOnRestore = excludeAccountStatusesOnRestore,
            isAccountStatusOnRestoreSet = isAccountStatusOnRestoreSet
        )
    }

    private fun parseAuthorityAndSecurityFilterParams(
        filterStates: Map<String, ListingFilterState>?
    ): AuthorityAndSecurityFilterParams {
        val authorityLevelFrom = (filterStates?.get(UserFilterValues.AUTHORITY_LEVEL_FROM) as? NumberListingFilterState)
            ?.value?.toInt() ?: params.authorityLevelFrom
        val authorityLevelTo = (filterStates?.get(UserFilterValues.AUTHORITY_LEVEL_TO) as? NumberListingFilterState)
            ?.value?.toInt() ?: params.authorityLevelTo
        val permissionCodes = (filterStates?.get(UserFilterValues.PERMISSION_CODES) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.permissionCodes
        val excludePermissionCodes =
            (filterStates?.get(UserFilterValues.EXCLUDE_PERMISSION_CODES) as? TextListingFilterState)
                ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.excludePermissionCodes
        val isTotpEnabled = (filterStates?.get(UserFilterValues.IS_TOTP_ENABLED) as? BooleanListingFilterState)
            ?.value ?: params.isTotpEnabled
        val accountLockoutTypes =
            (filterStates?.get(UserFilterValues.ACCOUNT_LOCKOUT_TYPE) as? ChoiceListingFilterState)
                ?.selectedIds?.mapNotNull { AccountLockoutType.fromValueOrNull(it) } ?: params.accountLockoutTypes
        val excludeAccountLockoutTypes =
            (filterStates?.get(UserFilterValues.EXCLUDE_ACCOUNT_LOCKOUT_TYPE) as? ChoiceListingFilterState)
                ?.selectedIds?.mapNotNull { AccountLockoutType.fromValueOrNull(it) }
                ?: params.excludeAccountLockoutTypes
        val temporaryLockoutUntilFrom =
            (filterStates?.get(UserFilterValues.TEMPORARY_LOCKOUT_UNTIL_FROM) as? TextListingFilterState)
                ?.value?.takeIf { it.isNotBlank() } ?: params.temporaryLockoutUntilFrom
        val temporaryLockoutUntilTo =
            (filterStates?.get(UserFilterValues.TEMPORARY_LOCKOUT_UNTIL_TO) as? TextListingFilterState)
                ?.value?.takeIf { it.isNotBlank() } ?: params.temporaryLockoutUntilTo
        val isTemporaryLockoutUntilSet =
            (filterStates?.get(UserFilterValues.IS_TEMPORARY_LOCKOUT_UNTIL_SET) as? BooleanListingFilterState)
                ?.value ?: params.isTemporaryLockoutUntilSet

        return AuthorityAndSecurityFilterParams(
            authorityLevelFrom = authorityLevelFrom,
            authorityLevelTo = authorityLevelTo,
            permissionCodes = permissionCodes,
            excludePermissionCodes = excludePermissionCodes,
            isTotpEnabled = isTotpEnabled,
            accountLockoutTypes = accountLockoutTypes,
            excludeAccountLockoutTypes = excludeAccountLockoutTypes,
            temporaryLockoutUntilFrom = temporaryLockoutUntilFrom ?: "",
            temporaryLockoutUntilTo = temporaryLockoutUntilTo ?: "",
            isTemporaryLockoutUntilSet = isTemporaryLockoutUntilSet
        )
    }

    private fun parseTimestampFilterParams(filterStates: Map<String, ListingFilterState>?): TimestampFilterParams {
        val lastLogin = parseLastLoginFilterParams(filterStates)
        val lastActive = parseLastActiveFilterParams(filterStates)
        val createdAndUpdated = parseCreatedAndUpdatedFilterParams(filterStates)
        val scheduledDeletion = parseScheduledDeletionFilterParams(filterStates)
        val birthdate = parseBirthdateFilterParams(filterStates)

        return TimestampFilterParams(
            lastLoginAtFrom = lastLogin.from,
            lastLoginAtTo = lastLogin.to,
            isLastLoginAtSet = lastLogin.isSet,
            lastActiveAtFrom = lastActive.from,
            lastActiveAtTo = lastActive.to,
            isLastActiveAtSet = lastActive.isSet,
            createdAtFrom = createdAndUpdated.createdAtFrom,
            createdAtTo = createdAndUpdated.createdAtTo,
            updatedAtFrom = createdAndUpdated.updatedAtFrom,
            updatedAtTo = createdAndUpdated.updatedAtTo,
            isUpdatedAtSet = createdAndUpdated.isUpdatedAtSet,
            scheduledPermanentDeletionAtFrom = scheduledDeletion.from,
            scheduledPermanentDeletionAtTo = scheduledDeletion.to,
            isScheduledPermanentDeletionAtSet = scheduledDeletion.isSet,
            birthdateFrom = birthdate.from,
            birthdateTo = birthdate.to,
            isBirthdateSet = birthdate.isSet
        )
    }

    private fun parseLastLoginFilterParams(
        filterStates: Map<String, ListingFilterState>?
    ): TimestampRangeFilterParams {
        val from = (filterStates?.get(UserFilterValues.LAST_LOGIN_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.lastLoginAtFrom
        val to = (filterStates?.get(UserFilterValues.LAST_LOGIN_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.lastLoginAtTo
        val isSet = (filterStates?.get(UserFilterValues.IS_LAST_LOGIN_AT_SET) as? BooleanListingFilterState)
            ?.value ?: params.isLastLoginAtSet
        return TimestampRangeFilterParams(
            from = from ?: "",
            to = to ?: "",
            isSet = isSet
        )
    }

    private fun parseLastActiveFilterParams(
        filterStates: Map<String, ListingFilterState>?
    ): TimestampRangeFilterParams {
        val from = (filterStates?.get(UserFilterValues.LAST_ACTIVE_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.lastActiveAtFrom
        val to = (filterStates?.get(UserFilterValues.LAST_ACTIVE_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.lastActiveAtTo
        val isSet = (filterStates?.get(UserFilterValues.IS_LAST_ACTIVE_AT_SET) as? BooleanListingFilterState)
            ?.value ?: params.isLastActiveAtSet
        return TimestampRangeFilterParams(
            from = from ?: "",
            to = to ?: "",
            isSet = isSet
        )
    }

    private fun parseCreatedAndUpdatedFilterParams(
        filterStates: Map<String, ListingFilterState>?
    ): CreatedAndUpdatedFilterParams {
        val createdAtFrom = (filterStates?.get(UserFilterValues.CREATED_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.createdAtFrom
        val createdAtTo = (filterStates?.get(UserFilterValues.CREATED_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.createdAtTo
        val updatedAtFrom = (filterStates?.get(UserFilterValues.UPDATED_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.updatedAtFrom
        val updatedAtTo = (filterStates?.get(UserFilterValues.UPDATED_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.updatedAtTo
        val isUpdatedAtSet = (filterStates?.get(UserFilterValues.IS_UPDATED_AT_SET) as? BooleanListingFilterState)
            ?.value ?: params.isUpdatedAtSet

        return CreatedAndUpdatedFilterParams(
            createdAtFrom = createdAtFrom ?: "",
            createdAtTo = createdAtTo ?: "",
            updatedAtFrom = updatedAtFrom ?: "",
            updatedAtTo = updatedAtTo ?: "",
            isUpdatedAtSet = isUpdatedAtSet
        )
    }

    private fun parseScheduledDeletionFilterParams(
        filterStates: Map<String, ListingFilterState>?
    ): TimestampRangeFilterParams {
        val from = (filterStates?.get(UserFilterValues.SCHEDULED_PERMANENT_DELETION_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.scheduledPermanentDeletionAtFrom
        val to = (filterStates?.get(UserFilterValues.SCHEDULED_PERMANENT_DELETION_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.scheduledPermanentDeletionAtTo
        val isSet =
            (filterStates?.get(UserFilterValues.IS_SCHEDULED_PERMANENT_DELETION_AT_SET) as? BooleanListingFilterState)
                ?.value ?: params.isScheduledPermanentDeletionAtSet
        return TimestampRangeFilterParams(
            from = from ?: "",
            to = to ?: "",
            isSet = isSet
        )
    }

    private fun parseBirthdateFilterParams(filterStates: Map<String, ListingFilterState>?): TimestampRangeFilterParams {
        val from = (filterStates?.get(UserFilterValues.BIRTHDATE_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.birthdateFrom
        val to = (filterStates?.get(UserFilterValues.BIRTHDATE_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.birthdateTo
        val isSet = (filterStates?.get(UserFilterValues.IS_BIRTHDATE_SET) as? BooleanListingFilterState)
            ?.value ?: params.isBirthdateSet
        return TimestampRangeFilterParams(
            from = from ?: "",
            to = to ?: "",
            isSet = isSet
        )
    }

    private fun parseStorageFilterParams(filterStates: Map<String, ListingFilterState>?): StorageFilterParams {
        val storageQuotaBytesFrom =
            (filterStates?.get(UserFilterValues.STORAGE_QUOTA_BYTES_FROM) as? NumberListingFilterState)
                ?.value ?: params.storageQuotaBytesFrom
        val storageQuotaBytesTo =
            (filterStates?.get(UserFilterValues.STORAGE_QUOTA_BYTES_TO) as? NumberListingFilterState)
                ?.value ?: params.storageQuotaBytesTo
        val isStorageQuotaBytesSet =
            (filterStates?.get(UserFilterValues.IS_STORAGE_QUOTA_BYTES_SET) as? BooleanListingFilterState)
                ?.value ?: params.isStorageQuotaBytesSet
        val storageMaxUploadSizeBytesFrom =
            (filterStates?.get(UserFilterValues.STORAGE_MAX_UPLOAD_SIZE_BYTES_FROM) as? NumberListingFilterState)
                ?.value ?: params.storageMaxUploadSizeBytesFrom
        val storageMaxUploadSizeBytesTo =
            (filterStates?.get(UserFilterValues.STORAGE_MAX_UPLOAD_SIZE_BYTES_TO) as? NumberListingFilterState)
                ?.value ?: params.storageMaxUploadSizeBytesTo
        val isStorageMaxUploadSizeBytesSet =
            (filterStates?.get(UserFilterValues.IS_STORAGE_MAX_UPLOAD_SIZE_BYTES_SET) as? BooleanListingFilterState)
                ?.value ?: params.isStorageMaxUploadSizeBytesSet
        val storageMaxPendingUploadsFrom =
            (filterStates?.get(UserFilterValues.STORAGE_MAX_PENDING_UPLOADS_FROM) as? NumberListingFilterState)
                ?.value ?: params.storageMaxPendingUploadsFrom
        val storageMaxPendingUploadsTo =
            (filterStates?.get(UserFilterValues.STORAGE_MAX_PENDING_UPLOADS_TO) as? NumberListingFilterState)
                ?.value ?: params.storageMaxPendingUploadsTo
        val isStorageMaxPendingUploadsSet =
            (filterStates?.get(UserFilterValues.IS_STORAGE_MAX_PENDING_UPLOADS_SET) as? BooleanListingFilterState)
                ?.value ?: params.isStorageMaxPendingUploadsSet
        val isStorageConfigurationSet =
            (filterStates?.get(UserFilterValues.IS_STORAGE_CONFIGURATION_SET) as? BooleanListingFilterState)
                ?.value ?: params.isStorageConfigurationSet

        return StorageFilterParams(
            storageQuotaBytesFrom = storageQuotaBytesFrom,
            storageQuotaBytesTo = storageQuotaBytesTo,
            isStorageQuotaBytesSet = isStorageQuotaBytesSet,
            storageMaxUploadSizeBytesFrom = storageMaxUploadSizeBytesFrom,
            storageMaxUploadSizeBytesTo = storageMaxUploadSizeBytesTo,
            isStorageMaxUploadSizeBytesSet = isStorageMaxUploadSizeBytesSet,
            storageMaxPendingUploadsFrom = storageMaxPendingUploadsFrom,
            storageMaxPendingUploadsTo = storageMaxPendingUploadsTo,
            isStorageMaxPendingUploadsSet = isStorageMaxPendingUploadsSet,
            isStorageConfigurationSet = isStorageConfigurationSet
        )
    }
}

private data class TimestampRangeFilterParams(
    val from: String,
    val to: String,
    val isSet: Boolean?
)

private data class CreatedAndUpdatedFilterParams(
    val createdAtFrom: String,
    val createdAtTo: String,
    val updatedAtFrom: String,
    val updatedAtTo: String,
    val isUpdatedAtSet: Boolean?
)

private data class IdentityFilterParams(
    val userIds: List<String>,
    val nickname: String,
    val isNicknameSet: Boolean?,
    val displayName: String,
    val isDisplayNameSet: Boolean?,
    val isAvatarSet: Boolean?,
    val searchQuery: String
)

private data class RoleAndStatusFilterParams(
    val userRoles: List<UserRole>,
    val excludeUserRoles: List<UserRole>,
    val accountStatuses: List<UserAccountStatus>,
    val excludeAccountStatuses: List<UserAccountStatus>,
    val accountStatusesOnRestore: List<UserAccountStatus>,
    val excludeAccountStatusesOnRestore: List<UserAccountStatus>,
    val isAccountStatusOnRestoreSet: Boolean?
)

private data class AuthorityAndSecurityFilterParams(
    val authorityLevelFrom: Int?,
    val authorityLevelTo: Int?,
    val permissionCodes: List<String>,
    val excludePermissionCodes: List<String>,
    val isTotpEnabled: Boolean?,
    val accountLockoutTypes: List<AccountLockoutType>,
    val excludeAccountLockoutTypes: List<AccountLockoutType>,
    val temporaryLockoutUntilFrom: String,
    val temporaryLockoutUntilTo: String,
    val isTemporaryLockoutUntilSet: Boolean?
)

private data class TimestampFilterParams(
    val lastLoginAtFrom: String,
    val lastLoginAtTo: String,
    val isLastLoginAtSet: Boolean?,
    val lastActiveAtFrom: String,
    val lastActiveAtTo: String,
    val isLastActiveAtSet: Boolean?,
    val createdAtFrom: String,
    val createdAtTo: String,
    val updatedAtFrom: String,
    val updatedAtTo: String,
    val isUpdatedAtSet: Boolean?,
    val scheduledPermanentDeletionAtFrom: String,
    val scheduledPermanentDeletionAtTo: String,
    val isScheduledPermanentDeletionAtSet: Boolean?,
    val birthdateFrom: String,
    val birthdateTo: String,
    val isBirthdateSet: Boolean?
)

private data class StorageFilterParams(
    val storageQuotaBytesFrom: Long?,
    val storageQuotaBytesTo: Long?,
    val isStorageQuotaBytesSet: Boolean?,
    val storageMaxUploadSizeBytesFrom: Long?,
    val storageMaxUploadSizeBytesTo: Long?,
    val isStorageMaxUploadSizeBytesSet: Boolean?,
    val storageMaxPendingUploadsFrom: Long?,
    val storageMaxPendingUploadsTo: Long?,
    val isStorageMaxPendingUploadsSet: Boolean?,
    val isStorageConfigurationSet: Boolean?
)
