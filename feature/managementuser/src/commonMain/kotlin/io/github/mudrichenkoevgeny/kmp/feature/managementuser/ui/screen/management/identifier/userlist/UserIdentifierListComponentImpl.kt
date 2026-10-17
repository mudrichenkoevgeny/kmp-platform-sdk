package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.userlist.ManagementUserIdentifierListParams
import kotlinx.coroutines.launch

/**
 * Default [UserIdentifierListComponent] implementation: manages identifiers list for a specific user.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Screen destination parameters.
 * @param managementGetUserIdentifiersUseCase Use case to fetch paginated list of identifiers.
 * @param onIdentifierSelect Callback invoked when an identifier is tapped.
 * @param onBack Pops this screen from the navigation stack.
 */
class UserIdentifierListComponentImpl(
    componentContext: ComponentContext,
    private val params: ManagementUserIdentifierListParams,
    private val managementGetUserIdentifiersUseCase: ManagementGetUserIdentifiersUseCase,
    private val userRepository: UserRepository? = null,
    private val onIdentifierSelect: ((String) -> Unit)? = null,
    private val onBack: () -> Unit
) : UserIdentifierListComponent, ComponentContext by componentContext {

    private val userId: UserId? get() = params.typedUserId

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<UserIdentifierListScreenState>(UserIdentifierListScreenState.Loading)
    override val state: Value<UserIdentifierListScreenState> = _state

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
        val currentContent = _state.value as? UserIdentifierListScreenState.Content ?: return
        val updatedItems = currentContent.paging.items.filterNot { it.id == identifierId }
        _state.value = currentContent.copy(
            paging = currentContent.paging.copy(items = updatedItems)
        )
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? UserIdentifierListScreenState.Content ?: return
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

    private fun loadIdentifiers() {
        val targetUserId = userId
        if (targetUserId == null) {
            _state.value = UserIdentifierListScreenState.Error(
                CommonError.ContractViolation(throwable = IllegalStateException("Invalid userId in params"))
            )
            return
        }

        val currentContent = _state.value as? UserIdentifierListScreenState.Content

        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading()
            )
        } else {
            _state.value = UserIdentifierListScreenState.Loading
        }

        fetchPage(pageNumber = params.pageNumber)
    }

    private fun fetchPage(pageNumber: Int) {
        val targetUserId = userId ?: return
        scope.launch {
            managementGetUserIdentifiersUseCase(
                userId = targetUserId,
                pageNumber = pageNumber,
                pageSize = params.pageSize
            )
                .onSuccess { pagedResult ->
                    val currentContent = _state.value as? UserIdentifierListScreenState.Content
                    val newPaging = (currentContent?.paging ?: PaginationState<UserIdentifierSummary>()).appendResult(pagedResult)
                    val currentUserId = userRepository?.currentUser?.firstOrNull()?.id
                    _state.value = currentContent?.copy(
                        paging = newPaging,
                        currentUserId = currentUserId
                    ) ?: UserIdentifierListScreenState.Content(
                        paging = newPaging,
                        currentUserId = currentUserId
                    )
                }
                .onError { error ->
                    val currentContent = _state.value as? UserIdentifierListScreenState.Content
                    if (currentContent != null) {
                        _state.value = currentContent.copy(
                            paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                        )
                    } else {
                        _state.value = UserIdentifierListScreenState.Error(error)
                    }
                }
        }
    }
}
