package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher.ExternalLauncher
import io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher.getExternalLauncher

/**
 * Internal platform wiring for `core/common`.
 *
 * The SDK needs a minimal `platformContext` to construct platform-specific implementations.
 * This module converts it into concrete abstractions (currently [ExternalLauncher]).
 */
internal class CommonPlatformModule(
    platformContext: Any?
) {
    /**
     * Platform-specific launcher for opening external resources.
     */
    val externalLauncher: ExternalLauncher by lazy {
        getExternalLauncher(platformContext)
    }
}