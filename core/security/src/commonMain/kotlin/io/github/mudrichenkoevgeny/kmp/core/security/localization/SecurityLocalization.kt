package io.github.mudrichenkoevgeny.kmp.core.security.localization

import androidx.compose.runtime.staticCompositionLocalOf
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.CommonLocales
import io.github.mudrichenkoevgeny.shared.foundation.core.security.localization.SecurityModuleStrings

/**
 * CompositionLocal holding [SecurityModuleStrings] for security UI components.
 */
val LocalSecurityStrings = staticCompositionLocalOf {
    SecurityModuleStrings(KmpLocalization.resolver, CommonLocales.DEFAULT)
}
