package io.github.mudrichenkoevgeny.kmp.core.common.storage

import kotlinx.coroutines.flow.Flow

interface EncryptedSettings {
    suspend fun put(key: String, value: String)
    suspend fun get(key: String): String?
    suspend fun remove(key: String)
    fun observe(key: String): Flow<String?>
}

interface SettingsFactory {
    fun create(): EncryptedSettings
}

expect fun getSettingsFactory(platformContext: Any? = null): SettingsFactory