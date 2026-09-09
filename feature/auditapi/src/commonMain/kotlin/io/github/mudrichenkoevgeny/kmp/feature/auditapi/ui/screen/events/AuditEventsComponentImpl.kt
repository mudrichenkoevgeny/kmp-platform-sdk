package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.events

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
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.usecase.GetAuditEventsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import kotlinx.coroutines.launch

/**
 * Default implementation of [AuditEventsComponent].
 *
 * @param componentContext Decompose [ComponentContext].
 * @param getAuditEventsUseCase Fetches paginated audit events.
 * @param onNavigateToEventDetail Navigates to audit event detail screen.
 * @param onBack Pops this screen from the navigation stack.
 */
class AuditEventsComponentImpl(
    componentContext: ComponentContext,
    private val getAuditEventsUseCase: GetAuditEventsUseCase,
    private val onNavigateToEventDetail: (AuditEventId) -> Unit,
    private val onBack: () -> Unit
) : AuditEventsComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<AuditEventsScreenState>(AuditEventsScreenState.Loading)
    override val state: Value<AuditEventsScreenState> = _state

    init {
        loadEvents()
    }

    override fun onRefresh() {
        loadEvents()
    }

    override fun onEventClick(eventId: AuditEventId) {
        onNavigateToEventDetail(eventId)
    }

    override fun onLoadNextPage() {
        val currentContent = _state.value as? AuditEventsScreenState.Content ?: return
        val paging = currentContent.paging
        if (!paging.canLoadMore) return

        _state.value = currentContent.copy(paging = paging.toNextPageLoading())
        fetchPage(pageNumber = paging.nextPageNumber)
    }

    override fun onBackClick() {
        onBack()
    }

    private fun loadEvents() {
        val currentContent = _state.value as? AuditEventsScreenState.Content
        if (currentContent != null) {
            _state.value = currentContent.copy(
                paging = currentContent.paging.toInitialLoading(),
                actionError = null
            )
        } else {
            _state.value = AuditEventsScreenState.Loading
        }

        fetchPage(pageNumber = ListingConstants.INITIAL_PAGE_NUMBER)
    }

    private fun fetchPage(pageNumber: Int) {
        scope.launch {
            getAuditEventsUseCase(
                pageNumber = pageNumber,
                pageSize = ListingConstants.DEFAULT_PAGE_SIZE
            ).onSuccess { pagedResult ->
                val currentContent = _state.value as? AuditEventsScreenState.Content
                val newPaging = (currentContent?.paging ?: PaginationState<AuditEvent>()).appendResult(pagedResult)
                _state.value = AuditEventsScreenState.Content(paging = newPaging)
            }.onError { error ->
                val currentContent = _state.value as? AuditEventsScreenState.Content
                if (currentContent != null) {
                    _state.value = currentContent.copy(
                        paging = currentContent.paging.toError(error, pageNumber = pageNumber)
                    )
                } else {
                    _state.value = AuditEventsScreenState.Error(error)
                }
            }
        }
    }
}
