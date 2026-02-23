package io.github.mudrichenkoevgeny.kmp.core.common.error.parser

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppError.toLocalizedMessage(): String {
    return LocalErrorParser.current.parse(this)
        ?: stringResource(Res.string.error_common_unknown)
}