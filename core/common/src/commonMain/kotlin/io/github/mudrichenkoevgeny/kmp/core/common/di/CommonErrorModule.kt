package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.AppErrorParser

/**
 * Internal wiring holder for the resolved root [AppErrorParser].
 *
 * The root parser is constructed in [CommonComponent.init]
 * and exposed to UI layers via composition locals.
 */
internal class CommonErrorModule(
    val appErrorParser: AppErrorParser
)