package io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher

actual fun getExternalLauncher(platformContext: Any?): ExternalLauncher {
    return IosExternalLauncher()
}