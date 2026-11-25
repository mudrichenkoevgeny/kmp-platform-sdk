package io.github.mudrichenkoevgeny.kmp.core.common.storage.common

/**
 * Storage abstraction for device/platform-scoped data.
 */
public interface CommonStorage {
    /**
     * Reads the stored device id.
     *
     * @return device id or `null` if not set.
     */
    public suspend fun getDeviceId(): String?

    /**
     * Updates the stored device id.
     *
     * @param deviceId New device identifier to persist.
     */
    public suspend fun updateDeviceId(deviceId: String)

    /**
     * Clears stored device data.
     */
    public suspend fun clear()
}
