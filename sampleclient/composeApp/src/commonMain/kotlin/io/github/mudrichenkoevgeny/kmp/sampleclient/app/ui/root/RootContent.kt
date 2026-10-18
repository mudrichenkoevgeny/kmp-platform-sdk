package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.root

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
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.di.ClientAppComponent
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.di.LocalClientAppComponent
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.navigation.deeplink.ClientAppDeepLinkRoute
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main.MainScreen
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.splash.SplashScreen
import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.CommonModuleStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.localization.SecurityModuleStrings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.localization.UserModuleStrings

/**
 * Top-level sample UI: shows [SplashScreen] until [ClientAppComponent.isInitialized], then provides
 * [LocalCommonComponent], [LocalErrorParser], and [LocalClientAppComponent] and displays [MainScreen].
 *
 * @param clientAppComponent Wired host graph; [ClientAppComponent.init] must complete before navigation is shown.
 * @param initialRoute Optional initial deep link route.
 */
@Composable
fun RootContent(
    clientAppComponent: ClientAppComponent,
    initialRoute: ClientAppDeepLinkRoute? = null
) {
    val isInitialized by clientAppComponent.isInitialized.collectAsState()

    val activeLocale = remember { KmpLocalization.currentLocale }
    val commonStrings = remember(activeLocale) { CommonModuleStrings(KmpLocalization.resolver, activeLocale) }
    val securityStrings = remember(activeLocale) { SecurityModuleStrings(KmpLocalization.resolver, activeLocale) }
    val userStrings = remember(activeLocale) { UserModuleStrings(KmpLocalization.resolver, activeLocale) }

    CompositionLocalProvider(
        LocalCommonStrings provides commonStrings,
        LocalSecurityStrings provides securityStrings,
        LocalUserStrings provides userStrings
    ) {
        CoreTheme {
            if (isInitialized) {
                val componentContext = rememberComponentContext()
                val mainComponent = remember {
                    clientAppComponent.createMainScreenComponent(
                        componentContext = componentContext,
                        initialRoute = initialRoute
                    )
                }

                CompositionLocalProvider(
                    LocalCommonComponent provides clientAppComponent.commonComponent,
                    LocalErrorParser provides clientAppComponent.commonComponent.appErrorParser,
                    LocalClientAppComponent provides clientAppComponent
                ) {
                    SetupBrowserHistory(
                        mainComponent = mainComponent,
                        baseUrl = clientAppComponent.commonComponent.baseUrl
                    )
                    MainScreen(mainComponent)
                }
            } else {
                SplashScreen()
            }
        }
    }
}
