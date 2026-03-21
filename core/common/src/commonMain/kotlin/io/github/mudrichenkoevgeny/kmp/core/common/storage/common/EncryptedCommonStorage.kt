package io.github.mudrichenkoevgeny.kmp.core.common.storage.common

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings

/**
 * Default [CommonStorage] implementation backed by an [EncryptedSettings] store.
 */
class EncryptedCommonStorage(
    private val encryptedSettings: EncryptedSettings
) : CommonStorage {

    override suspend fun getDeviceId(): String? {
        return encryptedSettings.get(KEY_DEVICE_ID)
    }

    override suspend fun updateDeviceId(deviceId: String) {
        encryptedSettings.put(KEY_DEVICE_ID, deviceId)
    }

    override suspend fun clear() {
        encryptedSettings.remove(KEY_DEVICE_ID)
    }

    companion object {
        private const val KEY_DEVICE_ID = "device_id"
    }
}