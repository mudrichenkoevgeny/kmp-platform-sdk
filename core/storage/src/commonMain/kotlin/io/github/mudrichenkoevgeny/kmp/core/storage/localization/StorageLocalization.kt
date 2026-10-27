package io.github.mudrichenkoevgeny.kmp.core.storage.localization

import androidx.compose.runtime.staticCompositionLocalOf
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.CommonLocales
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.localization.StorageModuleStrings

/**
 * CompositionLocal holding [StorageModuleStrings] for storage UI components.
 */
val LocalStorageStrings = staticCompositionLocalOf {
    StorageModuleStrings(KmpLocalization.resolver, CommonLocales.DEFAULT)
}
