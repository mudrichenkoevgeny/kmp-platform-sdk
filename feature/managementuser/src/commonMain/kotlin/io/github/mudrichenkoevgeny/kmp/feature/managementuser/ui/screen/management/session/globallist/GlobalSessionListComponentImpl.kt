package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.launch

/**
 * Default [GlobalSessionListComponent] implementation: manages all sessions list globally.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param managementGetSessionsUseCase Use case to fetch paginated list of global sessions.
 * @param managementDeleteSessionUseCase Use case to revoke a global session.
 * @param onNavigateToSessionDetail Callback invoked when a session is tapped.
 * @param onBack Pops this screen from the navigation stack.
 */
class GlobalSessionListComponentImpl(
    componentContext: ComponentContext,
    private val managementGetSessionsUseCase: ManagementGetSessionsUseCase,
    private val managementDeleteSessionUseCase: ManagementDeleteSessionUseCase,
    private val onNavigateToSessionDetail: ((UserSession) -> Unit)? = null,
    private val onBack: () -> Unit
) : GlobalSessionListComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<GlobalSessionListScreenState>(GlobalSessionListScreenState.Loading)
    override val state: Value<GlobalSessionListScreenState> = _state

    init {
        loadSessions()
    }

    override fun onRefresh() {
        loadSessions()
    }

    override fun onSessionClick(session: UserSession) {
        onNavigateToSessionDetail?.invoke(session)
    }

    override fun onDeleteSessionClick(userId: UserId, sessionId: String) {
        val current = _state.value as? GlobalSessionListScreenState.Content ?: return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            managementDeleteSessionUseCase(userId, sessionId)
                .onSuccess {
                    val newItems = current.paging.items.filterNot { it.id.asHexDashString() == sessionId }
                    _state.value = current.copy(
                        paging = current.paging.copy(items = newItems),
                        actionLoading = false
                    )
                }
                .onError { error ->
                    _state.value = current.copy(
                        actionLoading = false,
                        actionError = error
                    )
                }
        }
    }

    override fun onSessionRevoked(sessionId: UserSessionId) {
        val current = _state.value as? GlobalSessionListScreenState.Content ?: return
        val newItems = current.paging.items.filterNot { it.id == sessionId }
        _state.value = current.copy(
            paging = current.paging.copy(items = newItems)
        )
    }

    override fun onBackClick() {
        onBack()
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? GlobalSessionListScreenState.Content ?: return
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
        val currentContent = _state.value as? GlobalSessionListScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = !currentContent.isFilterPanelExpanded)
    }

    override fun onSortChanged(sortState: ListingSortState) {
        val currentContent = _state.value as? GlobalSessionListScreenState.Content ?: return
        _state.value = currentContent.copy(sortState = sortState)
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
        val currentContent = _state.value as? GlobalSessionListScreenState.Content ?: return
        val newFilterStates = currentContent.filterStates.toMutableMap()
        if (filterState == null) {
            newFilterStates.remove(filterId)
        } else {
            newFilterStates[filterId] = filterState
        }
        _state.value = currentContent.copy(filterStates = newFilterStates)
    }

    override fun onApplyFilters() {
        val currentContent = _state.value as? GlobalSessionListScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = false)
        loadSessions()
    }

    private fun loadSessions() {
        val currentContent = _state.value as? GlobalSessionListScreenState.Content
        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionLoading = false,
                actionError = null
            )
        } else {
            _state.value = GlobalSessionListScreenState.Loading
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
        val sortBy = sortState?.optionId?.let { UserSortValues.UserSessionSortBy.fromValueOrNull(it) }

        val userIds = (filterStates?.get(UserFilterValues.UserSessionFilterValues.USER_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val userRoles = (filterStates?.get(UserFilterValues.UserSessionFilterValues.USER_ROLE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserRole.fromValueOrNull(it) }
        val identifiers = (filterStates?.get(UserFilterValues.UserSessionFilterValues.IDENTIFIER) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val identifierIds = (filterStates?.get(UserFilterValues.UserSessionFilterValues.IDENTIFIER_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val userAuthProviders = (filterStates?.get(UserFilterValues.UserSessionFilterValues.USER_AUTH_PROVIDER) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAuthProvider.fromValueOrNull(it) }
        val clientTypes = (filterStates?.get(UserFilterValues.UserSessionFilterValues.CLIENT_TYPE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { ClientType.fromValueOrNull(it) }
        val userAgents = (filterStates?.get(UserFilterValues.UserSessionFilterValues.USER_AGENT) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val ipAddresses = (filterStates?.get(UserFilterValues.UserSessionFilterValues.IP_ADDRESS) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val languages = (filterStates?.get(UserFilterValues.UserSessionFilterValues.LANGUAGE) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val deviceIds = (filterStates?.get(UserFilterValues.UserSessionFilterValues.DEVICE_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val deviceNames = (filterStates?.get(UserFilterValues.UserSessionFilterValues.DEVICE_NAME) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val appVersions = (filterStates?.get(UserFilterValues.UserSessionFilterValues.APP_VERSION) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val operationSystemVersions = (filterStates?.get(UserFilterValues.UserSessionFilterValues.OPERATION_SYSTEM_VERSION) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }

        scope.launch {
            managementGetSessionsUseCase(
                pageNumber = pageNumber,
                pageSize = ListingConstants.DEFAULT_PAGE_SIZE,
                sortBy = sortBy,
                sortOrder = sortOrder,
                userIds = userIds,
                userRoles = userRoles,
                identifiers = identifiers,
                identifierIds = identifierIds,
                userAuthProviders = userAuthProviders,
                clientTypes = clientTypes,
                userAgents = userAgents,
                ipAddresses = ipAddresses,
                languages = languages,
                deviceIds = deviceIds,
                deviceNames = deviceNames,
                appVersions = appVersions,
                operationSystemVersions = operationSystemVersions
            )
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? GlobalSessionListScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserSession>()).appendResult(pagedResult)
                    _state.value = currentContent?.copy(paging = newPaging) ?: GlobalSessionListScreenState.Content(paging = newPaging)
                }
                .onError { error ->
                    val currentContent = _state.value as? GlobalSessionListScreenState.Content
                    if (currentContent != null) {
                        _state.value = currentContent.copy(
                            paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                        )
                    } else {
                        _state.value = GlobalSessionListScreenState.Error(error)
                    }
                }
        }
    }
}