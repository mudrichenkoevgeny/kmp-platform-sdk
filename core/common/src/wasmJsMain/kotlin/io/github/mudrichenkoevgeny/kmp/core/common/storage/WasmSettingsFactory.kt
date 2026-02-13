package io.github.mudrichenkoevgeny.kmp.core.common.storage

import kotlinx.browser.window
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class WasmEncryptedSettings : EncryptedSettings {

    private val _updates = MutableStateFlow(0)

    override suspend fun put(key: String, value: String) {
        window.localStorage.setItem(key, value)
        _updates.value += 1
    }

    override suspend fun get(key: String): String? {
        return window.localStorage.getItem(key)
    }

    override suspend fun remove(key: String) {
        window.localStorage.removeItem(key)
        _updates.value += 1
    }

    override fun observe(key: String): Flow<String?> {
        return kotlinx.coroutines.flow.flow {
            emit(get(key))
            _updates.collect {
                emit(get(key))
            }
        }
    }
}

actual fun getSettingsFactory(platformContext: Any?): SettingsFactory = object : SettingsFactory {
    override fun create(): EncryptedSettings {
        return WasmEncryptedSettings()
    }
}