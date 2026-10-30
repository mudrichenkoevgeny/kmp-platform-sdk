package io.github.mudrichenkoevgeny.kmp.core.storage.di

import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.DeleteFileUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.DeleteFilesBatchUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.GetFileDetailUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.GetFilesUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.base.UploadFileUseCase

/**
 * Bundles storage file use cases required for cross-module wiring and file operations.
 *
 * @property getFilesUseCase Use case fetching paged storage files.
 * @property getFileDetailUseCase Use case fetching storage file details.
 * @property uploadFileUseCase Use case uploading a storage file.
 * @property deleteFileUseCase Use case deleting a single storage file.
 * @property deleteFilesBatchUseCase Use case deleting storage files in batch.
 */
data class OpenStorageDependencies(
    val getFilesUseCase: GetFilesUseCase,
    val getFileDetailUseCase: GetFileDetailUseCase,
    val uploadFileUseCase: UploadFileUseCase,
    val deleteFileUseCase: DeleteFileUseCase,
    val deleteFilesBatchUseCase: DeleteFilesBatchUseCase
)
