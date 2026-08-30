package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalCommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.rememberComponentContext
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.ManagementAppComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.LocalManagementAppComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main.MainScreen
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.splash.SplashScreen

/**
 * Top-level sample UI: shows [SplashScreen] until [ManagementAppComponent.isInitialized], then provides
 * [LocalCommonComponent], [LocalErrorParser], and [LocalManagementAppComponent] and displays [MainScreen].
 *
 * @param managementAppComponent Wired host graph; [ManagementAppComponent.init] must complete before navigation is shown.
 */
@Composable
fun RootContent(managementAppComponent: ManagementAppComponent) {
    val isInitialized by managementAppComponent.isInitialized.collectAsState()

    if (isInitialized) {
        val componentContext = rememberComponentContext()
        val mainComponent = remember {
            managementAppComponent.createMainScreenComponent(componentContext)
        }

        CompositionLocalProvider(
            LocalCommonComponent provides managementAppComponent.commonComponent,
            LocalErrorParser provides managementAppComponent.commonComponent.appErrorParser,
            LocalManagementAppComponent provides managementAppComponent
        ) {
            MainScreen(mainComponent)
        }
    } else {
        SplashScreen()
    }
}