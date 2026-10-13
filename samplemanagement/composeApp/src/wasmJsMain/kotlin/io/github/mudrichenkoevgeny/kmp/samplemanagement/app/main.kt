package io.github.mudrichenkoevgeny.kmp.samplemanagement.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.github.mudrichenkoevgeny.kmp.core.common.platform.clientdeviceinfo.WasmClientDeviceInfoProvider
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.WasmUserAuthServices
import io.github.mudrichenkoevgeny.kmp.samplemanagement.BuildConfig
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.ManagementAppComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink.ManagementAppDeepLinkParser
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.root.RootContent
import io.ktor.http.Url
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * Wasm browser entry: builds [ManagementAppComponent], initializes SDK services, connects WebSockets, and hosts [RootContent].
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val clientDeviceInfoProvider = WasmClientDeviceInfoProvider(
        appVersion = BuildConfig.APP_VERSION
    )

    val managementAppComponent = ManagementAppComponent(
        platformContext = null,
        clientDeviceInfo = clientDeviceInfoProvider.getClientDeviceInfo(),
        baseUrl = BuildConfig.BASE_URL,
        authServices = WasmUserAuthServices(
            googleWebClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
        )
    )

    val container = document.getElementById("ComposeTarget")
        ?: error("Element not found")

    val url = Url(window.location.href)
    val parser = ManagementAppDeepLinkParser()
    val initialRoute = parser.parse(url)

    MainScope().launch {
        managementAppComponent.init()
        managementAppComponent.refreshUserConfigurationUseCase() // or appComponent.syncDataUseCase()
        managementAppComponent.commonComponent.webSocketService.connect()

        ComposeViewport(container) {
            RootContent(
                managementAppComponent = managementAppComponent,
                initialRoute = initialRoute
            )
        }
    }
}