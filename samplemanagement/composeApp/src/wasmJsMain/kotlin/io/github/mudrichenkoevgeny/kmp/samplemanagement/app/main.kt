package io.github.mudrichenkoevgeny.kmp.samplemanagement.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.WasmDeviceInfoProvider
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.WasmUserAuthServices
import io.github.mudrichenkoevgeny.kmp.samplemanagement.BuildConfig
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.ManagementAppComponent
import kotlinx.browser.document
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.root.RootContent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * Wasm browser entry: builds [ManagementAppComponent], initializes SDK services, connects WebSockets, and hosts [RootContent].
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val deviceInfoProvider = WasmDeviceInfoProvider(
        appVersion = BuildConfig.APP_VERSION
    )

    val managementAppComponent = ManagementAppComponent(
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
        managementAppComponent.init()
        managementAppComponent.refreshUserConfigurationUseCase() // or appComponent.syncDataUseCase()
        managementAppComponent.commonComponent.webSocketService.connect()

        ComposeViewport(container) {
            RootContent(managementAppComponent)
        }
    }
}