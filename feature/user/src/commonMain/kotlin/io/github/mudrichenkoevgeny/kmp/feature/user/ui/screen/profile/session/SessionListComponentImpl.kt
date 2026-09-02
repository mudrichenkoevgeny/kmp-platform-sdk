package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session

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
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteAllOtherSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import kotlinx.coroutines.launch

/**
 * Default [SessionListComponent] implementation: handles listing and revoking active sessions.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param getSessionsUseCase Fetches the current list of active sessions.
 * @param deleteSessionUseCase Terminates a specific remote session.
 * @param deleteAllOtherSessionsUseCase Terminates all sessions except the current one.
 * @param onBack Pops this screen from the navigation stack.
 */
class SessionListComponentImpl(
    componentContext: ComponentContext,
    private val getSessionsUseCase: GetSessionsUseCase,
    private val deleteSessionUseCase: DeleteSessionUseCase,
    private val deleteAllOtherSessionsUseCase: DeleteAllOtherSessionsUseCase,
    private val onBack: () -> Unit
) : SessionListComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<SessionListScreenState>(SessionListScreenState.Loading)
    override val state: Value<SessionListScreenState> = _state

    init {
        loadSessions()
    }

    override fun onRefresh() {
        loadSessions()
    }

    override fun onRevokeSessionClick(sessionId: UserSessionId) {
        val current = _state.value as? SessionListScreenState.Content ?: return
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
        val current = _state.value as? SessionListScreenState.Content ?: return
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
        val currentContent = _state.value as? SessionListScreenState.Content ?: return
        val paging = currentContent.paging
        if (!paging.canLoadMore) return

        _state.value = currentContent.copy(paging = paging.toNextPageLoading())
        loadPage(paging.pageNumber + 1)
    }

    private fun loadSessions() {
        val currentContent = _state.value as? SessionListScreenState.Content
        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionError = null
            )
        } else {
            _state.value = SessionListScreenState.Loading
        }

        loadPage(pageNumber = ListingConstants.INITIAL_PAGE_NUMBER)
    }

    private fun loadPage(pageNumber: Int) {
        scope.launch {
            getSessionsUseCase(pageNumber = pageNumber, pageSize = ListingConstants.DEFAULT_PAGE_SIZE)
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? SessionListScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserSession>()).appendResult(pagedResult)
                    _state.value = SessionListScreenState.Content(paging = newPaging)
                }
                .onError { error ->
                    val currentContent = _state.value as? SessionListScreenState.Content
                    if (currentContent != null) {
                        val isInitial = pageNumber == ListingConstants.INITIAL_PAGE_NUMBER
                        _state.value = currentContent.copy(
                            paging = currentContent.paging.toError(error, isInitial = isInitial)
                        )
                    } else {
                        _state.value = SessionListScreenState.Error(error)
                    }
                }
        }
    }
}
