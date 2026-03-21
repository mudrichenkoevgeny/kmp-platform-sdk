package io.github.mudrichenkoevgeny.kmp.core.common.storage

import kotlinx.browser.window
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Browser [EncryptedSettings] backed by [window] `localStorage` (same-origin, survives reloads; not encrypted at rest).
 *
 * [observe] emits the current value for a key, then re-emits whenever [put] or [remove] runs on **any** key,
 * because storage has no per-key subscription API in this implementation.
 */
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

/**
 * Wasm [getSettingsFactory]: returns [WasmEncryptedSettings] scoped to the current browsing origin.
 *
 * @param platformContext Unused on Wasm (kept for the shared expect/actual signature).
 */
actual fun getSettingsFactory(platformContext: Any?): SettingsFactory = object : SettingsFactory {
    override fun create(): EncryptedSettings {
        return WasmEncryptedSettings()
    }
}