package io.github.mudrichenkoevgeny.kmp.core.common.error.parser

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

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

/**
 * Resolves a localized string by checking for the existence of a specific argument.
 *
 * @param args The map of arguments provided with the error.
 * @param key The key to look for in the [args] map.
 * @param withArgsRes The [StringResource] to use if the key exists and is not blank.
 * @param fallbackRes The [StringResource] to use as a fallback.
 */
@Composable
fun AppErrorParser.resolveLocalizedString(
    args: Map<String, String>,
    key: String,
    withArgsRes: StringResource,
    fallbackRes: StringResource
): String {
    val value = args[key]
    return if (!value.isNullOrBlank()) {
        stringResource(withArgsRes, value)
    } else {
        stringResource(fallbackRes)
    }
}