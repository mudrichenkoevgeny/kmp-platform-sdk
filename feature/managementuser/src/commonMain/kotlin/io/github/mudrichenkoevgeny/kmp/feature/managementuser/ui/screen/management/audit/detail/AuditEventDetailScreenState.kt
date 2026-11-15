package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventPrivate

/**
 * UI state for audit event detail screen.
 */
public sealed interface AuditEventDetailScreenState {

    /** Initial loading state while fetching audit event details. */
    public object Loading : AuditEventDetailScreenState

    /** Error state when fetching audit event details fails. */
    public data class Error(public val error: AppError) : AuditEventDetailScreenState

    /**
     * Content state displaying the audit event details.
     *
     * @param event The target audit event entity.
     */
    public data class Content(
        public val event: AuditEventPrivate
    ) : AuditEventDetailScreenState
}
