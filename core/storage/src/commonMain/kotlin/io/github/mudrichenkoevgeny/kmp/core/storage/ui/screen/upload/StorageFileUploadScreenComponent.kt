package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.upload

import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.StorageFileUploadComponent

/**
 * Presentation logic controller (Decompose component) for the full storage file upload screen.
 */
interface StorageFileUploadScreenComponent {
    /** Embedded upload area component controller. */
    val uploadComponent: StorageFileUploadComponent

    /** Handles click on screen back button. */
    fun onBackClick()

    /** Handles click on dropzone to trigger system file picker. */
    fun onSelectFilesClick()
}
