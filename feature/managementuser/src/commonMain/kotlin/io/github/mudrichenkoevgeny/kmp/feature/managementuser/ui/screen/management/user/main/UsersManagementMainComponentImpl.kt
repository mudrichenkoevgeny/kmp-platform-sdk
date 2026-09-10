package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.ListingConstants
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.appendResult
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.NumberListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toInitialLoading
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toNextPageLoading
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUsersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.launch

class UsersManagementMainComponentImpl(
    componentContext: ComponentContext,
    private val getUsersUseCase: GetUsersUseCase,
    private val onNavigateToUserDetail: (UserId) -> Unit,
    private val onNavigateToCreateUser: () -> Unit,
    private val onBack: () -> Unit
) : UsersManagementMainComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<UsersManagementMainScreenState>(UsersManagementMainScreenState.Loading)
    override val state: Value<UsersManagementMainScreenState> = _state

    init {
        loadUsers()
    }

    override fun onRefresh() {
        loadUsers()
    }

    override fun onUserClick(userId: UserId) {
        onNavigateToUserDetail(userId)
    }

    override fun onCreateUserClick() {
        onNavigateToCreateUser()
    }

    override fun onBackClick() {
        onBack()
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? UsersManagementMainScreenState.Content ?: return
        val paging = currentContent.paging
        if (!paging.canLoadMore) return

        _state.value = currentContent.copy(paging = paging.toNextPageLoading())
        fetchPage(
            pageNumber = paging.nextPageNumber,
            sortState = currentContent.sortState,
            filterStates = currentContent.filterStates
        )
    }

    override fun onToggleFilterPanel() {
        val currentContent = _state.value as? UsersManagementMainScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = !currentContent.isFilterPanelExpanded)
    }

    override fun onSortChanged(sortState: ListingSortState) {
        val currentContent = _state.value as? UsersManagementMainScreenState.Content ?: return
        _state.value = currentContent.copy(sortState = sortState)
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
        val currentContent = _state.value as? UsersManagementMainScreenState.Content ?: return
        val newFilterStates = currentContent.filterStates.toMutableMap()
        if (filterState == null) {
            newFilterStates.remove(filterId)
        } else {
            newFilterStates[filterId] = filterState
        }
        _state.value = currentContent.copy(filterStates = newFilterStates)
    }

    override fun onApplyFilters() {
        val currentContent = _state.value as? UsersManagementMainScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = false)
        loadUsers()
    }

    private fun loadUsers() {
        val currentContent = _state.value as? UsersManagementMainScreenState.Content
        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionError = null
            )
        } else {
            _state.value = UsersManagementMainScreenState.Loading
        }

        fetchPage(
            pageNumber = ListingConstants.INITIAL_PAGE_NUMBER,
            sortState = currentContent?.sortState,
            filterStates = currentContent?.filterStates
        )
    }

    private fun fetchPage(
        pageNumber: Int,
        sortState: ListingSortState?,
        filterStates: Map<String, ListingFilterState>?
    ) {
        val sortOrder = if (sortState?.isAscending == true) SortOrder.ASC else SortOrder.DESC
        val sortBy = if (sortState?.optionId == "created_at") {
            UserSortValues.UserSortBy.CREATED_AT
        } else {
            null
        }

        val roles = (filterStates?.get(UserFilterValues.UserFilterValues.ROLE) as? ChoiceListingFilterState)
            ?.selectedIds?.map { UserRole.valueOf(it) }
        val accountStatuses = (filterStates?.get(UserFilterValues.UserFilterValues.ACCOUNT_STATUS) as? ChoiceListingFilterState)
            ?.selectedIds?.map { UserAccountStatus.valueOf(it) }
        val isTotpEnabled = (filterStates?.get(UserFilterValues.UserFilterValues.IS_TOTP_ENABLED) as? BooleanListingFilterState)
            ?.value
        val authorityLevelFrom = (filterStates?.get(UserFilterValues.UserFilterValues.AUTHORITY_LEVEL_FROM) as? NumberListingFilterState)
            ?.value?.toInt()
        val authorityLevelTo = (filterStates?.get(UserFilterValues.UserFilterValues.AUTHORITY_LEVEL_TO) as? NumberListingFilterState)
            ?.value?.toInt()

        scope.launch {
            getUsersUseCase(
                pageNumber = pageNumber,
                pageSize = ListingConstants.DEFAULT_PAGE_SIZE,
                sortBy = sortBy,
                sortOrder = sortOrder,
                roles = roles,
                accountStatuses = accountStatuses,
                isTotpEnabled = isTotpEnabled,
                authorityLevelFrom = authorityLevelFrom,
                authorityLevelTo = authorityLevelTo
            )
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? UsersManagementMainScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserDetails>()).appendResult(pagedResult)
                    _state.value = currentContent?.copy(paging = newPaging) ?: UsersManagementMainScreenState.Content(paging = newPaging)
                }
                .onError { error ->
                    val currentContent = _state.value as? UsersManagementMainScreenState.Content
                    if (currentContent != null) {
                        _state.value = currentContent.copy(
                            paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                        )
                    } else {
                        _state.value = UsersManagementMainScreenState.Error(error)
                    }
                }
        }
    }
}
