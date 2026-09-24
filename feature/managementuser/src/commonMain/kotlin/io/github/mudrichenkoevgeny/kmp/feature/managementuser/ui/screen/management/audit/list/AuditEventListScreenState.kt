package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent

sealed interface AuditEventListScreenState {
    object Loading : AuditEventListScreenState
    data class Error(val error: AppError) : AuditEventListScreenState
    data class Content(
        val paging: PaginationState<AuditEvent>,
        val sortState: ListingSortState? = null,
        val filterStates: Map<String, ListingFilterState> = emptyMap(),
        val isFilterPanelExpanded: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : AuditEventListScreenState
}