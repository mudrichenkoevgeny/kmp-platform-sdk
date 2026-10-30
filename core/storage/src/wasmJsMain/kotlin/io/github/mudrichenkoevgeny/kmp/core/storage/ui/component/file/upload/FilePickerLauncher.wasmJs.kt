package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import io.ktor.utils.io.ByteReadChannel
import kotlinx.browser.document
import kotlinx.coroutines.suspendCancellableCoroutine
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.get
import org.w3c.dom.DragEvent
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.File
import org.w3c.files.FileReader
import org.w3c.dom.url.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.js.ExperimentalWasmJsInterop

private class WasmFilePickerLauncher(
    private val onLaunch: () -> Unit
) : FilePickerLauncher {
    override fun launch() {
        onLaunch()
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
actual fun rememberFilePickerLauncher(
    onFilesSelected: (List<SelectedFilePayload>) -> Unit
): FilePickerLauncher {

    fun processJsFiles(files: List<File>) {
        if (files.isEmpty()) {
            return
        }
        val payloads = files.map { file ->
            val fileName = file.name
            val fileSize = file.size.toInt().toLong()
            val mimeType = file.type.ifBlank { "application/octet-stream" }
            val localUri = URL.createObjectURL(file)

            SelectedFilePayload(
                fileName = fileName,
                fileSizeBytes = fileSize,
                mimeType = mimeType,
                localUri = localUri,
                openReadChannel = {
                    val bytes = readFileAsByteArray(file)
                    ByteReadChannel(bytes)
                }
            )
        }
        onFilesSelected(payloads)
    }

    DisposableEffect(Unit) {
        val onDragOver = { event: Event ->
            event.preventDefault()
            event.stopPropagation()
        }

        val onDrop = { event: Event ->
            event.preventDefault()
            event.stopPropagation()
            val dragEvent = event as? DragEvent
            val dataTransfer = dragEvent?.dataTransfer
            if (dataTransfer != null) {
                val fileList = dataTransfer.files
                val filesList = mutableListOf<File>()
                for (i in 0 until fileList.length) {
                    fileList.item(i)?.let { filesList.add(it) }
                }
                processJsFiles(filesList)
            }
        }

        document.addEventListener("dragover", onDragOver)
        document.addEventListener("drop", onDrop)

        onDispose {
            document.removeEventListener("dragover", onDragOver)
            document.removeEventListener("drop", onDrop)
        }
    }

    return remember {
        WasmFilePickerLauncher(
            onLaunch = {
                val input = document.createElement("input") as HTMLInputElement
                input.type = "file"
                input.multiple = true
                input.onchange = {
                    val fileList = input.files
                    if (fileList != null) {
                        val filesList = mutableListOf<File>()
                        for (i in 0 until fileList.length) {
                            fileList.item(i)?.let { filesList.add(it) }
                        }
                        processJsFiles(filesList)
                    }
                }
                input.click()
            }
        )
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private suspend fun readFileAsByteArray(file: File): ByteArray = suspendCancellableCoroutine { continuation ->
    val reader = FileReader()
    reader.onload = {
        val result = reader.result
        if (result != null) {
            val arrayBuffer = result.unsafeCast<ArrayBuffer>()
            val int8Array = Int8Array(arrayBuffer)
            val bytes = ByteArray(int8Array.length) { index -> int8Array[index] }
            continuation.resume(bytes)
        } else {
            continuation.resumeWithException(IllegalStateException("FileReader result was null"))
        }
    }
    reader.onerror = {
        continuation.resumeWithException(IllegalStateException("FileReader failed to read file"))
    }
    reader.readAsArrayBuffer(file)
}
