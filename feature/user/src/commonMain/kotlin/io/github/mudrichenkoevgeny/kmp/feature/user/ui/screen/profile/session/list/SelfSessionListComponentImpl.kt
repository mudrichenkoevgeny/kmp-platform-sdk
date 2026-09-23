package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.ListingConstants
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.appendResult
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toInitialLoading
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toNextPageLoading
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteAllOtherSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrNull
import kotlinx.coroutines.launch

/**
 * Default [SelfSessionListComponent] implementation: handles listing and revoking active sessions for self user.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param getSessionsUseCase Fetches the current list of active sessions.
 * @param deleteSessionUseCase Terminates a specific remote session.
 * @param deleteAllOtherSessionsUseCase Terminates all sessions except the current one.
 * @param onNavigateToSessionDetail Optional callback to navigate to session detail screen.
 * @param onBack Pops this screen from the navigation stack.
 * @param authStorage Token storage used to resolve the current active session ID.
 */
class SelfSessionListComponentImpl(
    componentContext: ComponentContext,
    private val getSessionsUseCase: GetSessionsUseCase,
    private val deleteSessionUseCase: DeleteSessionUseCase,
    private val deleteAllOtherSessionsUseCase: DeleteAllOtherSessionsUseCase,
    private val onNavigateToSessionDetail: ((UserSession) -> Unit)? = null,
    private val onBack: () -> Unit,
    private val authStorage: AuthStorage? = null
) : SelfSessionListComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<SelfSessionListScreenState>(SelfSessionListScreenState.Loading)
    override val state: Value<SelfSessionListScreenState> = _state

    init {
        loadSessions()
    }

    override fun onRefresh() {
        loadSessions()
    }

    override fun onSessionClick(session: UserSession) {
        onNavigateToSessionDetail?.invoke(session)
    }

    override fun onSessionRevoked(sessionId: UserSessionId) {
        val current = _state.value as? SelfSessionListScreenState.Content ?: return
        val newItems = current.paging.items.filterNot { it.id == sessionId }
        _state.value = current.copy(
            paging = current.paging.copy(items = newItems)
        )
    }

    override fun onRevokeSessionClick(sessionId: UserSessionId) {
        val current = _state.value as? SelfSessionListScreenState.Content ?: return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            deleteSessionUseCase(sessionId)
                .onSuccess {
                    loadSessions()
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onRevokeAllOtherSessionsClick() {
        val current = _state.value as? SelfSessionListScreenState.Content ?: return
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            deleteAllOtherSessionsUseCase()
                .onSuccess {
                    loadSessions()
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onBackClick() {
        onBack()
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? SelfSessionListScreenState.Content ?: return
        val paging = currentContent.paging
        if (!paging.canLoadMore) return

        _state.value = currentContent.copy(paging = paging.toNextPageLoading())
        fetchPage(
            pageNumber = paging.nextPageNumber
        )
    }

    private fun loadSessions() {
        val currentContent = _state.value as? SelfSessionListScreenState.Content
        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionLoading = false,
                actionError = null
            )
        } else {
            _state.value = SelfSessionListScreenState.Loading
        }

        fetchPage(
            pageNumber = ListingConstants.INITIAL_PAGE_NUMBER
        )
    }

    private fun fetchPage(
        pageNumber: Int
    ) {
        scope.launch {
            getSessionsUseCase(
                pageNumber = pageNumber,
                pageSize = ListingConstants.DEFAULT_PAGE_SIZE,
                sortBy = null,
                sortOrder = SortOrder.DESC,
                userAuthProviders = null,
                clientTypes = null,
                ipAddresses = null,
                deviceNames = null
            )
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? SelfSessionListScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserSession>()).appendResult(pagedResult)
                    val currentSessionId = authStorage?.getSessionId()?.toUserSessionIdOrNull()
                    _state.value = currentContent?.copy(
                        paging = newPaging,
                        currentSessionId = currentSessionId
                    ) ?: SelfSessionListScreenState.Content(
                        paging = newPaging,
                        currentSessionId = currentSessionId
                    )
                }
                .onError { error ->
                    val currentContent = _state.value as? SelfSessionListScreenState.Content
                    if (currentContent != null) {
                        _state.value = currentContent.copy(
                            paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                        )
                    } else {
                        _state.value = SelfSessionListScreenState.Error(error)
                    }
                }
        }
    }
}
