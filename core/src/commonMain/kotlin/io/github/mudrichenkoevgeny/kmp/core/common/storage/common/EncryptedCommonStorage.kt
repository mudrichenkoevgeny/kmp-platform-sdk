package io.github.mudrichenkoevgeny.kmp.core.common.storage.common

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings

/**
 * Default [CommonStorage] implementation backed by an [EncryptedSettings] store.
 */
public class EncryptedCommonStorage(
    private val encryptedSettings: EncryptedSettings
) : CommonStorage {

    public override suspend fun getDeviceId(): String? = encryptedSettings.get(KEY_DEVICE_ID)

    public override suspend fun updateDeviceId(deviceId: String) {
        encryptedSettings.put(KEY_DEVICE_ID, deviceId)
    }

    public override suspend fun clear() {
        encryptedSettings.remove(KEY_DEVICE_ID)
    }

    private companion object {
        private const val KEY_DEVICE_ID = "device_id"
    }
}
