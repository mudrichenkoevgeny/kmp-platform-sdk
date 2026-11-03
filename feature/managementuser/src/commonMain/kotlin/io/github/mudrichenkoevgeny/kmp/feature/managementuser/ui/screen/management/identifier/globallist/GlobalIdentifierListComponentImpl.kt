package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.appendResult
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.BooleanListingFilterState
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
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserIdentifierFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserIdentifierSortBy
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.globallist.ManagementGlobalIdentifierListParams
import kotlinx.coroutines.launch

/**
 * Default [GlobalIdentifierListComponent] implementation: manages all identifiers list globally.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Global identifiers list parameters.
 * @param managementGetIdentifiersUseCase Use case to fetch paginated list of global identifiers.
 * @param onIdentifierSelect Callback invoked when an identifier is tapped.
 * @param onBack Pops this screen from the navigation stack.
 */
class GlobalIdentifierListComponentImpl(
    componentContext: ComponentContext,
    private val params: ManagementGlobalIdentifierListParams = ManagementGlobalIdentifierListParams(),
    private val managementGetIdentifiersUseCase: ManagementGetIdentifiersUseCase,
    private val userRepository: UserRepository? = null,
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
        if ((backHandler as? BackDispatcher)?.back() != true) {
            onBack()
        }
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? GlobalIdentifierListScreenState.Content ?: return
        val paging = currentContent.paging
        if (!paging.canLoadMore) {
            return
        }

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
        loadIdentifiers()
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
            pageNumber = params.pageNumber,
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
        val sortBy = sortState?.optionId?.let { UserIdentifierSortBy.fromValueOrNull(it) } ?: params.sortBy

        val identifierIds = (filterStates?.get(UserIdentifierFilterValues.IDENTIFIER_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.identifierIds.ifEmpty { null }
        val userIds = (filterStates?.get(UserIdentifierFilterValues.USER_ID) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.userIds.ifEmpty { null }
        val userAuthProviders = (filterStates?.get(UserIdentifierFilterValues.USER_AUTH_PROVIDER) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAuthProvider.fromValueOrNull(it) } ?: params.userAuthProviders.ifEmpty { null }
        val excludeUserAuthProviders = (filterStates?.get(UserIdentifierFilterValues.EXCLUDE_USER_AUTH_PROVIDER) as? ChoiceListingFilterState)
            ?.selectedIds?.mapNotNull { UserAuthProvider.fromValueOrNull(it) } ?: params.excludeUserAuthProviders.ifEmpty { null }
        val identifiers = (filterStates?.get(UserIdentifierFilterValues.IDENTIFIER) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: params.identifier?.takeIf { it.isNotBlank() }?.let { listOf(it) }
        val displayName = (filterStates?.get(UserIdentifierFilterValues.DISPLAY_NAME) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.displayName
        val externalProviderEmail = (filterStates?.get(UserIdentifierFilterValues.EXTERNAL_PROVIDER_EMAIL) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.externalProviderEmail
        val isExternalProviderEmailSet = (filterStates?.get(UserIdentifierFilterValues.IS_EXTERNAL_PROVIDER_EMAIL_SET) as? BooleanListingFilterState)
            ?.value ?: params.isExternalProviderEmailSet
        val createdAtFrom = (filterStates?.get(UserIdentifierFilterValues.CREATED_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.createdAtFrom
        val createdAtTo = (filterStates?.get(UserIdentifierFilterValues.CREATED_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.createdAtTo
        val updatedAtFrom = (filterStates?.get(UserIdentifierFilterValues.UPDATED_AT_FROM) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.updatedAtFrom
        val updatedAtTo = (filterStates?.get(UserIdentifierFilterValues.UPDATED_AT_TO) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.updatedAtTo
        val isUpdatedAtSet = (filterStates?.get(UserIdentifierFilterValues.IS_UPDATED_AT_SET) as? BooleanListingFilterState)
            ?.value ?: params.isUpdatedAtSet
        val searchQuery = (filterStates?.get(UserIdentifierFilterValues.SEARCH_QUERY) as? TextListingFilterState)
            ?.value?.takeIf { it.isNotBlank() } ?: params.searchQuery?.takeIf { it.isNotBlank() }

        scope.launch {
            managementGetIdentifiersUseCase(
                params = ManagementGlobalIdentifierListParams(
                    pageNumber = pageNumber,
                    sortBy = sortBy,
                    sortOrder = sortOrder,
                    identifierIds = identifierIds ?: emptyList(),
                    userIds = userIds ?: emptyList(),
                    userAuthProviders = userAuthProviders ?: emptyList(),
                    excludeUserAuthProviders = excludeUserAuthProviders ?: emptyList(),
                    identifier = identifiers?.firstOrNull() ?: "",
                    displayName = displayName ?: "",
                    externalProviderEmail = externalProviderEmail ?: "",
                    isExternalProviderEmailSet = isExternalProviderEmailSet,
                    createdAtFrom = createdAtFrom ?: "",
                    createdAtTo = createdAtTo ?: "",
                    updatedAtFrom = updatedAtFrom ?: "",
                    updatedAtTo = updatedAtTo ?: "",
                    isUpdatedAtSet = isUpdatedAtSet,
                    searchQuery = searchQuery ?: ""
                )
            )
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? GlobalIdentifierListScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserIdentifierSummary>()).appendResult(pagedResult)
                    val currentUserId = userRepository?.currentUser?.firstOrNull()?.id
                    _state.value = currentContent?.copy(
                        paging = newPaging,
                        currentUserId = currentUserId
                    ) ?: GlobalIdentifierListScreenState.Content(
                        paging = newPaging,
                        currentUserId = currentUserId
                    )
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