package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser

internal class CommonErrorModule(
    val appErrorParser: AppErrorParser
)