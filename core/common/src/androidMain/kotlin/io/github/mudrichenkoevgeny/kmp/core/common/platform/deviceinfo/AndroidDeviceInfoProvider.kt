package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo

import android.content.Context
import android.os.Build
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceId
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import java.util.Locale

class AndroidDeviceInfoProvider(
    private val context: Context
) : DeviceInfoProvider {
    override fun getDeviceInfo(): DeviceInfo {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val appVersion = packageInfo.versionName
            ?: DeviceInfo.VERSION_UNKNOWN

        return DeviceInfo(
            clientType = UserClientType.ANDROID,
            deviceId = DeviceId.generate(),
            deviceName = "${Build.MANUFACTURER} ${Build.MODEL}",
            language = Locale.getDefault().language,
            appVersion = appVersion,
            osVersion = Build.VERSION.RELEASE
        )
    }
}