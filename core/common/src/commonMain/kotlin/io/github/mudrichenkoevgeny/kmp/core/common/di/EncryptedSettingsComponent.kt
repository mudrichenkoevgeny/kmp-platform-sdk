package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.common.storage.getSettingsFactory

/**
 * Lazily creates an [EncryptedSettings] instance for a host platform.
 *
 * This component exists to decouple the SDK core from platform-specific storage
 * implementations. The actual factory selection is delegated to [getSettingsFactory]
 * using the provided `platformContext`.
 */
class EncryptedSettingsComponent(platformContext: Any?) {
    val encryptedSettings: EncryptedSettings by lazy {
        getSettingsFactory(platformContext).create()
    }
}