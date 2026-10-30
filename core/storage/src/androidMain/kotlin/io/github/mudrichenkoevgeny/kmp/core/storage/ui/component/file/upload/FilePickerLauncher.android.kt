package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import co.touchlab.kermit.Logger
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private class AndroidFilePickerLauncher(
    private val onLaunch: () -> Unit
) : FilePickerLauncher {
    override fun launch() {
        onLaunch()
    }
}

@Composable
actual fun rememberFilePickerLauncher(
    onFilesSelected: (List<SelectedFilePayload>) -> Unit
): FilePickerLauncher {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            scope.launch {
                val payloads = withContext(Dispatchers.IO) {
                    uris.mapNotNull { uri -> createPayloadFromUri(context, uri) }
                }
                if (payloads.isNotEmpty()) {
                    onFilesSelected(payloads)
                }
            }
        }
    }

    return remember {
        AndroidFilePickerLauncher(
            onLaunch = { launcher.launch("*/*") }
        )
    }
}

private fun createPayloadFromUri(context: Context, uri: Uri): SelectedFilePayload? {
    return try {
        var fileName = "file"
        var fileSize = 0L
        val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"

        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (nameIndex >= 0) {
                    fileName = cursor.getString(nameIndex) ?: "file"
                }
                if (sizeIndex >= 0) {
                    fileSize = cursor.getLong(sizeIndex)
                }
            }
        }

        SelectedFilePayload(
            fileName = fileName,
            fileSizeBytes = fileSize,
            mimeType = mimeType,
            localUri = uri.toString(),
            openReadChannel = {
                val inputStream = context.contentResolver.openInputStream(uri)
                    ?: throw IllegalStateException("Cannot open input stream for $uri")
                inputStream.toByteReadChannel()
            }
        )
    } catch (e: Exception) {
        Logger.e(e) { "Failed to resolve file payload from URI: $uri" }
        null
    }
}
