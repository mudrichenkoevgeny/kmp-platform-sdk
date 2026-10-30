package io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.upload

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.component.file.upload.StorageFileUploadComponentMock
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.StorageFileUploadComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.upload.StorageFileUploadScreenComponent

/**
 * Mock implementation of [StorageFileUploadScreenComponent] for testing and Compose previews.
 */
@InternalApi
class StorageFileUploadScreenComponentMock(
    override val uploadComponent: StorageFileUploadComponent = StorageFileUploadComponentMock()
) : StorageFileUploadScreenComponent {

    var backCalls = 0
    var selectFilesCalls = 0

    override fun onBackClick() {
        backCalls++
    }

    override fun onSelectFilesClick() {
        selectFilesCalls++
    }
}
