package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.ListingConstants
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.appendResult
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toInitialLoading
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.toNextPageLoading
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteAllUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.userlist.ManagementUserSessionListParams
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * Default [UserSessionListComponent] implementation: manages sessions list for a specific user.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Screen destination parameters.
 * @param managementGetUserSessionsUseCase Use case to fetch paginated list of sessions for the user.
 * @param managementDeleteSessionUseCase Use case to administratively delete a specific session.
 * @param managementDeleteAllUserSessionsUseCase Use case to administratively delete all sessions for the user.
 * @param onNavigateToSessionDetail Callback invoked when a session is tapped.
 * @param onBack Pops this screen from the navigation stack.
 */
class UserSessionListComponentImpl(
    componentContext: ComponentContext,
    private val params: ManagementUserSessionListParams? = null,
    private val managementGetUserSessionsUseCase: ManagementGetUserSessionsUseCase,
    private val managementDeleteSessionUseCase: ManagementDeleteSessionUseCase,
    private val managementDeleteAllUserSessionsUseCase: ManagementDeleteAllUserSessionsUseCase,
    private val authStorage: AuthStorage? = null,
    private val userRepository: UserRepository? = null,
    private val onNavigateToSessionDetail: ((UserSessionSummary) -> Unit)? = null,
    private val onBack: () -> Unit
) : UserSessionListComponent, ComponentContext by componentContext {

    private val userId: UserId? get() = params?.typedUserId

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<UserSessionListScreenState>(UserSessionListScreenState.Loading)
    override val state: Value<UserSessionListScreenState> = _state

    init {
        loadSessions()
    }

    override fun onRefresh() {
        loadSessions()
    }

    override fun onSessionClick(session: UserSessionSummary) {
        onNavigateToSessionDetail?.invoke(session)
    }

    override fun onDeleteSessionClick(sessionId: String) {
        val targetUserId = userId ?: return
        val currentContent = _state.value as? UserSessionListScreenState.Content ?: return
        _state.value = currentContent.copy(actionLoading = true, actionError = null)

        scope.launch {
            managementDeleteSessionUseCase(targetUserId, sessionId)
                .onSuccess {
                    val updatedItems = currentContent.paging.items.filterNot { it.id.asHexDashString() == sessionId }
                    _state.value = currentContent.copy(
                        paging = currentContent.paging.copy(items = updatedItems),
                        actionLoading = false
                    )
                }
                .onError { error ->
                    val content = _state.value as? UserSessionListScreenState.Content
                    if (content != null) {
                        _state.value = content.copy(actionLoading = false, actionError = error)
                    }
                }
        }
    }

    override fun onSessionRevoked(sessionId: UserSessionId) {
        val currentContent = _state.value as? UserSessionListScreenState.Content ?: return
        val updatedItems = currentContent.paging.items.filterNot { it.id == sessionId }
        _state.value = currentContent.copy(
            paging = currentContent.paging.copy(items = updatedItems)
        )
    }

    override fun onDeleteAllSessionsClick() {
        val targetUserId = userId ?: return
        val currentContent = _state.value as? UserSessionListScreenState.Content ?: return
        _state.value = currentContent.copy(actionLoading = true, actionError = null)

        scope.launch {
            managementDeleteAllUserSessionsUseCase(targetUserId)
                .onSuccess {
                    _state.value = currentContent.copy(
                        paging = PaginationState(),
                        actionLoading = false
                    )
                }
                .onError { error ->
                    val content = _state.value as? UserSessionListScreenState.Content
                    if (content != null) {
                        _state.value = content.copy(actionLoading = false, actionError = error)
                    }
                }
        }
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? UserSessionListScreenState.Content ?: return
        val paging = currentContent.paging
        if (!paging.canLoadMore) {
            return
        }

        _state.value = currentContent.copy(paging = paging.toNextPageLoading())
        fetchPage(pageNumber = paging.nextPageNumber)
    }

    override fun onBackClick() {
        onBack()
    }

    private fun loadSessions() {
        val targetUserId = userId
        if (targetUserId == null) {
            _state.value = UserSessionListScreenState.Error(
                CommonError.ContractViolation(throwable = IllegalStateException("Invalid userId in params"))
            )
            return
        }

        val currentContent = _state.value as? UserSessionListScreenState.Content

        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionError = null
            )
        } else {
            _state.value = UserSessionListScreenState.Loading
        }

        fetchPage(pageNumber = params?.pageNumber ?: ListingConstants.INITIAL_PAGE_NUMBER)
    }

    private fun fetchPage(pageNumber: Int) {
        val targetUserId = userId ?: return
        scope.launch {
            managementGetUserSessionsUseCase(
                userId = targetUserId,
                pageNumber = pageNumber,
                pageSize = params?.pageSize ?: ListingConstants.DEFAULT_PAGE_SIZE
            )
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? UserSessionListScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserSessionSummary>()).appendResult(pagedResult)
                    val activeSessionId = authStorage?.getSessionId()?.toUserSessionIdOrNull()
                    val currentUserId = userRepository?.currentUser?.firstOrNull()?.id
                    val isSelfUser = currentUserId != null && targetUserId == currentUserId
                    _state.value = currentContent?.copy(
                        paging = newPaging,
                        currentSessionId = activeSessionId,
                        currentUserId = currentUserId,
                        isSelfUser = isSelfUser
                    ) ?: UserSessionListScreenState.Content(
                        paging = newPaging,
                        currentSessionId = activeSessionId,
                        currentUserId = currentUserId,
                        isSelfUser = isSelfUser
                    )
                }
                .onError { error ->
                    val currentContent = _state.value as? UserSessionListScreenState.Content
                    if (currentContent != null) {
                        _state.value = currentContent.copy(
                            paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                        )
                    } else {
                        _state.value = UserSessionListScreenState.Error(error)
                    }
                }
        }
    }
}
