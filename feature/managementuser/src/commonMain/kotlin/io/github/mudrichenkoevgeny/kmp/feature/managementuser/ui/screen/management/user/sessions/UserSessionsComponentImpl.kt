package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.sessions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.ListingConstants
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.appendResult
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toInitialLoading
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toNextPageLoading
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.launch

class UserSessionsComponentImpl(
    componentContext: ComponentContext,
    private val userId: UserId? = null,
    private val managementGetSessionsUseCase: ManagementGetSessionsUseCase,
    private val onBack: () -> Unit
) : UserSessionsComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<UserSessionsScreenState>(UserSessionsScreenState.Loading)
    override val state: Value<UserSessionsScreenState> = _state

    init {
        loadSessions()
    }

    override fun onRefresh() {
        loadSessions()
    }

    override fun onBackClick() {
        onBack()
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? UserSessionsScreenState.Content ?: return
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
        val currentContent = _state.value as? UserSessionsScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = !currentContent.isFilterPanelExpanded)
    }

    override fun onSortChanged(sortState: ListingSortState) {
        val currentContent = _state.value as? UserSessionsScreenState.Content ?: return
        _state.value = currentContent.copy(sortState = sortState)
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
        val currentContent = _state.value as? UserSessionsScreenState.Content ?: return
        val newFilterStates = currentContent.filterStates.toMutableMap()
        if (filterState == null) {
            newFilterStates.remove(filterId)
        } else {
            newFilterStates[filterId] = filterState
        }
        _state.value = currentContent.copy(filterStates = newFilterStates)
    }

    override fun onApplyFilters() {
        val currentContent = _state.value as? UserSessionsScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = false)
        loadSessions()
    }

    private fun loadSessions() {
        val currentContent = _state.value as? UserSessionsScreenState.Content
        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionError = null
            )
        } else {
            _state.value = UserSessionsScreenState.Loading
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
            UserSortValues.UserSessionSortBy.CREATED_AT
        } else {
            null
        }

        val userRoles = (filterStates?.get(UserFilterValues.UserSessionFilterValues.USER_ROLE) as? ChoiceListingFilterState)
            ?.selectedIds?.map { UserRole.valueOf(it) }
        val userAuthProviders = (filterStates?.get(UserFilterValues.UserSessionFilterValues.USER_AUTH_PROVIDER) as? ChoiceListingFilterState)
            ?.selectedIds?.map { UserAuthProvider.valueOf(it.uppercase()) }
        val clientTypes = (filterStates?.get(UserFilterValues.UserSessionFilterValues.CLIENT_TYPE) as? ChoiceListingFilterState)
            ?.selectedIds?.map { ClientType.valueOf(it.uppercase()) }
        val ipAddresses = (filterStates?.get(UserFilterValues.UserSessionFilterValues.IP_ADDRESS) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val userAgents = (filterStates?.get(UserFilterValues.UserSessionFilterValues.USER_AGENT) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val deviceNames = (filterStates?.get(UserFilterValues.UserSessionFilterValues.DEVICE_NAME) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }

        scope.launch {
            managementGetSessionsUseCase(
                pageNumber = pageNumber,
                pageSize = ListingConstants.DEFAULT_PAGE_SIZE,
                userIds = userId?.let { listOf(it.value.toString()) },
                sortBy = sortBy,
                sortOrder = sortOrder,
                userRoles = userRoles,
                userAuthProviders = userAuthProviders,
                clientTypes = clientTypes,
                ipAddresses = ipAddresses,
                userAgents = userAgents,
                deviceNames = deviceNames
            )
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? UserSessionsScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserSession>()).appendResult(pagedResult)
                    _state.value = currentContent?.copy(paging = newPaging) ?: UserSessionsScreenState.Content(paging = newPaging)
                }
                .onError { error ->
                    val currentContent = _state.value as? UserSessionsScreenState.Content
                    if (currentContent != null) {
                        _state.value = currentContent.copy(
                            paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                        )
                    } else {
                        _state.value = UserSessionsScreenState.Error(error)
                    }
                }
        }
    }
}
