package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload

import androidx.compose.runtime.Composable

/**
 * Controller interface triggering native platform file selection and drag-and-drop operations.
 */
interface FilePickerLauncher {
    /**
     * Launches native or browser file picker dialog.
     */
    fun launch()
}

/**
 * Creates and remembers a platform-specific [FilePickerLauncher] producing [SelectedFilePayload] items.
 *
 * @param onFilesSelected Callback invoked with converted file payloads when files are selected or dropped.
 */
@Composable
expect fun rememberFilePickerLauncher(
    onFilesSelected: (List<SelectedFilePayload>) -> Unit
): FilePickerLauncher
