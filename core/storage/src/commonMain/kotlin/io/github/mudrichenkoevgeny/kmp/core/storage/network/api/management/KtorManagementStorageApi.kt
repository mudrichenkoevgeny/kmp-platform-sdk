package io.github.mudrichenkoevgeny.kmp.core.storage.network.api.management

import io.github.mudrichenkoevgeny.kmp.core.common.network.httprequest.parameters
import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.file.toStorageFilePrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.mapper.file.toStorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.navigation.screen.management.list.ManagementStorageFileListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.contract.StorageApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.file.StorageFilePrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.file.StorageFileSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.request.file.DeleteFilesBatchRequest
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.route.management.ManagementStorageRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [ManagementStorageApi] implementation backed by Ktor [HttpClient]. */
class KtorManagementStorageApi(
    private val client: HttpClient
) : ManagementStorageApi {

    override suspend fun getFiles(params: ManagementStorageFileListParams?): AppResult<PagedResult<StorageFileSummary>> =
        client.callResult<PagedResult<StorageFileSummaryPayload>> {
            get(ManagementStorageRoutes.GET_FILES) {
                parameters(params)
            }
        }.mapSuccess { pagedResult ->
            pagedResult.mapItems { it.toStorageFileSummary() }
        }

    override suspend fun getFileDetail(fileId: String): AppResult<StorageFilePrivate> {
        val path = ManagementStorageRoutes.GET_FILE.replace("{${StorageApiPaths.FILE_ID}}", fileId)
        return client.callResult<StorageFilePrivatePayload> {
            get(path)
        }.mapSuccess { it.toStorageFilePrivate() }
    }

    override suspend fun deleteFile(fileId: String): AppResult<Unit> {
        val path = ManagementStorageRoutes.DELETE_FILE.replace("{${StorageApiPaths.FILE_ID}}", fileId)
        return client.callResult {
            delete(path)
        }
    }

    override suspend fun deleteFilesBatch(request: DeleteFilesBatchRequest): AppResult<Unit> =
        client.callResult {
            post(ManagementStorageRoutes.DELETE_FILES_BATCH) {
                setBody(request)
            }
        }
}
