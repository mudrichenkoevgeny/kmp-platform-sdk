package io.github.mudrichenkoevgeny.kmp.core.common.storage

import kotlinx.coroutines.flow.Flow

/**
 * Encrypted key/value storage abstraction used by the SDK.
 *
 * Implementations are platform-specific (Android DataStore, Wasm local storage, etc.).
 */
public interface EncryptedSettings {
    /**
     * Stores a string value for the provided key.
     *
     * @param key Storage key.
     * @param value Value to store.
     */
    public suspend fun put(
        key: String,
        value: String
    )

    /**
     * Reads a value for the provided key.
     *
     * @param key Storage key.
     * @return Stored value, or `null` if missing.
     */
    public suspend fun get(key: String): String?

    /**
     * Removes a value for the provided key (effectively setting it to `null`).
     *
     * @param key Storage key.
     */
    public suspend fun remove(key: String)

    /**
     * Observes value changes for a given key.
     *
     * @param key Storage key.
     * @return Cold or hot flow of values; emits `null` when the key is absent.
     */
    public fun observe(key: String): Flow<String?>
}

/**
 * Factory for creating an [EncryptedSettings] instance for a platform.
 */
public interface SettingsFactory {
    /**
     * Creates a new [EncryptedSettings] instance.
     */
    public fun create(): EncryptedSettings
}

/**
 * Platform entry point for selecting the [SettingsFactory].
 *
 * The actual factory implementation is provided by platform source sets.
 */
public expect fun getSettingsFactory(platformContext: Any? = null): SettingsFactory
