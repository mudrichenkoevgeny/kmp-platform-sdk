package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo

import android.content.Context
import android.os.Build
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceId
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import java.util.Locale

/**
 * Android [DeviceInfoProvider] that reads app metadata from the host `PackageManager`, device fields from
 * [Build], and the default [Locale].
 *
 * @param context Any application context used for package metadata.
 */
class AndroidDeviceInfoProvider(
    private val context: Context
) : DeviceInfoProvider {
    /**
     * @return [DeviceInfo] with [UserClientType.ANDROID], a newly generated [DeviceId], manufacturer and model
     * as device name, default locale language code, the host app `versionName` (or [DeviceInfo.VERSION_UNKNOWN]),
     * and the Android OS release string from `Build`.
     */
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