package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.config.model.CommonConfig
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.platform.DeviceInfoProvider
import io.github.mudrichenkoevgeny.kmp.core.common.platform.getDeviceInfoProvider
import io.github.mudrichenkoevgeny.kmp.core.common.platform.model.DeviceInfo
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.common.storage.common.CommonStorage
import io.github.mudrichenkoevgeny.kmp.core.common.storage.common.EncryptedCommonStorage
import io.github.mudrichenkoevgeny.kmp.core.common.storage.getSettingsFactory

class CommonComponent(
    val commonConfig: CommonConfig,
    private val platformContext: Any? = null
) {
    @InternalApi
    constructor(
        commonConfig: CommonConfig,
        deviceInfo: DeviceInfo
    ) : this(commonConfig, null) {
        this._deviceInfo = deviceInfo
    }

    val encryptedSettings: EncryptedSettings by lazy {
        getSettingsFactory(platformContext).create()
    }

    private val commonStorage: CommonStorage by lazy {
        EncryptedCommonStorage(encryptedSettings)
    }

    val deviceInfoProvider: DeviceInfoProvider by lazy {
        getDeviceInfoProvider(platformContext, commonConfig, commonStorage)
    }

    private var _deviceInfo: DeviceInfo? = null
    val deviceInfo: DeviceInfo
        get() = _deviceInfo ?: error("CommonComponent not initialized! Call init() first.")

    suspend fun init() {
        if (_deviceInfo != null) {
            return
        }
        _deviceInfo = deviceInfoProvider.getDeviceInfo()
    }
}