package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.storage

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.ListingParamNames
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.listing.StorageSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.presigned.PresignedUrl
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.status.StorageFileStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.file.toStorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.file.toStorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.presigned.toPresignedUrl
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.contract.StorageApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.file.StorageFilePrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.file.StorageFileSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.presigned.PresignedUrlPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.presigned.PresignedUrlRequest
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.route.management.self.SelfManagementStorageRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [SelfManagementStorageApi] implementation backed by Ktor [HttpClient]. */
class KtorSelfManagementStorageApi(
    private val client: HttpClient
) : SelfManagementStorageApi {

    override suspend fun getPresignedUploadUrl(request: PresignedUrlRequest): AppResult<PresignedUrl> =
        client.callResult<PresignedUrlPayload> {
            post(SelfManagementStorageRoutes.POST_PRESIGNED_UPLOAD_URL) {
                setBody(request)
            }
        }.mapSuccess { it.toPresignedUrl() }

    override suspend fun confirmUpload(fileId: String): AppResult<StorageFilePrivate> {
        val path = SelfManagementStorageRoutes.CONFIRM_UPLOAD.replace("{${StorageApiPaths.FILE_ID}}", fileId)
        return client.callResult<StorageFilePrivatePayload> {
            post(path)
        }.mapSuccess { it.toStorageFilePrivate() }
    }

    override suspend fun getFiles(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: StorageSortValues.StorageFileSortBy?,
        sortOrder: SortOrder?,
        fileIds: List<String>?,
        fileName: String?,
        mimeTypes: List<String>?,
        categories: List<StorageCategory>?,
        visibilities: List<StorageFileVisibility>?,
        statuses: List<StorageFileStatus>?,
        targetScopes: List<String>?
    ): AppResult<PagedResult<StorageFileSummary>> =
        client.callResult<PagedResult<StorageFileSummaryPayload>> {
            get(SelfManagementStorageRoutes.GET_FILES) {
                parameter(ListingParamNames.Pagination.PAGE_NUMBER, pageNumber)
                parameter(ListingParamNames.Pagination.PAGE_SIZE, pageSize)
                parameter(ListingParamNames.Sort.SORT_BY, sortBy?.value)
                parameter(ListingParamNames.Sort.SORT_ORDER, sortOrder?.value)
                fileIds?.forEach { id ->
                    parameter(StorageFilterValues.StorageFileFilterValues.FILE_ID, id)
                }
                parameter(StorageFilterValues.StorageFileFilterValues.FILE_NAME, fileName)
                mimeTypes?.forEach { mime ->
                    parameter(StorageFilterValues.StorageFileFilterValues.MIME_TYPE, mime)
                }
                categories?.forEach { cat ->
                    parameter(StorageFilterValues.StorageFileFilterValues.CATEGORY, cat.value)
                }
                visibilities?.forEach { vis ->
                    parameter(StorageFilterValues.StorageFileFilterValues.VISIBILITY, vis.value)
                }
                statuses?.forEach { stat ->
                    parameter(StorageFilterValues.StorageFileFilterValues.STATUS, stat.value)
                }
                targetScopes?.forEach { scope ->
                    parameter(StorageFilterValues.StorageFileFilterValues.TARGET_SCOPE, scope)
                }
            }
        }.mapSuccess { pagedResult ->
            pagedResult.mapItems { it.toStorageFileSummary() }
        }

    override suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate> {
        val path = SelfManagementStorageRoutes.GET_FILE.replace("{${StorageApiPaths.FILE_ID}}", fileId)
        return client.callResult<StorageFilePrivatePayload> {
            get(path)
        }.mapSuccess { it.toStorageFilePrivate() }
    }

    override suspend fun deleteFile(fileId: String): AppResult<Unit> {
        val path = SelfManagementStorageRoutes.DELETE_FILE.replace("{${StorageApiPaths.FILE_ID}}", fileId)
        return client.callResult {
            delete(path)
        }
    }

    override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> =
        client.callResult {
            post(SelfManagementStorageRoutes.DELETE_FILES_BATCH) {
                setBody(request)
            }
        }
}
