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
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.permission.PermissionCode
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortBy
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
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
class GlobalUserListComponentImpl(
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

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
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
        val sortOrder = if (sortState?.isAscending == true) SortOrder.ASC else SortOrder.DESC
        val sortBy = sortState?.optionId?.let { optionId ->
            UserSortBy.entries.firstOrNull { it.name == optionId || it.value == optionId }
        } ?: params.sortBy

        val userIds = (filterStates?.get(UserFilterValues.USER_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.userIds.ifEmpty { null }
        val roles = (filterStates?.get(UserFilterValues.ROLE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserRole.fromValueOrNull(it) } ?: params.userRoles.ifEmpty { null }
        val excludeRoles = (filterStates?.get(UserFilterValues.EXCLUDE_ROLE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserRole.fromValueOrNull(it) } ?: params.excludeUserRoles.ifEmpty { null }
        val accountStatuses = (filterStates?.get(UserFilterValues.ACCOUNT_STATUS) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAccountStatus.fromValueOrNull(it) } ?: params.accountStatuses.ifEmpty { null }
        val excludeAccountStatuses = (filterStates?.get(UserFilterValues.EXCLUDE_ACCOUNT_STATUS) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAccountStatus.fromValueOrNull(it) } ?: params.excludeAccountStatuses.ifEmpty { null }
        val accountStatusesOnRestore = (filterStates?.get(UserFilterValues.ACCOUNT_STATUS_ON_RESTORE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAccountStatus.fromValueOrNull(it) } ?: params.accountStatusesOnRestore.ifEmpty { null }
        val excludeAccountStatusesOnRestore = (filterStates?.get(UserFilterValues.EXCLUDE_ACCOUNT_STATUS_ON_RESTORE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAccountStatus.fromValueOrNull(it) } ?: params.excludeAccountStatusesOnRestore.ifEmpty { null }
        val isAccountStatusOnRestoreSet = (filterStates?.get(UserFilterValues.IS_ACCOUNT_STATUS_ON_RESTORE_SET) as? BooleanListingFilterState)
            ?.value ?: params.isAccountStatusOnRestoreSet
        val accountLockoutTypes = (filterStates?.get(UserFilterValues.ACCOUNT_LOCKOUT_TYPE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { AccountLockoutType.fromValueOrNull(it) } ?: params.accountLockoutTypes.ifEmpty { null }
        val excludeAccountLockoutTypes = (filterStates?.get(UserFilterValues.EXCLUDE_ACCOUNT_LOCKOUT_TYPE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { AccountLockoutType.fromValueOrNull(it) } ?: params.excludeAccountLockoutTypes.ifEmpty { null }
        val isTotpEnabled = (filterStates?.get(UserFilterValues.IS_TOTP_ENABLED) as? BooleanListingFilterState)
            ?.value ?: params.isTotpEnabled
        val authorityLevelFrom = (filterStates?.get(UserFilterValues.AUTHORITY_LEVEL_FROM) as? NumberListingFilterState)
            ?.value?.toInt() ?: params.authorityLevelFrom
        val authorityLevelTo = (filterStates?.get(UserFilterValues.AUTHORITY_LEVEL_TO) as? NumberListingFilterState)
            ?.value?.toInt() ?: params.authorityLevelTo
        val permissionCodes = (filterStates?.get(UserFilterValues.PERMISSION_CODES) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(PermissionCode(it)) } ?: params.permissionCodes.map { PermissionCode(it) }.ifEmpty { null }
        val excludePermissionCodes = (filterStates?.get(UserFilterValues.EXCLUDE_PERMISSION_CODES) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(PermissionCode(it)) } ?: params.excludePermissionCodes.map { PermissionCode(it) }.ifEmpty { null }
        val temporaryLockoutUntilFrom = (filterStates?.get(UserFilterValues.TEMPORARY_LOCKOUT_UNTIL_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.temporaryLockoutUntilFrom
        val temporaryLockoutUntilTo = (filterStates?.get(UserFilterValues.TEMPORARY_LOCKOUT_UNTIL_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.temporaryLockoutUntilTo
        val isTemporaryLockoutUntilSet = (filterStates?.get(UserFilterValues.IS_TEMPORARY_LOCKOUT_UNTIL_SET) as? BooleanListingFilterState)
            ?.value ?: params.isTemporaryLockoutUntilSet
        val lastLoginAtFrom = (filterStates?.get(UserFilterValues.LAST_LOGIN_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.lastLoginAtFrom
        val lastLoginAtTo = (filterStates?.get(UserFilterValues.LAST_LOGIN_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.lastLoginAtTo
        val isLastLoginAtSet = (filterStates?.get(UserFilterValues.IS_LAST_LOGIN_AT_SET) as? BooleanListingFilterState)
            ?.value ?: params.isLastLoginAtSet
        val lastActiveAtFrom = (filterStates?.get(UserFilterValues.LAST_ACTIVE_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.lastActiveAtFrom
        val lastActiveAtTo = (filterStates?.get(UserFilterValues.LAST_ACTIVE_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.lastActiveAtTo
        val isLastActiveAtSet = (filterStates?.get(UserFilterValues.IS_LAST_ACTIVE_AT_SET) as? BooleanListingFilterState)
            ?.value ?: params.isLastActiveAtSet
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
        val scheduledPermanentDeletionAtFrom = (filterStates?.get(UserFilterValues.SCHEDULED_PERMANENT_DELETION_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.scheduledPermanentDeletionAtFrom
        val scheduledPermanentDeletionAtTo = (filterStates?.get(UserFilterValues.SCHEDULED_PERMANENT_DELETION_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.scheduledPermanentDeletionAtTo
        val isScheduledPermanentDeletionAtSet = (filterStates?.get(UserFilterValues.IS_SCHEDULED_PERMANENT_DELETION_AT_SET) as? BooleanListingFilterState)
            ?.value ?: params.isScheduledPermanentDeletionAtSet
        val storageQuotaBytesFrom = (filterStates?.get(UserFilterValues.STORAGE_QUOTA_BYTES_FROM) as? NumberListingFilterState)
            ?.value ?: params.storageQuotaBytesFrom
        val storageQuotaBytesTo = (filterStates?.get(UserFilterValues.STORAGE_QUOTA_BYTES_TO) as? NumberListingFilterState)
            ?.value ?: params.storageQuotaBytesTo
        val isStorageQuotaBytesSet = (filterStates?.get(UserFilterValues.IS_STORAGE_QUOTA_BYTES_SET) as? BooleanListingFilterState)
            ?.value ?: params.isStorageQuotaBytesSet
        val storageMaxUploadSizeBytesFrom = (filterStates?.get(UserFilterValues.STORAGE_MAX_UPLOAD_SIZE_BYTES_FROM) as? NumberListingFilterState)
            ?.value ?: params.storageMaxUploadSizeBytesFrom
        val storageMaxUploadSizeBytesTo = (filterStates?.get(UserFilterValues.STORAGE_MAX_UPLOAD_SIZE_BYTES_TO) as? NumberListingFilterState)
            ?.value ?: params.storageMaxUploadSizeBytesTo
        val isStorageMaxUploadSizeBytesSet = (filterStates?.get(UserFilterValues.IS_STORAGE_MAX_UPLOAD_SIZE_BYTES_SET) as? BooleanListingFilterState)
            ?.value ?: params.isStorageMaxUploadSizeBytesSet
        val storageMaxPendingUploadsFrom = (filterStates?.get(UserFilterValues.STORAGE_MAX_PENDING_UPLOADS_FROM) as? NumberListingFilterState)
            ?.value?.toInt() ?: params.storageMaxPendingUploadsFrom?.toInt()
        val storageMaxPendingUploadsTo = (filterStates?.get(UserFilterValues.STORAGE_MAX_PENDING_UPLOADS_TO) as? NumberListingFilterState)
            ?.value?.toInt() ?: params.storageMaxPendingUploadsTo?.toInt()
        val isStorageMaxPendingUploadsSet = (filterStates?.get(UserFilterValues.IS_STORAGE_MAX_PENDING_UPLOADS_SET) as? BooleanListingFilterState)
            ?.value ?: params.isStorageMaxPendingUploadsSet
        val isStorageConfigurationSet = (filterStates?.get(UserFilterValues.IS_STORAGE_CONFIGURATION_SET) as? BooleanListingFilterState)
            ?.value ?: params.isStorageConfigurationSet
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
        val birthdateFrom = (filterStates?.get(UserFilterValues.BIRTHDATE_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.birthdateFrom
        val birthdateTo = (filterStates?.get(UserFilterValues.BIRTHDATE_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.birthdateTo
        val isBirthdateSet = (filterStates?.get(UserFilterValues.IS_BIRTHDATE_SET) as? BooleanListingFilterState)
            ?.value ?: params.isBirthdateSet
        val searchQuery = (filterStates?.get(UserFilterValues.SEARCH_QUERY) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.searchQuery?.takeIf { it.isNotBlank() }

        scope.launch {
            getUsersUseCase(
                params = ManagementUserListParams(
                    pageNumber = pageNumber,
                    sortBy = sortBy,
                    sortOrder = sortOrder,
                    userIds = userIds ?: emptyList(),
                    userRoles = roles ?: emptyList(),
                    excludeUserRoles = excludeRoles ?: emptyList(),
                    accountStatuses = accountStatuses ?: emptyList(),
                    excludeAccountStatuses = excludeAccountStatuses ?: emptyList(),
                    accountStatusesOnRestore = accountStatusesOnRestore ?: emptyList(),
                    excludeAccountStatusesOnRestore = excludeAccountStatusesOnRestore ?: emptyList(),
                    isAccountStatusOnRestoreSet = isAccountStatusOnRestoreSet,
                    authorityLevelFrom = authorityLevelFrom,
                    authorityLevelTo = authorityLevelTo,
                    permissionCodes = permissionCodes?.map { it.value } ?: emptyList(),
                    excludePermissionCodes = excludePermissionCodes?.map { it.value } ?: emptyList(),
                    isTotpEnabled = isTotpEnabled,
                    lastLoginAtFrom = lastLoginAtFrom ?: "",
                    lastLoginAtTo = lastLoginAtTo ?: "",
                    isLastLoginAtSet = isLastLoginAtSet,
                    lastActiveAtFrom = lastActiveAtFrom ?: "",
                    lastActiveAtTo = lastActiveAtTo ?: "",
                    isLastActiveAtSet = isLastActiveAtSet,
                    createdAtFrom = createdAtFrom ?: "",
                    createdAtTo = createdAtTo ?: "",
                    updatedAtFrom = updatedAtFrom ?: "",
                    updatedAtTo = updatedAtTo ?: "",
                    isUpdatedAtSet = isUpdatedAtSet,
                    scheduledPermanentDeletionAtFrom = scheduledPermanentDeletionAtFrom ?: "",
                    scheduledPermanentDeletionAtTo = scheduledPermanentDeletionAtTo ?: "",
                    isScheduledPermanentDeletionAtSet = isScheduledPermanentDeletionAtSet,
                    accountLockoutTypes = accountLockoutTypes ?: emptyList(),
                    excludeAccountLockoutTypes = excludeAccountLockoutTypes ?: emptyList(),
                    temporaryLockoutUntilFrom = temporaryLockoutUntilFrom ?: "",
                    temporaryLockoutUntilTo = temporaryLockoutUntilTo ?: "",
                    isTemporaryLockoutUntilSet = isTemporaryLockoutUntilSet,
                    storageQuotaBytesFrom = storageQuotaBytesFrom,
                    storageQuotaBytesTo = storageQuotaBytesTo,
                    isStorageQuotaBytesSet = isStorageQuotaBytesSet,
                    storageMaxUploadSizeBytesFrom = storageMaxUploadSizeBytesFrom,
                    storageMaxUploadSizeBytesTo = storageMaxUploadSizeBytesTo,
                    isStorageMaxUploadSizeBytesSet = isStorageMaxUploadSizeBytesSet,
                    storageMaxPendingUploadsFrom = storageMaxPendingUploadsFrom?.toLong(),
                    storageMaxPendingUploadsTo = storageMaxPendingUploadsTo?.toLong(),
                    isStorageMaxPendingUploadsSet = isStorageMaxPendingUploadsSet,
                    isStorageConfigurationSet = isStorageConfigurationSet,
                    nickname = nickname ?: "",
                    isNicknameSet = isNicknameSet,
                    displayName = displayName ?: "",
                    isDisplayNameSet = isDisplayNameSet,
                    isAvatarSet = isAvatarSet,
                    birthdateFrom = birthdateFrom ?: "",
                    birthdateTo = birthdateTo ?: "",
                    isBirthdateSet = isBirthdateSet,
                    searchQuery = searchQuery ?: ""
                )
            )
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

    private fun createContent(
        currentContent: GlobalUserListScreenState.Content?,
        newPaging: PaginationState<UserSummary>,
        sortState: ListingSortState?,
        filterStates: Map<String, ListingFilterState>?
    ) = currentContent?.copy(paging = newPaging) ?: GlobalUserListScreenState.Content(
        paging = newPaging,
        sortState = sortState,
        filterStates = filterStates ?: emptyMap()
    )
}
