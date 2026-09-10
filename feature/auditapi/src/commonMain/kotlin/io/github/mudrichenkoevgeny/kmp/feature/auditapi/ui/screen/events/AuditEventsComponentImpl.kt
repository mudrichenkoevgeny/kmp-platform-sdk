package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.events

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
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.usecase.GetAuditEventsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import kotlinx.coroutines.launch

/**
 * Default implementation of [AuditEventsComponent].
 *
 * @param componentContext Decompose [ComponentContext].
 * @param getAuditEventsUseCase Fetches paginated audit events.
 * @param onNavigateToEventDetail Navigates to audit event detail screen.
 * @param onBack Pops this screen from the navigation stack.
 */
class AuditEventsComponentImpl(
    componentContext: ComponentContext,
    private val getAuditEventsUseCase: GetAuditEventsUseCase,
    private val onNavigateToEventDetail: (AuditEventId) -> Unit,
    private val onBack: () -> Unit
) : AuditEventsComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<AuditEventsScreenState>(AuditEventsScreenState.Loading)
    override val state: Value<AuditEventsScreenState> = _state

    init {
        loadEvents()
    }

    override fun onRefresh() {
        loadEvents()
    }

    override fun onEventClick(eventId: AuditEventId) {
        onNavigateToEventDetail(eventId)
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? AuditEventsScreenState.Content ?: return
        val paging = currentContent.paging
        if (!paging.canLoadMore) return

        _state.value = currentContent.copy(paging = paging.toNextPageLoading())
        fetchPage(
            pageNumber = paging.nextPageNumber,
            sortState = currentContent.sortState,
            filterStates = currentContent.filterStates
        )
    }

    override fun onBackClick() {
        onBack()
    }

    override fun onToggleFilterPanel() {
        val currentContent = _state.value as? AuditEventsScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = !currentContent.isFilterPanelExpanded)
    }

    override fun onSortChanged(sortState: ListingSortState) {
        val currentContent = _state.value as? AuditEventsScreenState.Content ?: return
        _state.value = currentContent.copy(sortState = sortState)
        loadEvents()
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
        val currentContent = _state.value as? AuditEventsScreenState.Content ?: return
        val newFilterStates = currentContent.filterStates.toMutableMap()
        if (filterState == null) {
            newFilterStates.remove(filterId)
        } else {
            newFilterStates[filterId] = filterState
        }
        _state.value = currentContent.copy(filterStates = newFilterStates)
    }

    override fun onApplyFilters() {
        val currentContent = _state.value as? AuditEventsScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = false)
        loadEvents()
    }

    private fun loadEvents() {
        val content = _state.value as? AuditEventsScreenState.Content
        if (content != null) {
            _state.value = content.copy(
                paging = content.paging.toInitialLoading(),
                actionError = null
            )
        } else {
            _state.value = AuditEventsScreenState.Loading
        }

        fetchPage(
            pageNumber = ListingConstants.INITIAL_PAGE_NUMBER,
            sortState = content?.sortState,
            filterStates = content?.filterStates
        )
    }

    private fun fetchPage(
        pageNumber: Int,
        sortState: ListingSortState?,
        filterStates: Map<String, ListingFilterState>?
    ) {
        val sortOrder = if (sortState?.isAscending == true) SortOrder.ASC else SortOrder.DESC
        val sortBy = if (sortState?.optionId == AuditSortValues.AuditEventSortBy.CREATED_AT.serialName) {
            AuditSortValues.AuditEventSortBy.CREATED_AT
        } else {
            null
        }

        val actorIds = (filterStates?.get(AuditFilterValues.AuditEventFilterValues.ACTOR_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val actorTypes = (filterStates?.get(AuditFilterValues.AuditEventFilterValues.ACTOR_TYPE) as? ChoiceListingFilterState)
            ?.selectedIds?.map { AuditActorType.valueOf(it.uppercase()) }
        val actorUserRoles = (filterStates?.get(AuditFilterValues.AuditEventFilterValues.ACTOR_USER_ROLE) as? ChoiceListingFilterState)
            ?.selectedIds?.map { UserRole.valueOf(it.uppercase()) }
        val actions = (filterStates?.get(AuditFilterValues.AuditEventFilterValues.ACTION) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val resources = (filterStates?.get(AuditFilterValues.AuditEventFilterValues.RESOURCE) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val resourceIds = (filterStates?.get(AuditFilterValues.AuditEventFilterValues.RESOURCE_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val statuses = (filterStates?.get(AuditFilterValues.AuditEventFilterValues.STATUS) as? ChoiceListingFilterState)
            ?.selectedIds?.map { AuditStatus.valueOf(it.uppercase()) }
        val messages = (filterStates?.get(AuditFilterValues.AuditEventFilterValues.MESSAGE) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }

        scope.launch {
            getAuditEventsUseCase(
                pageNumber = pageNumber,
                pageSize = ListingConstants.DEFAULT_PAGE_SIZE,
                sortBy = sortBy,
                sortOrder = sortOrder,
                actorIds = actorIds,
                actorTypes = actorTypes,
                actorUserRoles = actorUserRoles,
                actions = actions,
                resources = resources,
                resourceIds = resourceIds,
                statuses = statuses,
                messages = messages
            ).onSuccess { pagedResult ->
                val currentContent = _state.value as? AuditEventsScreenState.Content
                val newPaging = (currentContent?.paging ?: PaginationState<AuditEvent>()).appendResult(pagedResult)
                _state.value = currentContent?.copy(paging = newPaging) ?: AuditEventsScreenState.Content(paging = newPaging)
            }.onError { error ->
                val currentContent = _state.value as? AuditEventsScreenState.Content
                if (currentContent != null) {
                    _state.value = currentContent.copy(
                        paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                    )
                } else {
                    _state.value = AuditEventsScreenState.Error(error)
                }
            }
        }
    }
}
