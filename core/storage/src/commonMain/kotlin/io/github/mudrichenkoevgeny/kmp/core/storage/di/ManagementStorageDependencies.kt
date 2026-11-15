package io.github.mudrichenkoevgeny.kmp.core.storage.di

import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.UploadFileUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementDeleteFileUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementDeleteFilesBatchUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementGetFileDetailUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementGetFilesUseCase

/**
 * Bundles management storage file use cases required for administrative storage operations.
 *
 * @property getFilesUseCase Administrative use case fetching paged storage files.
 * @property getFileDetailUseCase Administrative use case fetching storage file details.
 * @property uploadFileUseCase Direct upload use case.
 * @property deleteFileUseCase Administrative use case deleting a single storage file.
 * @property deleteFilesBatchUseCase Administrative use case deleting storage files in batch.
 */
public data class ManagementStorageDependencies(
    public val getFilesUseCase: ManagementGetFilesUseCase,
    public val getFileDetailUseCase: ManagementGetFileDetailUseCase,
    public val uploadFileUseCase: UploadFileUseCase,
    public val deleteFileUseCase: ManagementDeleteFileUseCase,
    public val deleteFilesBatchUseCase: ManagementDeleteFilesBatchUseCase
)
