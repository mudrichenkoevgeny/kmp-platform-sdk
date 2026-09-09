package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.detail

import com.arkivanov.decompose.value.Value

/**
 * Manages fetching and displaying details of a specific audit event.
 */
interface AuditEventDetailComponent {
    /** Reactive UI state. */
    val state: Value<AuditEventDetailScreenState>

    /** Retries loading event details after a failure. */
    fun onRetry()

    /** Navigates back. */
    fun onBackClick()
}
