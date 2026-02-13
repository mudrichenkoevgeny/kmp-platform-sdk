package io.github.mudrichenkoevgeny.kmp.core.common.platform

import android.content.Context
import android.os.Build
import io.github.mudrichenkoevgeny.kmp.core.common.config.model.CommonConfig
import io.github.mudrichenkoevgeny.kmp.core.common.platform.model.DeviceInfo
import io.github.mudrichenkoevgeny.kmp.core.common.storage.common.CommonStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import java.util.Locale

class AndroidDeviceInfoProvider(
    private val context: Context,
    private val commonStorage: CommonStorage
) : DeviceInfoProvider {
    override suspend fun getDeviceInfo(): DeviceInfo {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val appVersion = packageInfo.versionName
            ?: DeviceInfo.VERSION_UNKNOWN

        return DeviceInfo(
            clientType = UserClientType.ANDROID,
            deviceId = getOrCreateDeviceId(commonStorage),
            deviceName = "${Build.MANUFACTURER} ${Build.MODEL}",
            language = Locale.getDefault().language,
            appVersion = appVersion,
            osVersion = Build.VERSION.RELEASE
        )
    }
}

actual fun getDeviceInfoProvider(
    platformContext: Any?,
    commonConfig: CommonConfig,
    commonStorage: CommonStorage
): DeviceInfoProvider = AndroidDeviceInfoProvider(platformContext as Context, commonStorage)