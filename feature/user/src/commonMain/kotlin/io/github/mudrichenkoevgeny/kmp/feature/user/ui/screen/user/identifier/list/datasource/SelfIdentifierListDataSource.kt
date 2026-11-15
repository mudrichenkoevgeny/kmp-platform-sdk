package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.apptype.AppType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.list.SelfIdentifierListParams

/**
 * [IdentifierListDataSource] implementation for self-service / client active user identifiers list.
 *
 * @param getUserIdentifiersUseCase Use case fetching linked identity records.
 * @param appType Operational context (Client or Management).
 */
public class SelfIdentifierListDataSource(
    private val getUserIdentifiersUseCase: GetUserIdentifiersUseCase,
    private val appType: AppType = AppType.CLIENT
) : IdentifierListDataSource {

    public override val isAddIdentifierSupported: Boolean
        get() = appType == AppType.CLIENT

    public override suspend fun getIdentifiers(
        query: IdentifierListQuery
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        val params = SelfIdentifierListParams(
            pageNumber = query.pageNumber
        )
        return getUserIdentifiersUseCase(params)
    }
}
