package io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher

/**
 * Wasm [getExternalLauncher]: returns a stateless [WasmExternalLauncher].
 *
 * @param platformContext Unused on Wasm (kept for the shared expect/actual signature).
 */
actual fun getExternalLauncher(platformContext: Any?): ExternalLauncher {
    return WasmExternalLauncher()
}