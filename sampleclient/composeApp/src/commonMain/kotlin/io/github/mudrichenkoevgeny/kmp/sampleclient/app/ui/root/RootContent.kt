package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalCommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.rememberComponentContext
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.di.ClientAppComponent
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.di.LocalClientAppComponent
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main.MainScreen
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.splash.SplashScreen

/**
 * Top-level sample UI: shows [SplashScreen] until [ClientAppComponent.isInitialized], then provides
 * [LocalCommonComponent], [LocalErrorParser], and [LocalClientAppComponent] and displays [MainScreen].
 *
 * @param clientAppComponent Wired host graph; [ClientAppComponent.init] must complete before navigation is shown.
 */
@Composable
fun RootContent(clientAppComponent: ClientAppComponent) {
    val isInitialized by clientAppComponent.isInitialized.collectAsState()

    if (isInitialized) {
        val componentContext = rememberComponentContext()
        val mainComponent = remember {
            clientAppComponent.createMainScreenComponent(componentContext)
        }

        CompositionLocalProvider(
            LocalCommonComponent provides clientAppComponent.commonComponent,
            LocalErrorParser provides clientAppComponent.commonComponent.appErrorParser,
            LocalClientAppComponent provides clientAppComponent
        ) {
            MainScreen(mainComponent)
        }
    } else {
        SplashScreen()
    }
}