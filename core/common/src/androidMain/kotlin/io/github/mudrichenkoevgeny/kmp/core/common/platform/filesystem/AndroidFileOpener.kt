package io.github.mudrichenkoevgeny.kmp.core.common.platform.filesystem

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

/**
 * Opens on-disk files on Android by granting a temporary content URI through [FileProvider].
 *
 * Host integration checklist:
 * - Add `res/xml/file_paths.xml` with the directories you expose.
 * - Register a `FileProvider` in `AndroidManifest.xml` whose `android:authorities` matches the
 *   `authority` passed to this class.
 */
class AndroidFileOpener(
    private val context: Context,
    private val authority: String
) {
    /**
     * Launches a view intent for an existing local file. No-op if the path does not exist or resolution fails.
     *
     * @param filePath Absolute filesystem path to the file.
     */
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