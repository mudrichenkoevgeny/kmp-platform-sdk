package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload

import androidx.compose.runtime.Composable

/**
 * Controller interface triggering native platform file selection and drag-and-drop operations.
 */
public interface FilePickerLauncher {
    /**
     * Launches native or browser file picker dialog.
     */
    public fun launch(): Unit
}

/**
 * Creates and remembers a platform-specific [FilePickerLauncher] producing [SelectedFilePayload] items.
 *
 * @param onFilesSelect Callback invoked with converted file payloads when files are selected or dropped.
 */
@Composable
public expect fun rememberFilePickerLauncher(
    onFilesSelect: (List<SelectedFilePayload>) -> Unit
): FilePickerLauncher
