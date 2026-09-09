package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.detail.AuditEventDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.events.AuditEventsComponent

/**
 * Root Decompose component for the audit logs flows.
 */
interface AuditApiRootComponent {
    /** Navigation stack of child screens. */
    val stack: Value<ChildStack<AuditApiDestination, Child>>

    /** Active child component in the navigation stack. */
    sealed interface Child {
        /** Audit events listing screen. */
        class Main(val component: AuditEventsComponent) : Child

        /** Audit event detail screen. */
        class Detail(val component: AuditEventDetailComponent) : Child
    }

    /** Navigates back. */
    fun onBackClick()
}
