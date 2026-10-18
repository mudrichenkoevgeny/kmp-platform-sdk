package io.github.mudrichenkoevgeny.kmp.core.common.error.parser

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings

/**
 * Resolves the localized message for this [AppError] using the currently provided [LocalErrorParser].
 *
 * If no parser recognizes the error code, the function falls back to common unknown error message.
 */
@Composable
fun AppError.toLocalizedMessage(): String {
    return LocalErrorParser.current.parse(this)
        ?: LocalCommonStrings.current.errors.unknown
}
