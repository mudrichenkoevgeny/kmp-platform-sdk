package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.ListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventSummary

/**
 * UI state for audit event listing screen.
 */
public sealed interface AuditEventListScreenState {

    /** Initial loading state while fetching the first page of audit events. */
    public object Loading : AuditEventListScreenState

    /** Error state when loading audit events fails. */
    public data class Error(public val error: AppError) : AuditEventListScreenState

    /**
     * Content state displaying paginated audit events and filter/sort options.
     *
     * @param paging Paginated list state for audit event summaries.
     * @param sortState Active sorting configuration.
     * @param filterStates Active filter values mapped by filter ID.
     * @param isFilterPanelExpanded True if the filter configuration panel is expanded.
     * @param actionLoading True if a background operation is currently running.
     * @param actionError Error result from the last attempted operation.
     */
    public data class Content(
        public val paging: PaginationState<AuditEventSummary>,
        public val sortState: ListingSortState? = null,
        public val filterStates: Map<String, ListingFilterState> = emptyMap(),
        public val isFilterPanelExpanded: Boolean = false,
        public val actionLoading: Boolean = false,
        public val actionError: AppError? = null
    ) : AuditEventListScreenState
}
