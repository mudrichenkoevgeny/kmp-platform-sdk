package io.github.mudrichenkoevgeny.kmp.sampleclient.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.WasmDeviceInfoProvider
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.WasmUserAuthServices
import io.github.mudrichenkoevgeny.kmp.sampleclient.BuildConfig
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.di.ClientAppComponent
import kotlinx.browser.document
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.root.RootContent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * Wasm browser entry: builds [ClientAppComponent], initializes SDK services, connects WebSockets, and hosts [RootContent].
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val deviceInfoProvider = WasmDeviceInfoProvider(
        appVersion = BuildConfig.APP_VERSION
    )

    val clientAppComponent = ClientAppComponent(
        platformContext = null,
        deviceInfo = deviceInfoProvider.getDeviceInfo(),
        baseUrl = BuildConfig.BASE_URL,
        authServices = WasmUserAuthServices(
            googleWebClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
        )
    )

    val container = document.getElementById("ComposeTarget")
        ?: error("Element not found")

    MainScope().launch {
        clientAppComponent.init()
        clientAppComponent.refreshUserConfigurationUseCase() // or appComponent.syncDataUseCase()
        clientAppComponent.commonComponent.webSocketService.connect()

        ComposeViewport(container) {
            RootContent(clientAppComponent)
        }
    }
}