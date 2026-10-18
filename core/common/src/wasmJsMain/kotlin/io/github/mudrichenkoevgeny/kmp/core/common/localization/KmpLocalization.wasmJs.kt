package io.github.mudrichenkoevgeny.kmp.core.common.localization

import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.CommonLocales
import kotlinx.browser.window

actual fun getDeviceLocaleTag(): String {
    return try {
        window.navigator.language
    } catch (_: Throwable) {
        CommonLocales.ENGLISH
    }
}
