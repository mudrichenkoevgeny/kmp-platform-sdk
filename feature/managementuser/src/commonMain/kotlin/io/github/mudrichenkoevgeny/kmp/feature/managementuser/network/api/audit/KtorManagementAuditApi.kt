package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.audit

import io.github.mudrichenkoevgeny.kmp.core.common.network.httprequest.parameters
import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.screen.management.audit.list.ManagementAuditEventListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.contract.AuditApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.model.event.AuditEventPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.model.event.AuditEventSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.network.route.management.ManagementAuditRoutes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * Ktor-backed implementation of [ManagementAuditApi].
 *
 * @param client Shared [HttpClient].
 */
class KtorManagementAuditApi(
    private val client: HttpClient
) : ManagementAuditApi {
    override suspend fun getAuditEvents(
        params: ManagementAuditEventListParams?
    ): AppResult<PagedResult<AuditEventSummaryPayload>> = client.callResult {
        get(ManagementAuditRoutes.GET_AUDIT_EVENTS) {
            parameters(params)
        }
    }

    override suspend fun getAuditEvent(eventId: String): AppResult<AuditEventPrivatePayload> = client.callResult {
        get(
            ManagementAuditRoutes.GET_AUDIT_EVENT.replace(
                "{${AuditApiPaths.EVENT_ID}}",
                eventId
            )
        )
    }
}
