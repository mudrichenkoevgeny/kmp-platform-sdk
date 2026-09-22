package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.root.UnlockRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp.UnlockOtpScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection.UnlockMethodSelectionScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.success.UnlockSuccessScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target.UnlockTargetInputScreen

@Composable
fun UnlockRootScreen(component: UnlockRootComponent) {
    Children(stack = component.stack) { child ->
        when (val instance = child.instance) {
            is UnlockRootComponent.Child.MethodSelection -> UnlockMethodSelectionScreen(instance.component)
            is UnlockRootComponent.Child.TargetInput -> UnlockTargetInputScreen(instance.component)
            is UnlockRootComponent.Child.OtpInput -> UnlockOtpScreen(instance.component)
            is UnlockRootComponent.Child.Success -> UnlockSuccessScreen(onFinished = component::onDismiss)
        }
    }
}

@InternalApi
@Composable
private fun UnlockRootScreenPreviewContent() {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        UnlockRootScreen(
            component = UnlockRootComponentMock()
        )
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun UnlockRootScreenPreview() {
    ScreenPreviewContainer {
        UnlockRootScreenPreviewContent()
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        UnlockRootScreenPreviewContent()
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        UnlockRootScreenPreviewContent()
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        UnlockRootScreenPreviewContent()
    }
}
