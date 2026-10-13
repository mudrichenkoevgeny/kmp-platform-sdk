package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list

import kotlinx.serialization.Serializable

/**
 * Navigation arguments for audit event list screen.
 */
@Serializable
data class AuditEventListParams(
    val initialFilters: Filters? = null
) {
    /**
     * Initial pre-applied filters for the audit events list.
     */
    @Serializable
    data class Filters(
        val actorIds: List<String>? = null
    )
}
