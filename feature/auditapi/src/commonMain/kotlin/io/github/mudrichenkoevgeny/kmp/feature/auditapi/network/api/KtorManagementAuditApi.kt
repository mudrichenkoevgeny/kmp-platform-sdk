package io.github.mudrichenkoevgeny.kmp.feature.auditapi.network.api

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.ListingParamNames
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.contract.AuditApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.model.event.AuditEventPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.auditapi.network.route.management.ManagementAuditRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Ktor-backed implementation of [ManagementAuditApi].
 *
 * @param client Shared [HttpClient].
 */
class KtorManagementAuditApi(
    private val client: HttpClient
) : ManagementAuditApi {
    override suspend fun getAuditEvents(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: AuditSortValues.AuditEventSortBy?,
        sortOrder: SortOrder?
    ): AppResult<PagedResult<AuditEventPayload>> = client.callResult {
        get(ManagementAuditRoutes.GET_AUDIT_EVENTS) {
            parameter(ListingParamNames.Pagination.PAGE_NUMBER, pageNumber)
            parameter(ListingParamNames.Pagination.PAGE_SIZE, pageSize)
            parameter(ListingParamNames.Sort.SORT_BY, sortBy?.serialName)
            parameter(ListingParamNames.Sort.SORT_ORDER, sortOrder?.serialName)
        }
    }

    override suspend fun getAuditEvent(eventId: String): AppResult<AuditEventPayload> = client.callResult {
        get(
            ManagementAuditRoutes.GET_AUDIT_EVENT.replace(
                "{${AuditApiPaths.EVENT_ID}}",
                eventId
            )
        )
    }
}
