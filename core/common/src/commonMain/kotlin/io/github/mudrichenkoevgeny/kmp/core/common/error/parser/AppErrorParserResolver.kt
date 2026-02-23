package io.github.mudrichenkoevgeny.kmp.core.common.error.parser

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * A resolver that coordinates error parsing across multiple modules.
 * This class implements a prioritized Chain of Responsibility pattern to transform an [AppError]
 * into a user-friendly localized string.
 *
 * Execution Order:
 * 1. Iterates through the [specificParsers] list from start to finish (index 0 onwards).
 * The first parser that returns a non-null string terminates the search and its result is used.
 * 2. If no specific parser recognizes the error code, the request is delegated to the [commonParser] parser.
 * 3. The [commonParser] parser acts as the final authority (fallback), ensuring that even unrecognized
 * errors result in a generic localized message (e.g., "Unknown error").
 *
 * @param commonParser The foundational parser for system-wide errors (Network, Internal, etc.).
 * @param specificParsers A list of feature-specific parsers (e.g., User, Payments).
 * Parsers at the beginning of the list have higher priority.
 */
internal class AppErrorParserResolver(
    private val commonParser: AppErrorParser,
    private val specificParsers: List<AppErrorParser> = emptyList()
) : AppErrorParser {

    @Composable
    override fun parse(appError: AppError): String? {
        for (parser in specificParsers) {
            val message = parser.parse(appError)
            if (message != null) {
                return message
            }
        }

        return commonParser.parse(appError)
    }
}