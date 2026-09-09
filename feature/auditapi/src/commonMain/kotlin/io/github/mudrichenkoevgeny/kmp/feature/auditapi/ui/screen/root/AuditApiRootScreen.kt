package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.detail.AuditEventDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.events.AuditEventsScreen

@Composable
fun AuditApiRootScreen(component: AuditApiRootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation(slide())
    ) { child ->
        when (val instance = child.instance) {
            is AuditApiRootComponent.Child.Main -> AuditEventsScreen(instance.component)
            is AuditApiRootComponent.Child.Detail -> AuditEventDetailScreen(instance.component)
        }
    }
}
