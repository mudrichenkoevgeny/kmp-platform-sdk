package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo

import android.content.Context
import android.os.Build
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
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
     * @return [ClientDeviceInfo] with [ClientType.ANDROID], a newly generated [ClientDeviceId], manufacturer and model
     * as device name, default locale language code, the host app `versionName`,
     * and the Android OS release string from `Build`.
     */
    override fun getDeviceInfo(): ClientDeviceInfo {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val appVersion = packageInfo.versionName

        return ClientDeviceInfo(
            deviceId = ClientDeviceId.generate(),
            deviceName = "${Build.MANUFACTURER} ${Build.MODEL}",
            clientType = ClientType.ANDROID,
            language = Locale.getDefault().language,
            appVersion = appVersion,
            operationSystemVersion = Build.VERSION.RELEASE
        )
    }
}