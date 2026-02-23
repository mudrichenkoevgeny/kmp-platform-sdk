package io.github.mudrichenkoevgeny.kmp.core.common.platform.externallauncher

import android.content.Context
import io.github.mudrichenkoevgeny.kmp.core.common.platform.filesystem.AndroidFileOpener

actual fun getExternalLauncher(platformContext: Any?): ExternalLauncher {
    val context = platformContext as? Context
        ?: error("Android ExternalLauncher requires a valid Context")

    val authority = "${context.packageName}.fileprovider"

    val fileOpener = AndroidFileOpener(context, authority)

    return AndroidExternalLauncher(context, fileOpener)
}