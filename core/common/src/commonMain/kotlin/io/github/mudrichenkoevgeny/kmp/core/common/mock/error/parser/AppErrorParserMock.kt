package io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi

/**
 * Mock implementation of [AppErrorParser] for previews/tests.
 *
 * Always returns a constant message, regardless of [AppError].
 */
@InternalApi
object AppErrorParserMock : AppErrorParser {
    @Composable
    override fun parse(appError: AppError): String {
        return "Unknown Error"
    }
}