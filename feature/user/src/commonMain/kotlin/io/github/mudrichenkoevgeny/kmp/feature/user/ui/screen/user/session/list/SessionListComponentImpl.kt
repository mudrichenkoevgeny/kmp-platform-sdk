package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.doOnCreate
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.appendResult
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toInitialLoading
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toNextPageLoading
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.datasource.SessionListDataSource
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.datasource.SessionListQuery
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.ListingDefaultValues
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * Universal Decompose component implementation for active sessions listing screens (client & management).
 *
 * @param componentContext Decompose [ComponentContext].
 * @param dataSource Data source contract for session fetch and revocation operations.
 * @param includeFilterPanel Whether filter panel features are included (administrative mode).
 * @param initialUserId Optional target user ID for scope-constrained queries.
 * @param authStorage Token storage used to resolve current active session ID.
 * @param userRepository Optional user repository to resolve current user ID.
 * @param onNavigateToSessionDetail Optional callback to navigate to session detail.
 * @param onBack Pops this screen from the navigation stack.
 */
public class SessionListComponentImpl(
    componentContext: ComponentContext,
    private val dataSource: SessionListDataSource,
    override val includeFilterPanel: Boolean = false,
    private val initialUserId: String? = null,
    private val authStorage: AuthStorage? = null,
    private val userRepository: UserRepository? = null,
    private val onNavigateToSessionDetail: ((UserSessionSummary) -> Unit)? = null,
    private val onBack: () -> Unit
) : SessionListComponent, ComponentContext by componentContext {

    public override val isRevokeAvailable: Boolean
        get() = dataSource.isRevokeAvailable

    public override val isRevokeAllOthersAvailable: Boolean
        get() = dataSource.isRevokeAllOthersAvailable

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<SessionListScreenState>(SessionListScreenState.Loading)
    public override val state: Value<SessionListScreenState> = _state

    private var activeSortState: ListingSortState? = null
    private var activeFilterStates: Map<String, ListingFilterState> = emptyMap()
    private var isFilterPanelExpanded: Boolean = false

    init {
        lifecycle.doOnCreate {
            initScreen()
        }
    }

    private fun initScreen() {
        if (_state.value is SessionListScreenState.Content) {
            return
        }
        _state.value = SessionListScreenState.Loading
        fetchPage(pageNumber = ListingDefaultValues.Pagination.INITIAL_PAGE_NUMBER)
    }

    public override fun onBackClick() {
        if ((backHandler as? BackDispatcher)?.back() != true) {
            onBack()
        }
    }

    public override fun onRefresh() {
        _state.value = SessionListScreenState.Loading
        fetchPage(pageNumber = ListingDefaultValues.Pagination.INITIAL_PAGE_NUMBER)
    }

    public override fun onLoadNextPage() {
        val currentContent = _state.value as? SessionListScreenState.Content ?: return
        val paging = currentContent.paging
        if (!paging.canLoadMore) {
            return
        }

        _state.value = currentContent.copy(paging = paging.toNextPageLoading())
        fetchPage(pageNumber = paging.nextPageNumber)
    }

    public override fun onToggleFilterPanel() {
        val currentContent = _state.value as? SessionListScreenState.Content ?: return
        isFilterPanelExpanded = !isFilterPanelExpanded
        _state.value = currentContent.copy(isFilterPanelExpanded = isFilterPanelExpanded)
    }

    public override fun onSortChanged(sortState: ListingSortState) {
        activeSortState = sortState
        val currentContent = _state.value as? SessionListScreenState.Content ?: return
        _state.value = currentContent.copy(sortState = sortState)
        onRefresh()
    }

    public override fun onFilterChanged(
        filterId: String,
        filterState: ListingFilterState?
    ) {
        val newFilters = activeFilterStates.toMutableMap()
        if (filterState == null) {
            newFilters.remove(filterId)
        } else {
            newFilters[filterId] = filterState
        }
        activeFilterStates = newFilters

        val currentContent = _state.value as? SessionListScreenState.Content ?: return
        _state.value = currentContent.copy(filterStates = newFilters)
    }

    public override fun onApplyFilters() {
        isFilterPanelExpanded = false
        val currentContent = _state.value as? SessionListScreenState.Content
        if (currentContent != null) {
            _state.value = currentContent.copy(isFilterPanelExpanded = false)
        }
        onRefresh()
    }

    public override fun onSessionClick(session: UserSessionSummary) {
        onNavigateToSessionDetail?.invoke(session)
    }

    public override fun onSessionRevoked(sessionId: UserSessionId) {
        val current = _state.value as? SessionListScreenState.Content ?: return
        val newItems = current.paging.items.filterNot { it.id == sessionId }
        _state.value = current.copy(
            paging = current.paging.copy(items = newItems)
        )
    }

    public override fun onRevokeSessionClick(sessionId: UserSessionId) {
        val current = _state.value as? SessionListScreenState.Content ?: return
        if (!isRevokeAvailable || current.actionLoading) {
            return
        }

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            dataSource.revokeSession(
                sessionId = sessionId.asHexDashString(),
                userId = initialUserId
            )
                .onSuccess {
                    val newItems = current.paging.items.filterNot { it.id == sessionId }
                    _state.value = current.copy(
                        paging = current.paging.copy(items = newItems),
                        actionLoading = false,
                        actionError = null
                    )
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    public override fun onRevokeAllOtherSessionsClick() {
        val current = _state.value as? SessionListScreenState.Content ?: return
        if (!isRevokeAllOthersAvailable || current.actionLoading) {
            return
        }

        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            dataSource.revokeAllOtherSessions(userId = initialUserId)
                .onSuccess {
                    onRefresh()
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    private fun fetchPage(pageNumber: Int) {
        scope.launch {
            val query = SessionListQuery(
                pageNumber = pageNumber,
                sortState = activeSortState,
                filterStates = activeFilterStates,
                targetUserId = initialUserId
            )

            val currentContent = _state.value as? SessionListScreenState.Content

            if (currentContent != null && pageNumber == ListingDefaultValues.Pagination.INITIAL_PAGE_NUMBER) {
                _state.value = currentContent.copy(
                    paging = currentContent.paging.toInitialLoading(),
                    actionLoading = false,
                    actionError = null
                )
            }

            dataSource.getSessions(query)
                .onSuccess { pagedResult ->
                    val latestContent = _state.value as? SessionListScreenState.Content
                    val newPaging =
                        (latestContent?.paging ?: PaginationState<UserSessionSummary>()).appendResult(pagedResult)
                    val activeSessionId = authStorage?.getSessionId()
                    val currentUserId = userRepository?.currentUser?.firstOrNull()?.id
                    _state.value = latestContent?.copy(
                        paging = newPaging,
                        currentSessionId = activeSessionId,
                        currentUserId = currentUserId,
                        isFilterPanelExpanded = isFilterPanelExpanded,
                        sortState = activeSortState,
                        filterStates = activeFilterStates,
                        actionLoading = false,
                        actionError = null
                    ) ?: SessionListScreenState.Content(
                        paging = newPaging,
                        currentSessionId = activeSessionId,
                        currentUserId = currentUserId,
                        isFilterPanelExpanded = isFilterPanelExpanded,
                        sortState = activeSortState,
                        filterStates = activeFilterStates
                    )
                }
                .onError { error ->
                    val latestContent = _state.value as? SessionListScreenState.Content
                    if (latestContent != null) {
                        _state.value = latestContent.copy(
                            paging = latestContent.paging.toError(error, pageNumber = pageNumber),
                            actionLoading = false
                        )
                    } else {
                        _state.value = SessionListScreenState.Error(error)
                    }
                }
        }
    }
}
