package io.github.mudrichenkoevgeny.kmp.core.storage.localization

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.CommonLocales
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.localization.StorageModuleStrings

/**
 * CompositionLocal holding [StorageModuleStrings] for storage UI components.
 */
public val LocalStorageStrings: ProvidableCompositionLocal<StorageModuleStrings> = staticCompositionLocalOf {
    StorageModuleStrings(KmpLocalization.resolver, CommonLocales.DEFAULT)
}
