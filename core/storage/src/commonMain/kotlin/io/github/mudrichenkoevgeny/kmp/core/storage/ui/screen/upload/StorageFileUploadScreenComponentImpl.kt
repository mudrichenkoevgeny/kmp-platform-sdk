package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.upload

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.StorageFileUploadComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.StorageFileUploadComponentImpl
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.UploadFileUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.ui.component.fileupload.StorageFileUploadUiConfig

/**
 * Default implementation of [StorageFileUploadScreenComponent].
 *
 * @param componentContext Decompose component context.
 * @param config Options and constraints for the upload queue.
 * @param uploadFileUseCase Use case for executing direct file uploads.
 * @param onUploaded Optional callback invoked when all queued uploads complete.
 * @param onSelectFiles Optional callback for system file picker trigger.
 * @param onNavigateBack Callback for back navigation.
 */
class StorageFileUploadScreenComponentImpl(
    componentContext: ComponentContext,
    config: StorageFileUploadUiConfig = StorageFileUploadUiConfig(),
    uploadFileUseCase: UploadFileUseCase,
    onUploaded: ((List<StorageFilePrivate>) -> Unit)? = null,
    private val onSelectFiles: (() -> Unit)? = null,
    private val onNavigateBack: () -> Unit
) : StorageFileUploadScreenComponent, ComponentContext by componentContext {

    override val uploadComponent: StorageFileUploadComponent = StorageFileUploadComponentImpl(
        componentContext = this,
        config = config,
        uploadFileUseCase = uploadFileUseCase,
        onUploaded = { files ->
            onUploaded?.invoke(files)
            onNavigateBack()
        }
    )

    override fun onBackClick() {
        onNavigateBack()
    }

    override fun onSelectFilesClick() {
        onSelectFiles?.invoke()
    }
}
