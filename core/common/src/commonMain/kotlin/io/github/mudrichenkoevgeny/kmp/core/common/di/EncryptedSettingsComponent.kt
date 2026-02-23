package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.common.storage.getSettingsFactory

class EncryptedSettingsComponent(platformContext: Any?) {
    val encryptedSettings: EncryptedSettings by lazy {
        getSettingsFactory(platformContext).create()
    }
}