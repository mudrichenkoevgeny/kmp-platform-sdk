package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail

import com.arkivanov.decompose.value.Value

/**
 * Manages fetching and displaying details of a specific audit event.
 */
public interface AuditEventDetailComponent {
    /** Reactive UI state. */
    public val state: Value<AuditEventDetailScreenState>

    /** Retries loading event details after a failure. */
    public fun onRetry()

    /** Navigates back. */
    public fun onBackClick()

    /** Navigates to resource detail screen depending on resource type and ownership. */
    public fun onResourceClick()

    /** Navigates to subject user detail screen depending on subject type and ownership. */
    public fun onSubjectClick()
}
