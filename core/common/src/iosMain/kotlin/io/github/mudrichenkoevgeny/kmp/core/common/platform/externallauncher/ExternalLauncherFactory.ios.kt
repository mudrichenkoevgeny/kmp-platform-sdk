package io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher

/**
 * Creates an iOS platform implementation of [ExternalLauncher].
 *
 * @param platformContext Optional platform context, unused on iOS.
 * @return An [ExternalLauncher] instance for iOS.
 */
actual fun getExternalLauncher(platformContext: Any?): ExternalLauncher {
    return IosExternalLauncher()
}
