package io.github.mudrichenkoevgeny.kmp.samplemanagement.android

import android.app.Application
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.AndroidDeviceInfoProvider
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.AndroidUserAuthServices
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di.ManagementAppComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Sample `Application`: owns a process-wide [ManagementAppComponent], initializes SDK wiring on startup, and opens the WebSocket.
 */
class AndroidApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    val deviceInfoProvider = AndroidDeviceInfoProvider(this)

    val managementAppComponent: ManagementAppComponent by lazy {
        ManagementAppComponent(
            platformContext = this,
            deviceInfo = deviceInfoProvider.getDeviceInfo(),
            baseUrl = BuildConfig.BASE_URL,
            authServices = AndroidUserAuthServices(
                context = this,
                googleWebClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
            )
        )
    }

    override fun onCreate() {
        super.onCreate()

        applicationScope.launch {
            managementAppComponent.init()
            managementAppComponent.refreshUserConfigurationUseCase() // or appComponent.syncDataUseCase()
            managementAppComponent.commonComponent.webSocketService.connect()
        }
    }
}