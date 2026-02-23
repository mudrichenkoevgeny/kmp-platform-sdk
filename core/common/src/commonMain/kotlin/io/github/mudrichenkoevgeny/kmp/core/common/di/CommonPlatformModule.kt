package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher.ExternalLauncher
import io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher.getExternalLauncher

internal class CommonPlatformModule(
    platformContext: Any?
) {
    val externalLauncher: ExternalLauncher by lazy {
        getExternalLauncher(platformContext)
    }
}