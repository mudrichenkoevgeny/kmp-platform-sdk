package io.github.mudrichenkoevgeny.kmp.sample.app.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalCommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.rememberComponentContext
import io.github.mudrichenkoevgeny.kmp.sample.app.di.AppComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.di.LocalAppComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main.MainScreen
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.splash.SplashScreen

/**
 * Top-level sample UI: shows [SplashScreen] until [AppComponent.isInitialized], then provides
 * [LocalCommonComponent], [LocalErrorParser], and [LocalAppComponent] and displays [MainScreen].
 *
 * @param appComponent Wired host graph; [AppComponent.init] must complete before navigation is shown.
 */
@Composable
fun RootContent(appComponent: AppComponent) {
    val isInitialized by appComponent.isInitialized.collectAsState()

    if (isInitialized) {
        val componentContext = rememberComponentContext()
        val mainComponent = remember {
            appComponent.createMainScreenComponent(componentContext)
        }

        CompositionLocalProvider(
            LocalCommonComponent provides appComponent.commonComponent,
            LocalErrorParser provides appComponent.commonComponent.appErrorParser,
            LocalAppComponent provides appComponent
        ) {
            MainScreen(mainComponent)
        }
    } else {
        SplashScreen()
    }
}