package io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser

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
    override fun parse(appError: AppError): String {
        return "Unknown Error"
    }
}
