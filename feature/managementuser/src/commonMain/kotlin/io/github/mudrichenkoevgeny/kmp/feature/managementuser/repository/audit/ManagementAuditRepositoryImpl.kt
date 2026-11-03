package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.audit

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.audit.ManagementAuditApi
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.action.CompositeAuditActionTypeParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventPrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.metadata.CompositeAuditMetadataKeyParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.resource.CompositeAuditResourceTypeParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.mapper.audit.toAuditEventPrivate
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.mapper.audit.toAuditEventSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.screen.management.audit.list.ManagementAuditEventListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems

/**
 * Implementation of [ManagementAuditRepository] delegating to [ManagementAuditApi].
 *
 * @param managementAuditApi Network client for administrative audit log endpoints.
 * @param compositeAuditActionTypeParser Parser for converting audit action wire strings to domain models.
 * @param compositeAuditResourceTypeParser Parser for converting audit resource wire strings to domain models.
 * @param compositeAuditMetadataKeyParser Parser for converting audit metadata keys.
 */
class ManagementAuditRepositoryImpl(
    private val managementAuditApi: ManagementAuditApi,
    private val compositeAuditActionTypeParser: CompositeAuditActionTypeParser,
    private val compositeAuditResourceTypeParser: CompositeAuditResourceTypeParser,
    private val compositeAuditMetadataKeyParser: CompositeAuditMetadataKeyParser
) : ManagementAuditRepository {

    override suspend fun getAuditEvents(
        params: ManagementAuditEventListParams?
    ): AppResult<PagedResult<AuditEventSummary>> {
        return managementAuditApi.getAuditEvents(params).mapSuccess { pagedResult ->
            pagedResult.mapItems {
                it.toAuditEventSummary(
                    compositeActionTypeParser = compositeAuditActionTypeParser,
                    compositeResourceTypeParser = compositeAuditResourceTypeParser
                )
            }
        }
    }

    override suspend fun getAuditEvent(eventId: String): AppResult<AuditEventPrivate> {
        return managementAuditApi.getAuditEvent(eventId).mapSuccess {
            it.toAuditEventPrivate(
                compositeActionTypeParser = compositeAuditActionTypeParser,
                compositeResourceTypeParser = compositeAuditResourceTypeParser,
                compositeMetadataKeyParser = compositeAuditMetadataKeyParser
            )
        }
    }
}
