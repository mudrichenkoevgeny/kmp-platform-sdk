package io.github.mudrichenkoevgeny.kmp.core.common.platform

import io.github.mudrichenkoevgeny.kmp.core.common.config.model.CommonConfig
import io.github.mudrichenkoevgeny.kmp.core.common.platform.model.DeviceInfo
import io.github.mudrichenkoevgeny.kmp.core.common.storage.common.CommonStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import platform.Foundation.NSBundle
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.UIKit.UIDevice

class IosDeviceInfoProvider(
    private val commonStorage: CommonStorage
) : DeviceInfoProvider {
    override suspend fun getDeviceInfo(): DeviceInfo {
        val device = UIDevice.currentDevice
        val appVersion = NSBundle.mainBundle.infoDictionary
            ?.get("CFBundleShortVersionString") as? String
            ?: DeviceInfo.VERSION_UNKNOWN

        return DeviceInfo(
            clientType = UserClientType.IOS,
            deviceId = getOrCreateDeviceId(commonStorage),
            deviceName = "${device.systemName} ${device.model}",
            language = NSLocale.currentLocale.languageCode,
            appVersion = appVersion,
            osVersion = device.systemVersion
        )
    }
}

actual fun getDeviceInfoProvider(
    platformContext: Any?,
    commonConfig: CommonConfig,
    commonStorage: CommonStorage
): DeviceInfoProvider = IosDeviceInfoProvider(commonStorage)