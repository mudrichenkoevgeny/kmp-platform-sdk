package io.github.mudrichenkoevgeny.kmp.core.common.storage.common

interface CommonStorage {
    suspend fun getDeviceId(): String?
    suspend fun updateDeviceId(deviceId: String)
    suspend fun clear()
}