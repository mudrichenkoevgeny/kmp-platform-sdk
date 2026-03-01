package io.github.mudrichenkoevgeny.kmp.sample.android

import android.app.Application
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.AndroidDeviceInfoProvider
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.AndroidUserAuthServices
import io.github.mudrichenkoevgeny.kmp.sample.app.di.AppComponent
import io.github.mudrichenkoevgeny.kmp.sample.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AndroidApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    val deviceInfoProvider = AndroidDeviceInfoProvider(this)

    val appComponent by lazy {
        AppComponent(
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
            appComponent.init()
            appComponent.refreshUserConfigurationUseCase() // or appComponent.syncDataUseCase()
            appComponent.commonComponent.webSocketService.connect()
        }
    }
}