package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import kotlinx.coroutines.launch

/**
 * Default [GlobalIdentifierListComponent] implementation: manages all identifiers list globally.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param managementGetIdentifiersUseCase Use case to fetch paginated list of global identifiers.
 * @param onIdentifierSelect Callback invoked when an identifier is tapped.
 * @param onBack Pops this screen from the navigation stack.
 */
class GlobalIdentifierListComponentImpl(
    componentContext: ComponentContext,
    private val managementGetIdentifiersUseCase: ManagementGetIdentifiersUseCase,
    private val onIdentifierSelect: ((String) -> Unit)? = null,
    private val onBack: () -> Unit
) : GlobalIdentifierListComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<GlobalIdentifierListScreenState>(GlobalIdentifierListScreenState.Loading)
    override val state: Value<GlobalIdentifierListScreenState> = _state

    init {
        loadIdentifiers()
    }

    override fun onRefresh() {
        loadIdentifiers()
    }

    override fun onIdentifierClick(identifierId: String) {
        onIdentifierSelect?.invoke(identifierId)
    }

    override fun onIdentifierDeleted(identifierId: UserIdentifierId) {
        val current = _state.value as? GlobalIdentifierListScreenState.Content ?: return
        val newItems = current.paging.items.filterNot { it.id == identifierId }
        _state.value = current.copy(
            paging = current.paging.copy(items = newItems)
        )
    }

    override fun onBackClick() {
        onBack()
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? GlobalIdentifierListScreenState.Content ?: return
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
        val currentContent = _state.value as? GlobalIdentifierListScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = !currentContent.isFilterPanelExpanded)
    }

    override fun onSortChanged(sortState: ListingSortState) {
        val currentContent = _state.value as? GlobalIdentifierListScreenState.Content ?: return
        _state.value = currentContent.copy(sortState = sortState)
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
        val currentContent = _state.value as? GlobalIdentifierListScreenState.Content ?: return
        val newFilterStates = currentContent.filterStates.toMutableMap()
        if (filterState == null) {
            newFilterStates.remove(filterId)
        } else {
            newFilterStates[filterId] = filterState
        }
        _state.value = currentContent.copy(filterStates = newFilterStates)
    }

    override fun onApplyFilters() {
        val currentContent = _state.value as? GlobalIdentifierListScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = false)
        loadIdentifiers()
    }

    private fun loadIdentifiers() {
        val currentContent = _state.value as? GlobalIdentifierListScreenState.Content
        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionLoading = false,
                actionError = null
            )
        } else {
            _state.value = GlobalIdentifierListScreenState.Loading
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
            UserSortValues.UserIdentifierSortBy.CREATED_AT
        } else {
            null
        }

        val userAuthProviders = (filterStates?.get(UserFilterValues.UserIdentifierFilterValues.USER_AUTH_PROVIDER) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAuthProvider.fromValueOrNull(it) }
        val identifiers = (filterStates?.get(UserFilterValues.UserIdentifierFilterValues.IDENTIFIER) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }

        scope.launch {
            managementGetIdentifiersUseCase(
                pageNumber = pageNumber,
                pageSize = ListingConstants.DEFAULT_PAGE_SIZE,
                sortBy = sortBy,
                sortOrder = sortOrder,
                userAuthProviders = userAuthProviders,
                identifiers = identifiers
            )
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? GlobalIdentifierListScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserIdentifier>()).appendResult(pagedResult)
                    _state.value = currentContent?.copy(paging = newPaging) ?: GlobalIdentifierListScreenState.Content(paging = newPaging)
                }
                .onError { error ->
                    val currentContent = _state.value as? GlobalIdentifierListScreenState.Content
                    if (currentContent != null) {
                        _state.value = currentContent.copy(
                            paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                        )
                    } else {
                        _state.value = GlobalIdentifierListScreenState.Error(error)
                    }
                }
        }
    }
}
