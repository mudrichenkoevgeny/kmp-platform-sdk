package io.github.mudrichenkoevgeny.kmp.core.common.localization

import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.CommonLocales
import kotlinx.browser.window

/**
 * Returns the device language locale tag for the WasmJs target from browser navigation properties.
 *
 * @return The locale tag string.
 */
actual fun getDeviceLocaleTag(): String {
    return try {
        window.navigator.language
    } catch (_: Throwable) {
        CommonLocales.ENGLISH
    }
}
