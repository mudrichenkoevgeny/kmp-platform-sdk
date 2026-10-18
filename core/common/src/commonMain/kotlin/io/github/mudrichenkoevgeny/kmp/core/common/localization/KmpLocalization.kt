package io.github.mudrichenkoevgeny.kmp.core.common.localization

import androidx.compose.runtime.staticCompositionLocalOf
import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.CommonLocales
import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.CommonModuleStrings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.DefaultLocalizedStringResolver
import io.github.mudrichenkoevgeny.shared.foundation.core.common.localization.LocalizedStringResolver
import kotlin.concurrent.Volatile

/**
 * Singleton entry point for client localization resolution using `shared-foundation`.
 */
object KmpLocalization {

    /**
     * Shared thread-safe [LocalizedStringResolver] using embedded resources.
     */
    val resolver: LocalizedStringResolver = DefaultLocalizedStringResolver()

    /**
     * Active BCP 47 locale tag used for non-Composable resolution (e.g., error parsers).
     * Defaults to the system device locale tag and can be updated dynamically at runtime.
     */
    @Volatile
    var currentLocale: String = getDeviceLocaleTag()
}

/**
 * Expect function to retrieve current device or system active BCP 47 locale tag.
 *
 * @return Active locale tag (e.g., [CommonLocales.ENGLISH], [CommonLocales.RUSSIAN], "en-US").
 */
expect fun getDeviceLocaleTag(): String

/**
 * CompositionLocal holding [CommonModuleStrings] for UI components.
 */
val LocalCommonStrings = staticCompositionLocalOf {
    CommonModuleStrings(KmpLocalization.resolver, CommonLocales.DEFAULT)
}
