package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.events

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent

sealed interface AuditEventsScreenState {
    object Loading : AuditEventsScreenState
    data class Error(val error: AppError) : AuditEventsScreenState
    data class Content(
        val paging: PaginationState<AuditEvent>,
        val sortState: ListingSortState? = null,
        val filterStates: Map<String, ListingFilterState> = emptyMap(),
        val isFilterPanelExpanded: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : AuditEventsScreenState
}
