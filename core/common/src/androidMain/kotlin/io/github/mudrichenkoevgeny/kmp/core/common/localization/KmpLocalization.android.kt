package io.github.mudrichenkoevgeny.kmp.core.common.localization

import java.util.Locale

/**
 * Returns the IETF BCP 47 language tag representation of the default device locale on Android.
 */
public actual fun getDeviceLocaleTag(): String = Locale.getDefault().toLanguageTag()
