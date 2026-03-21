package io.github.mudrichenkoevgeny.kmp.core.common.storage.common

/**
 * Storage abstraction for device/platform-scoped data.
 */
interface CommonStorage {
    /**
     * Reads the stored device id.
     *
     * @return device id or `null` if not set.
     */
    suspend fun getDeviceId(): String?

    /**
     * Updates the stored device id.
     *
     * @param deviceId New device identifier to persist.
     */
    suspend fun updateDeviceId(deviceId: String)

    /**
     * Clears stored device data.
     */
    suspend fun clear()
}