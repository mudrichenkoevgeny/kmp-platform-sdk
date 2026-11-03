package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.appendResult
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ChoiceListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditEventFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditEventSortBy
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.screen.management.audit.list.ManagementAuditEventListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import kotlinx.coroutines.launch

/**
 * Default [AuditEventListComponent] implementation managing administrative audit events list.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Navigation parameters containing filter state and page size.
 * @param getAuditEventsUseCase Use case to fetch paged audit logs.
 * @param onNavigateToAuditEventDetail Callback invoked when an audit event card is selected.
 * @param onBack Callback invoked to pop the screen.
 */
class AuditEventListComponentImpl(
    componentContext: ComponentContext,
    private val params: ManagementAuditEventListParams,
    private val getAuditEventsUseCase: GetAuditEventsUseCase,
    private val onNavigateToAuditEventDetail: (AuditEventId) -> Unit,
    private val onBack: () -> Unit
) : AuditEventListComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<AuditEventListScreenState>(AuditEventListScreenState.Loading)
    override val state: Value<AuditEventListScreenState> = _state

    init {
        fetchInitialPage()
    }

    override fun onRefresh() {
        val currentContent = _state.value as? AuditEventListScreenState.Content
        fetchPage(
            pageNumber = 1,
            sortState = currentContent?.sortState,
            filterStates = currentContent?.filterStates
        )
    }

    override fun onEventClick(eventId: AuditEventId) {
        onNavigateToAuditEventDetail(eventId)
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? AuditEventListScreenState.Content ?: return
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

    override fun onBackClick() {
        onBack()
    }

    override fun onToggleFilterPanel() {
        val currentContent = _state.value as? AuditEventListScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = !currentContent.isFilterPanelExpanded)
    }

    override fun onSortChanged(sortState: ListingSortState) {
        val currentContent = _state.value as? AuditEventListScreenState.Content ?: return
        _state.value = currentContent.copy(sortState = sortState)
        fetchPage(
            pageNumber = 1,
            sortState = sortState,
            filterStates = currentContent.filterStates
        )
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
        val currentContent = _state.value as? AuditEventListScreenState.Content ?: return
        val updatedFilters = currentContent.filterStates.toMutableMap()
        if (filterState != null) {
            updatedFilters[filterId] = filterState
        } else {
            updatedFilters.remove(filterId)
        }
        _state.value = currentContent.copy(filterStates = updatedFilters)
    }

    override fun onApplyFilters() {
        val currentContent = _state.value as? AuditEventListScreenState.Content ?: return
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
        val sortBy = if (sortState?.optionId == AuditEventSortBy.CREATED_AT.value) {
            AuditEventSortBy.CREATED_AT
        } else {
            params.sortBy
        }

        val eventIds = (filterStates?.get(AuditEventFilterValues.EVENT_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.eventIds.ifEmpty { null }
        val actorIds = (filterStates?.get(AuditEventFilterValues.ACTOR_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.actorIds.ifEmpty { null }
        val actorTypes = (filterStates?.get(AuditEventFilterValues.ACTOR_TYPE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { AuditActorType.fromValueOrNull(it) } ?: params.actorTypes.ifEmpty { null }
        val excludeActorTypes = (filterStates?.get(AuditEventFilterValues.EXCLUDE_ACTOR_TYPE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { AuditActorType.fromValueOrNull(it) } ?: params.excludeActorTypes.ifEmpty { null }
        val actorUserRoles = (filterStates?.get(AuditEventFilterValues.ACTOR_USER_ROLE) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { id -> UserRole.fromValueOrNull(id) } ?: params.actorUserRoles.mapNotNull { id -> UserRole.fromValueOrNull(id) }.ifEmpty { null }
        val actions = (filterStates?.get(AuditEventFilterValues.ACTION) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.actions.ifEmpty { null }
        val excludeActions = (filterStates?.get(AuditEventFilterValues.EXCLUDE_ACTION) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.excludeActions.ifEmpty { null }
        val resources = (filterStates?.get(AuditEventFilterValues.RESOURCE) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.resources.ifEmpty { null }
        val resourceIds = (filterStates?.get(AuditEventFilterValues.RESOURCE_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.resourceIds.ifEmpty { null }
        val statuses = (filterStates?.get(AuditEventFilterValues.STATUS) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { AuditStatus.fromValueOrNull(it) } ?: params.statuses.ifEmpty { null }
        val excludeStatuses = (filterStates?.get(AuditEventFilterValues.EXCLUDE_STATUS) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { AuditStatus.fromValueOrNull(it) } ?: params.excludeStatuses.ifEmpty { null }
        val createdAtFrom = (filterStates?.get(AuditEventFilterValues.CREATED_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.createdAtFrom?.takeIf { it.isNotBlank() }
        val createdAtTo = (filterStates?.get(AuditEventFilterValues.CREATED_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.createdAtTo?.takeIf { it.isNotBlank() }
        val searchQuery = (filterStates?.get(AuditEventFilterValues.SEARCH_QUERY) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.searchQuery?.takeIf { it.isNotBlank() }

        scope.launch {
            getAuditEventsUseCase(
                params = ManagementAuditEventListParams(
                    pageNumber = pageNumber,
                    pageSize = params.pageSize,
                    sortBy = sortBy,
                    sortOrder = sortOrder,
                    eventIds = eventIds ?: emptyList(),
                    actorIds = actorIds ?: emptyList(),
                    actorTypes = actorTypes ?: emptyList(),
                    excludeActorTypes = excludeActorTypes ?: emptyList(),
                    actorUserRoles = actorUserRoles?.map { it.value } ?: emptyList(),
                    actions = actions ?: emptyList(),
                    excludeActions = excludeActions ?: emptyList(),
                    resources = resources ?: emptyList(),
                    resourceIds = resourceIds ?: emptyList(),
                    statuses = statuses ?: emptyList(),
                    excludeStatuses = excludeStatuses ?: emptyList(),
                    createdAtFrom = createdAtFrom ?: "",
                    createdAtTo = createdAtTo ?: "",
                    searchQuery = searchQuery ?: ""
                )
            )
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? AuditEventListScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<AuditEventSummary>()).appendResult(pagedResult)
                    _state.value = createContent(currentContent, newPaging, sortState, filterStates)
                }
                .onError { error ->
                    val currentContent = _state.value as? AuditEventListScreenState.Content
                    if (currentContent != null) {
                        _state.value = currentContent.copy(
                            paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                        )
                    } else {
                        _state.value = AuditEventListScreenState.Error(error)
                    }
                }
        }
    }

    private fun createContent(
        currentContent: AuditEventListScreenState.Content?,
        newPaging: PaginationState<AuditEventSummary>,
        sortState: ListingSortState?,
        filterStates: Map<String, ListingFilterState>?
    ) = currentContent?.copy(paging = newPaging) ?: AuditEventListScreenState.Content(
        paging = newPaging,
        sortState = sortState,
        filterStates = filterStates ?: emptyMap()
    )
}
