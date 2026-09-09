package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.sessions

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.launch

class UserSessionsComponentImpl(
    componentContext: ComponentContext,
    private val userId: UserId,
    private val managementGetSessionsUseCase: ManagementGetSessionsUseCase,
    private val onBack: () -> Unit
) : UserSessionsComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<UserSessionsScreenState>(UserSessionsScreenState.Loading)
    override val state: Value<UserSessionsScreenState> = _state

    init {
        loadSessions()
    }

    override fun onRefresh() {
        loadSessions()
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? UserSessionsScreenState.Content ?: return
        val paging = currentContent.paging
        if (!paging.canLoadMore) return

        _state.value = currentContent.copy(paging = paging.toNextPageLoading())
        fetchPage(pageNumber = paging.nextPageNumber)
    }

    override fun onBackClick() {
        onBack()
    }

    private fun loadSessions() {
        val currentContent = _state.value as? UserSessionsScreenState.Content
        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionError = null
            )
        } else {
            _state.value = UserSessionsScreenState.Loading
        }

        fetchPage(pageNumber = ListingConstants.INITIAL_PAGE_NUMBER)
    }

    private fun fetchPage(pageNumber: Int) {
        scope.launch {
            managementGetSessionsUseCase(
                pageNumber = pageNumber,
                pageSize = ListingConstants.DEFAULT_PAGE_SIZE,
                userIds = listOf(userId.value.toString())
            ).onSuccess { pagedResult ->
                val currentContent = _state.value as? UserSessionsScreenState.Content
                val newPaging = (currentContent?.paging ?: PaginationState<UserSession>()).appendResult(pagedResult)
                _state.value = UserSessionsScreenState.Content(paging = newPaging)
            }.onError { error ->
                val currentContent = _state.value as? UserSessionsScreenState.Content
                if (currentContent != null) {
                    _state.value = currentContent.copy(
                        paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                    )
                } else {
                    _state.value = UserSessionsScreenState.Error(error)
                }
            }
        }
    }
}
