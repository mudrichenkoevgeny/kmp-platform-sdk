package io.github.mudrichenkoevgeny.kmp.core.common.error.parser

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import org.jetbrains.compose.resources.stringResource

/**
 * Resolves the localized message for this [AppError] using the currently provided [LocalErrorParser].
 *
 * If no parser recognizes the error code, the function falls back to
 * [Res.string.error_common_unknown].
 */
@Composable
fun AppError.toLocalizedMessage(): String {
    return LocalErrorParser.current.parse(this)
        ?: stringResource(Res.string.error_common_unknown)
}