package io.github.mudrichenkoevgeny.kmp.feature.user.localization

import androidx.compose.runtime.staticCompositionLocalOf
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.CommonLocales
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.localization.UserModuleStrings

/**
 * CompositionLocal holding [UserModuleStrings] for user UI components and screens.
 */
val LocalUserStrings = staticCompositionLocalOf {
    UserModuleStrings(KmpLocalization.resolver, CommonLocales.DEFAULT)
}
