package io.github.mudrichenkoevgeny.kmp.core.common.platform.filesystem

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

/**
 * Helper for opening local files on Android using [FileProvider].
 * Requirements for usage:
 * 1. Provide a 'res/xml/file_paths.xml' with appropriate paths.
 * 2. Add a FileProvider to AndroidManifest.xml.
 * @param context Android context for starting activities.
 * @param authority Should match the authority defined in AndroidManifest.xml.
 */
class AndroidFileOpener(
    private val context: Context,
    private val authority: String
) {
    fun openLocalFile(filePath: String) {
        runCatching {
            val file = File(filePath)
            if (!file.exists()) {
                return
            }

            val contentUri = FileProvider.getUriForFile(context, authority, file)

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(contentUri, context.contentResolver.getType(contentUri))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)
        }
    }
}