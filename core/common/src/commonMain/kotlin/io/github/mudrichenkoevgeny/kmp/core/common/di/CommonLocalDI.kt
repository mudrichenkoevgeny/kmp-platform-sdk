package io.github.mudrichenkoevgeny.kmp.core.common.di

import androidx.compose.runtime.staticCompositionLocalOf
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.CommonErrorParser

/**
 * Composition-local holder for the root SDK wiring component.
 *
 * UI should read this value only from composition scopes that provided `LocalCommonComponent`;
 * otherwise access will throw.
 */
val LocalCommonComponent = staticCompositionLocalOf<CommonComponent> {
    error("CommonComponent not provided")
}

/**
 * Composition-local holder for the resolved root [AppErrorParser].
 *
 * If not provided by the host app, this defaults to [CommonErrorParser] to keep the UI functional.
 */
val LocalErrorParser = staticCompositionLocalOf<AppErrorParser> {
    CommonErrorParser
}