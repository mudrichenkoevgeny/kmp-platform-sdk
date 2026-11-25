package io.github.mudrichenkoevgeny.kmp.core.common.storage

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
