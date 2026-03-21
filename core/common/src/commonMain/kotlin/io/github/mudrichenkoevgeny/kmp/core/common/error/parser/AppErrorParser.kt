package io.github.mudrichenkoevgeny.kmp.core.common.error.parser

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * Maps an [AppError] into a user-facing, localized message.
 *
 * A parser may decide whether it recognizes a particular [AppError.code].
 * If the parser cannot handle the error, it should return `null` so that the
 * resolver can delegate to the fallback parser.
 */
interface AppErrorParser {
    /**
     * @param appError Error instance to localize.
     * @return Localized message for the provided [appError], or `null` when this parser does not recognize the error code.
     */
    @Composable fun parse(appError: AppError): String?
}