package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.identifiers

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
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
        fetchPage(pageNumber = paging.nextPageNumber)
    }

    override fun onBackClick() {
        onBack()
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

        fetchPage(pageNumber = ListingConstants.INITIAL_PAGE_NUMBER)
    }

    private fun fetchPage(pageNumber: Int) {
        scope.launch {
            managementGetIdentifiersUseCase(
                pageNumber = pageNumber,
                pageSize = ListingConstants.DEFAULT_PAGE_SIZE,
                userIds = listOf(userId.value.toString())
            ).onSuccess { pagedResult ->
                val currentContent = _state.value as? UserIdentifiersScreenState.Content
                val newPaging = (currentContent?.paging ?: PaginationState<UserIdentifier>()).appendResult(pagedResult)
                _state.value = UserIdentifiersScreenState.Content(paging = newPaging)
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
