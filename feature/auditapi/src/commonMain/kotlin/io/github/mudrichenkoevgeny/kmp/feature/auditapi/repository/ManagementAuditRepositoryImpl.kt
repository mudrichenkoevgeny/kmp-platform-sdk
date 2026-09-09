package io.github.mudrichenkoevgeny.kmp.feature.auditapi.repository

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.network.api.ManagementAuditApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.action.CompositeAuditActionTypeParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.metadata.CompositeAuditMetadataKeyParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.resource.CompositeAuditResourceTypeParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.mapper.audit.toAuditEvent

/**
 * Default implementation of [ManagementAuditRepository].
 *
 * @param managementAuditApi Network API source.
 * @param compositeAuditActionTypeParser Parser for action types.
 * @param compositeAuditResourceTypeParser Parser for resource types.
 * @param compositeAuditMetadataKeyParser Parser for metadata keys.
 */
class ManagementAuditRepositoryImpl(
    private val managementAuditApi: ManagementAuditApi,
    private val compositeAuditActionTypeParser: CompositeAuditActionTypeParser,
    private val compositeAuditResourceTypeParser: CompositeAuditResourceTypeParser,
    private val compositeAuditMetadataKeyParser: CompositeAuditMetadataKeyParser
) : ManagementAuditRepository {

    override suspend fun getAuditEvents(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: AuditSortValues.AuditEventSortBy?,
        sortOrder: SortOrder?
    ): AppResult<PagedResult<AuditEvent>> {
        return managementAuditApi.getAuditEvents(
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            sortOrder = sortOrder
        ).mapSuccess { pagedPayload ->
            pagedPayload.mapItems { payload ->
                payload.toAuditEvent(
                    compositeActionTypeParser = compositeAuditActionTypeParser,
                    compositeResourceTypeParser = compositeAuditResourceTypeParser,
                    compositeMetadataKeyParser = compositeAuditMetadataKeyParser
                )
            }
        }
    }

    override suspend fun getAuditEvent(eventId: String): AppResult<AuditEvent> {
        return managementAuditApi.getAuditEvent(eventId).mapSuccess { payload ->
            payload.toAuditEvent(
                compositeActionTypeParser = compositeAuditActionTypeParser,
                compositeResourceTypeParser = compositeAuditResourceTypeParser,
                compositeMetadataKeyParser = compositeAuditMetadataKeyParser
            )
        }
    }
}
