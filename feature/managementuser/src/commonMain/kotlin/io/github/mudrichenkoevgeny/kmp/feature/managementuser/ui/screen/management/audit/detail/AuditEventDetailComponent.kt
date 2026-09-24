package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail

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

    /** Navigates to resource detail screen or profile depending on resource type and ownership. */
    fun onResourceClick()

    /** Navigates to subject user detail screen or profile depending on subject type and ownership. */
    fun onSubjectClick()
}