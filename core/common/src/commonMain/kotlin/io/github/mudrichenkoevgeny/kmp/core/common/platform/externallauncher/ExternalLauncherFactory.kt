package io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher

/**
 * Creates the [ExternalLauncher] implementation for the current platform.
 *
 * The actual factory is provided by platform source sets (Android/iOS/Wasm),
 * while the common code just depends on the returned abstraction.
 */
expect fun getExternalLauncher(platformContext: Any?): ExternalLauncher