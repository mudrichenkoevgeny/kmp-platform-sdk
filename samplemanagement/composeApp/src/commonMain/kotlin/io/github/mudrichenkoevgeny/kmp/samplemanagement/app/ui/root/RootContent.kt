package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalCommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.rememberComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.security.localization.LocalSecurityStrings
import io.github.mudrichenkoevgeny.kmp.core.storage.localization.LocalStorageStrings
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.LocalManagementAppComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.ManagementAppComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink.ManagementAppDeepLinkRoute
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main.MainScreen
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.splash.SplashScreen
import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.CommonModuleStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.localization.SecurityModuleStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.localization.StorageModuleStrings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.localization.UserModuleStrings

/**
 * Top-level sample UI: shows [SplashScreen] until [ManagementAppComponent.isInitialized], then provides
 * [LocalCommonComponent], [LocalErrorParser], and [LocalManagementAppComponent] and displays [MainScreen].
 *
 * @param managementAppComponent Wired host graph; [ManagementAppComponent.init] must complete before navigation is shown.
 */
@Composable
fun RootContent(
    managementAppComponent: ManagementAppComponent,
    initialRoute: ManagementAppDeepLinkRoute? = null
) {
    val isInitialized by managementAppComponent.isInitialized.collectAsState()

    val activeLocale = remember { KmpLocalization.currentLocale }
    val commonStrings = remember(activeLocale) { CommonModuleStrings(KmpLocalization.resolver, activeLocale) }
    val securityStrings = remember(activeLocale) { SecurityModuleStrings(KmpLocalization.resolver, activeLocale) }
    val storageStrings = remember(activeLocale) { StorageModuleStrings(KmpLocalization.resolver, activeLocale) }
    val userStrings = remember(activeLocale) { UserModuleStrings(KmpLocalization.resolver, activeLocale) }

    CompositionLocalProvider(
        LocalCommonStrings provides commonStrings,
        LocalSecurityStrings provides securityStrings,
        LocalStorageStrings provides storageStrings,
        LocalUserStrings provides userStrings
    ) {
        CoreTheme {
            if (isInitialized) {
                val componentContext = rememberComponentContext()
                val mainComponent = remember {
                    managementAppComponent.createMainScreenComponent(
                        componentContext = componentContext,
                        initialRoute = initialRoute
                    )
                }

                CompositionLocalProvider(
                    LocalCommonComponent provides managementAppComponent.commonComponent,
                    LocalErrorParser provides managementAppComponent.commonComponent.appErrorParser,
                    LocalManagementAppComponent provides managementAppComponent
                ) {
                    SetupBrowserHistory(
                        mainComponent = mainComponent,
                        baseUrl = managementAppComponent.commonComponent.baseUrl
                    )
                    MainScreen(mainComponent)
                }
            } else {
                SplashScreen()
            }
        }
    }
}
