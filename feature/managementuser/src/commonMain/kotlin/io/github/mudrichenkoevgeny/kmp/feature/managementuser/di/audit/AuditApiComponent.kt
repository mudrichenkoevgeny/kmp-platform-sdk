package io.github.mudrichenkoevgeny.kmp.feature.managementuser.di.audit

import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.audit.KtorManagementAuditApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.audit.ManagementAuditApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.audit.ManagementAuditRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.audit.ManagementAuditRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.action.CompositeAuditActionTypeParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.metadata.CompositeAuditMetadataKeyParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.resource.CompositeAuditResourceTypeParser
import io.ktor.client.HttpClient

/**
 * Feature-level dependency root for the audit logs module.
 *
 * @param httpClient Shared Ktor HTTP client.
 * @param compositeAuditActionTypeParser Parser for audit action types.
 * @param compositeAuditResourceTypeParser Parser for audit resource types.
 * @param compositeAuditMetadataKeyParser Parser for audit metadata keys.
 */
class AuditApiComponent(
    httpClient: HttpClient,
    compositeAuditActionTypeParser: CompositeAuditActionTypeParser,
    compositeAuditResourceTypeParser: CompositeAuditResourceTypeParser,
    compositeAuditMetadataKeyParser: CompositeAuditMetadataKeyParser
) {
    private val auditApi: ManagementAuditApi = KtorManagementAuditApi(httpClient)
    private val auditRepository: ManagementAuditRepository = ManagementAuditRepositoryImpl(
        managementAuditApi = auditApi,
        compositeAuditActionTypeParser = compositeAuditActionTypeParser,
        compositeAuditResourceTypeParser = compositeAuditResourceTypeParser,
        compositeAuditMetadataKeyParser = compositeAuditMetadataKeyParser
    )

    /** Use case for fetching paginated audit events. */
    val getAuditEventsUseCase = GetAuditEventsUseCase(auditRepository)

    /** Use case for fetching a specific audit event by ID. */
    val getAuditEventUseCase = GetAuditEventUseCase(auditRepository)
}