package io.github.mudrichenkoevgeny.kmp.sample.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.WasmDeviceInfoProvider
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.WasmUserAuthServices
import io.github.mudrichenkoevgeny.kmp.sample.BuildConfig
import io.github.mudrichenkoevgeny.kmp.sample.app.di.AppComponent
import kotlinx.browser.document
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.root.RootContent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val deviceInfoProvider = WasmDeviceInfoProvider(
        appVersion = BuildConfig.APP_VERSION
    )

    val appComponent = AppComponent(
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
        appComponent.init()
        appComponent.refreshUserConfigurationUseCase() // or appComponent.syncDataUseCase()
        appComponent.commonComponent.webSocketService.connect()

        ComposeViewport(container) {
            RootContent(appComponent)
        }
    }
}