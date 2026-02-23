package io.github.mudrichenkoevgeny.kmp.core.common.di

import androidx.compose.runtime.staticCompositionLocalOf
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.CommonErrorParser

val LocalCommonComponent = staticCompositionLocalOf<CommonComponent> {
    error("CommonComponent not provided")
}

val LocalErrorParser = staticCompositionLocalOf<AppErrorParser> {
    CommonErrorParser
}