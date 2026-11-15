package io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.upload

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.component.file.upload.StorageFileUploadComponentMock
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.StorageFileUploadComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.upload.StorageFileUploadScreenComponent

/**
 * Mock implementation of [StorageFileUploadScreenComponent] for testing and Compose previews.
 */
@InternalApi
public class StorageFileUploadScreenComponentMock(
    public override val uploadComponent: StorageFileUploadComponent = StorageFileUploadComponentMock()
) : StorageFileUploadScreenComponent {

    public var backCalls: Int = 0
    public var selectFilesCalls: Int = 0

    public override fun onBackClick() {
        backCalls++
    }

    public override fun onSelectFilesClick() {
        selectFilesCalls++
    }
}
