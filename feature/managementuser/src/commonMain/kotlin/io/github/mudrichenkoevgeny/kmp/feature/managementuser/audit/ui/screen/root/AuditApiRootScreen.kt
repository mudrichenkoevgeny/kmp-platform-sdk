package io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.ui.screen.root.AuditApiRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.detail.AuditEventDetailScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.events.AuditEventsScreen

@Composable
fun AuditApiRootScreen(component: AuditApiRootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation(fade())
    ) { child ->
        when (val instance = child.instance) {
            is AuditApiRootComponent.Child.Main -> AuditEventsScreen(instance.component)
            is AuditApiRootComponent.Child.Detail -> AuditEventDetailScreen(instance.component)
        }
    }
}

@InternalApi
@Composable
private fun AuditApiRootScreenPreviewContent() {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        AuditApiRootScreen(
            component = AuditApiRootComponentMock()
        )
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun AuditApiRootScreenPreview() {
    ScreenPreviewContainer {
        AuditApiRootScreenPreviewContent()
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        AuditApiRootScreenPreviewContent()
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        AuditApiRootScreenPreviewContent()
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        AuditApiRootScreenPreviewContent()
    }
}
