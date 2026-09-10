package io.github.mudrichenkoevgeny.kmp.feature.auditapi.network.api

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.ListingParamNames
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditFilterValues
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
        sortOrder: SortOrder?,
        actorIds: List<String>?,
        actorTypes: List<AuditActorType>?,
        actorUserRoles: List<UserRole>?,
        actions: List<String>?,
        resources: List<String>?,
        resourceIds: List<String>?,
        statuses: List<AuditStatus>?,
        messages: List<String>?
    ): AppResult<PagedResult<AuditEventPayload>> = client.callResult {
        get(ManagementAuditRoutes.GET_AUDIT_EVENTS) {
            parameter(ListingParamNames.Pagination.PAGE_NUMBER, pageNumber)
            parameter(ListingParamNames.Pagination.PAGE_SIZE, pageSize)
            parameter(ListingParamNames.Sort.SORT_BY, sortBy?.serialName)
            parameter(ListingParamNames.Sort.SORT_ORDER, sortOrder?.serialName)
            actorIds?.forEach { parameter(AuditFilterValues.AuditEventFilterValues.ACTOR_ID, it) }
            actorTypes?.forEach { parameter(AuditFilterValues.AuditEventFilterValues.ACTOR_TYPE, it.serialName) }
            actorUserRoles?.forEach { parameter(AuditFilterValues.AuditEventFilterValues.ACTOR_USER_ROLE, it.name) }
            actions?.forEach { parameter(AuditFilterValues.AuditEventFilterValues.ACTION, it) }
            resources?.forEach { parameter(AuditFilterValues.AuditEventFilterValues.RESOURCE, it) }
            resourceIds?.forEach { parameter(AuditFilterValues.AuditEventFilterValues.RESOURCE_ID, it) }
            statuses?.forEach { parameter(AuditFilterValues.AuditEventFilterValues.STATUS, it.serialName) }
            messages?.forEach { parameter(AuditFilterValues.AuditEventFilterValues.MESSAGE, it) }
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
