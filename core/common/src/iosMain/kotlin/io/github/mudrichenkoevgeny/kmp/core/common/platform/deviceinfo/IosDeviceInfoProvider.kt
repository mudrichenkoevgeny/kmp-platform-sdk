package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo

import platform.Foundation.NSBundle
import platform.UIKit.UIDevice
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

class IosDeviceInfoProvider : DeviceInfoProvider {
    override fun getDeviceInfo(): ClientDeviceInfo {
        val device = UIDevice.currentDevice
        val appVersion = NSBundle.mainBundle.infoDictionary
            ?.get("CFBundleShortVersionString") as? String
            ?: "1.0"
        val language = NSLocale.currentLocale.languageCode

        return ClientDeviceInfo(
            deviceId = ClientDeviceId.generate(),
            deviceName = "${device.systemName} ${device.model}",
            clientType = ClientType.IOS,
            language = language,
            appVersion = appVersion,
            operationSystemVersion = device.systemVersion
        )
    }
}