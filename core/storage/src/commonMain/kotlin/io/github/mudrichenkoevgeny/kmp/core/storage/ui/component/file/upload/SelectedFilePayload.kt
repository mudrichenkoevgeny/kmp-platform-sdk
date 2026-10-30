package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload

import io.ktor.utils.io.ByteReadChannel

/**
 * Represents a selected binary file payload ready to be processed in the upload queue.
 *
 * @property fileName Name of the selected file.
 * @property fileSizeBytes Size of the file payload in bytes.
 * @property mimeType MIME type of the file.
 * @property localUri Local platform URI string for previewing the file.
 * @property openReadChannel Suspending function to defer opening a [ByteReadChannel] stream for reading binary content.
 */
class SelectedFilePayload(
    val fileName: String,
    val fileSizeBytes: Long,
    val mimeType: String,
    val localUri: String?,
    val openReadChannel: suspend () -> ByteReadChannel
)
