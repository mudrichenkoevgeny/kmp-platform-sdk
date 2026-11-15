package io.github.mudrichenkoevgeny.kmp.core.common.ui.test

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import org.jetbrains.compose.resources.PreviewContextConfigurationEffect

@InternalApi
@Composable
public fun ComponentTestHarness(content: @Composable () -> Unit) {
    PreviewContextConfigurationEffect()
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            content()
        }
    }
}
