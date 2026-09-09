package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.detail

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent

sealed interface AuditEventDetailScreenState {
    object Loading : AuditEventDetailScreenState
    data class Error(val error: AppError) : AuditEventDetailScreenState
    data class Content(
        val event: AuditEvent
    ) : AuditEventDetailScreenState
}
