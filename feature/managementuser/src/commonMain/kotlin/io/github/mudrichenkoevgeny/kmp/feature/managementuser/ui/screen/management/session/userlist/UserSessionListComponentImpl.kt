package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteAllUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.launch

class UserSessionListComponentImpl(
    componentContext: ComponentContext,
    private val userId: UserId? = null,
    private val managementGetSessionsUseCase: ManagementGetSessionsUseCase,
    private val managementDeleteSessionUseCase: ManagementDeleteSessionUseCase,
    private val managementDeleteAllUserSessionsUseCase: ManagementDeleteAllUserSessionsUseCase,
    private val onBack: () -> Unit
) : UserSessionListComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<UserSessionListScreenState>(UserSessionListScreenState.Loading)
    override val state: Value<UserSessionListScreenState> = _state

    init {
        loadSessions()
    }

    override fun onRefresh() {
        loadSessions()
    }

    override fun onBackClick() {
        onBack()
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? UserSessionListScreenState.Content ?: return
        val paging = currentContent.paging
        if (!paging.canLoadMore) return

        _state.value = currentContent.copy(paging = paging.toNextPageLoading())
        fetchPage(
            pageNumber = paging.nextPageNumber
        )
    }

    override fun onDeleteSessionClick(sessionId: String) {
        val targetUserId = userId ?: return
        val currentContent = _state.value as? UserSessionListScreenState.Content ?: return
        _state.value = currentContent.copy(actionLoading = true, actionError = null)

        scope.launch {
            managementDeleteSessionUseCase(userId = targetUserId, sessionId = sessionId)
                .onSuccess {
                    loadSessions()
                }
                .onError { error ->
                    _state.value = currentContent.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onDeleteAllSessionsClick() {
        val targetUserId = userId ?: return
        val currentContent = _state.value as? UserSessionListScreenState.Content ?: return
        _state.value = currentContent.copy(actionLoading = true, actionError = null)

        scope.launch {
            managementDeleteAllUserSessionsUseCase(userId = targetUserId)
                .onSuccess {
                    loadSessions()
                }
                .onError { error ->
                    _state.value = currentContent.copy(actionLoading = false, actionError = error)
                }
        }
    }

    private fun loadSessions() {
        val currentContent = _state.value as? UserSessionListScreenState.Content
        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionLoading = false,
                actionError = null
            )
        } else {
            _state.value = UserSessionListScreenState.Loading
        }

        fetchPage(
            pageNumber = ListingConstants.INITIAL_PAGE_NUMBER
        )
    }

    private fun fetchPage(
        pageNumber: Int
    ) {
        scope.launch {
            managementGetSessionsUseCase(
                pageNumber = pageNumber,
                pageSize = ListingConstants.DEFAULT_PAGE_SIZE,
                userIds = userId?.let { listOf(it.asHexDashString()) },
                sortBy = null,
                sortOrder = SortOrder.DESC,
                userRoles = null,
                userAuthProviders = null,
                clientTypes = null,
                ipAddresses = null,
                userAgents = null,
                deviceNames = null
            )
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? UserSessionListScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserSession>()).appendResult(pagedResult)
                    _state.value = currentContent?.copy(paging = newPaging) ?: UserSessionListScreenState.Content(paging = newPaging)
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
