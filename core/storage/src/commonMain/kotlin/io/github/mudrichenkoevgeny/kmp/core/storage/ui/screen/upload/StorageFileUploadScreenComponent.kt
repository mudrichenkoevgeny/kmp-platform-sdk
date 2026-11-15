package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.upload

import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.StorageFileUploadComponent

/**
 * Presentation logic controller (Decompose component) for the full storage file upload screen.
 */
public interface StorageFileUploadScreenComponent {
    /** Embedded upload area component controller. */
    public val uploadComponent: StorageFileUploadComponent

    /** Handles click on screen back button. */
    public fun onBackClick()

    /** Handles click on dropzone to trigger system file picker. */
    public fun onSelectFilesClick()
}
