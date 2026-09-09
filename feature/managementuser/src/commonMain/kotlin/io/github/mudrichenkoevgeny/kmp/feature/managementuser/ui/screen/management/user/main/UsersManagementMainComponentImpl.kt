package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUsersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.launch

class UsersManagementMainComponentImpl(
    componentContext: ComponentContext,
    private val getUsersUseCase: GetUsersUseCase,
    private val onNavigateToUserDetail: (UserId) -> Unit,
    private val onNavigateToCreateUser: () -> Unit,
    private val onBack: () -> Unit
) : UsersManagementMainComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<UsersManagementMainScreenState>(UsersManagementMainScreenState.Loading)
    override val state: Value<UsersManagementMainScreenState> = _state

    init {
        loadUsers()
    }

    override fun onRefresh() {
        loadUsers()
    }

    override fun onUserClick(userId: UserId) {
        onNavigateToUserDetail(userId)
    }

    override fun onCreateUserClick() {
        onNavigateToCreateUser()
    }

    override fun onBackClick() {
        onBack()
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? UsersManagementMainScreenState.Content ?: return
        val paging = currentContent.paging
        if (!paging.canLoadMore) return

        _state.value = currentContent.copy(paging = paging.toNextPageLoading())
        fetchPage(pageNumber = paging.nextPageNumber)
    }

    private fun loadUsers() {
        val currentContent = _state.value as? UsersManagementMainScreenState.Content
        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionError = null
            )
        } else {
            _state.value = UsersManagementMainScreenState.Loading
        }

        fetchPage(pageNumber = ListingConstants.INITIAL_PAGE_NUMBER)
    }

    private fun fetchPage(pageNumber: Int) {
        scope.launch {
            getUsersUseCase(pageNumber = pageNumber, pageSize = ListingConstants.DEFAULT_PAGE_SIZE)
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? UsersManagementMainScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserDetails>()).appendResult(pagedResult)
                    _state.value = UsersManagementMainScreenState.Content(paging = newPaging)
                }
                .onError { error ->
                    val currentContent = _state.value as? UsersManagementMainScreenState.Content
                    if (currentContent != null) {
                        _state.value = currentContent.copy(
                            paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                        )
                    } else {
                        _state.value = UsersManagementMainScreenState.Error(error)
                    }
                }
        }
    }
}
