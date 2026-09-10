package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.identifiers

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
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.launch

class UserIdentifiersComponentImpl(
    componentContext: ComponentContext,
    private val userId: UserId,
    private val managementGetIdentifiersUseCase: ManagementGetIdentifiersUseCase,
    private val onBack: () -> Unit
) : UserIdentifiersComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<UserIdentifiersScreenState>(UserIdentifiersScreenState.Loading)
    override val state: Value<UserIdentifiersScreenState> = _state

    init {
        loadIdentifiers()
    }

    override fun onRefresh() {
        loadIdentifiers()
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? UserIdentifiersScreenState.Content ?: return
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
        val currentContent = _state.value as? UserIdentifiersScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = !currentContent.isFilterPanelExpanded)
    }

    override fun onSortChanged(sortState: ListingSortState) {
        val currentContent = _state.value as? UserIdentifiersScreenState.Content ?: return
        _state.value = currentContent.copy(sortState = sortState)
    }

    override fun onFilterChanged(filterId: String, filterState: ListingFilterState?) {
        val currentContent = _state.value as? UserIdentifiersScreenState.Content ?: return
        val newFilterStates = currentContent.filterStates.toMutableMap()
        if (filterState == null) {
            newFilterStates.remove(filterId)
        } else {
            newFilterStates[filterId] = filterState
        }
        _state.value = currentContent.copy(filterStates = newFilterStates)
    }

    override fun onApplyFilters() {
        val currentContent = _state.value as? UserIdentifiersScreenState.Content ?: return
        _state.value = currentContent.copy(isFilterPanelExpanded = false)
        loadIdentifiers()
    }

    private fun loadIdentifiers() {
        val currentContent = _state.value as? UserIdentifiersScreenState.Content
        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionError = null
            )
        } else {
            _state.value = UserIdentifiersScreenState.Loading
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
            ?.selectedIds?.map { UserAuthProvider.valueOf(it.uppercase()) }
        val identifiers = (filterStates?.get(UserFilterValues.UserIdentifierFilterValues.IDENTIFIER) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) }

        scope.launch {
            managementGetIdentifiersUseCase(
                pageNumber = pageNumber,
                pageSize = ListingConstants.DEFAULT_PAGE_SIZE,
                userIds = listOf(userId.value.toString()),
                sortBy = sortBy,
                sortOrder = sortOrder,
                userAuthProviders = userAuthProviders,
                identifiers = identifiers
            ).onSuccess { pagedResult ->
                val currentContent = _state.value as? UserIdentifiersScreenState.Content
                val newPaging = (currentContent?.paging ?: PaginationState<UserIdentifier>()).appendResult(pagedResult)
                _state.value = currentContent?.copy(paging = newPaging) ?: UserIdentifiersScreenState.Content(paging = newPaging)
            }.onError { error ->
                val currentContent = _state.value as? UserIdentifiersScreenState.Content
                if (currentContent != null) {
                    _state.value = currentContent.copy(
                        paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                    )
                } else {
                    _state.value = UserIdentifiersScreenState.Error(error)
                }
            }
        }
    }
}
