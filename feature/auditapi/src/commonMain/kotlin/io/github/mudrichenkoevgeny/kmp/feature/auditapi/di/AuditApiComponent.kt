package io.github.mudrichenkoevgeny.kmp.feature.auditapi.di

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.network.api.KtorManagementAuditApi
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.network.api.ManagementAuditApi
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.repository.ManagementAuditRepository
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.repository.ManagementAuditRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.root.AuditApiRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.root.AuditApiRootComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.usecase.GetAuditEventUseCase
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.usecase.GetAuditEventsUseCase
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

    /**
     * Creates the root Decompose component for the audit logs flow.
     *
     * @param componentContext Decompose context.
     * @param onBack Navigation back callback.
     * @return A new instance of [AuditApiRootComponent].
     */
    fun createAuditApiRootComponent(
        componentContext: ComponentContext,
        onBack: () -> Unit
    ): AuditApiRootComponent = AuditApiRootComponentImpl(
        componentContext = componentContext,
        getAuditEventsUseCase = getAuditEventsUseCase,
        getAuditEventUseCase = getAuditEventUseCase,
        onBack = onBack
    )
}
