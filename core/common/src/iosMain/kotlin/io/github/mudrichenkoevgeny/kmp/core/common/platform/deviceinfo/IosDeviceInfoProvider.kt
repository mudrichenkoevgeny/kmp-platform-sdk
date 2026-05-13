package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo

import io.github.mudrichenkoevgeny.kmp.core.common.storage.common.CommonStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import platform.Foundation.NSBundle
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.UIKit.UIDevice

class IosDeviceInfoProvider() : DeviceInfoProvider {
    override fun getDeviceInfo(): DeviceInfo {
        val device = UIDevice.currentDevice
        val appVersion = NSBundle.mainBundle.infoDictionary
            ?.get("CFBundleShortVersionString") as? String
            ?: DeviceInfo.VERSION_UNKNOWN

        ClientDeviceInfo(
            deviceId = ClientDeviceId.generate(),
            deviceName = "${device.systemName} ${device.model}",
            clientType = ClientType.IOS,
            language = Locale.getDefault().language,
            appVersion = appVersion,
            operationSystemVersion = Build.VERSION.RELEASE
        )
    }
}