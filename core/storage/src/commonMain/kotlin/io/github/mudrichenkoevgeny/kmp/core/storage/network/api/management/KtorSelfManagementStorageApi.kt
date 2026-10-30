package io.github.mudrichenkoevgeny.kmp.core.storage.network.api.management

import io.github.mudrichenkoevgeny.kmp.core.common.network.httprequest.parameters
import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.base.StorageApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.presigned.PresignedUrl
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.file.toStorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.file.toStorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.presigned.toPresignedUrl
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.open.list.SelfStorageFileListParams
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
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [StorageApi] implementation for staff self-service storage backed by Ktor [HttpClient]. */
class KtorSelfManagementStorageApi(
    private val client: HttpClient
) : StorageApi {

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

    override suspend fun getFiles(params: SelfStorageFileListParams?): AppResult<PagedResult<StorageFileSummary>> =
        client.callResult<PagedResult<StorageFileSummaryPayload>> {
            get(SelfManagementStorageRoutes.GET_FILES) {
                parameters(params)
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
