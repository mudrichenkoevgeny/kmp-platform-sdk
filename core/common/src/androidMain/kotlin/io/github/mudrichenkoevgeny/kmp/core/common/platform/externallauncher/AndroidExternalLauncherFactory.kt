package io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher

import android.content.Context
import io.github.mudrichenkoevgeny.kmp.core.common.platform.filesystem.AndroidFileOpener

/**
 * Android [getExternalLauncher] implementation.
 *
 * @param platformContext Must be an Android [Context]. Builds an [AndroidFileOpener] whose authority is
 * `"{packageName}.fileprovider"`; the host app must declare a matching `FileProvider` in the manifest.
 * @return [AndroidExternalLauncher] wired for the given context.
 */
actual fun getExternalLauncher(platformContext: Any?): ExternalLauncher {
    val context = platformContext as? Context
        ?: error("Android ExternalLauncher requires a valid Context")

    val authority = "${context.packageName}.fileprovider"

    val fileOpener = AndroidFileOpener(context, authority)

    return AndroidExternalLauncher(context, fileOpener)
}