package io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser

object MockAppErrorParser : AppErrorParser {
    @Composable
    override fun parse(appError: AppError): String {
        return "Unknown Error"
    }
}