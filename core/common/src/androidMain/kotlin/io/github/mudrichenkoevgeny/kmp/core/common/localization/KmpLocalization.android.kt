package io.github.mudrichenkoevgeny.kmp.core.common.localization

import java.util.Locale

actual fun getDeviceLocaleTag(): String = Locale.getDefault().toLanguageTag()
