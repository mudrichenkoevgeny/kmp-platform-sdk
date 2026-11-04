package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.datasource.IdentifierListDataSource
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.datasource.IdentifierListQuery
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.globallist.ManagementGlobalIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.userlist.ManagementUserIdentifierListParams

/**
 * [IdentifierListDataSource] implementation for management identifiers list screens (global or user-specific).
 *
 * @param managementGetIdentifiersUseCase Use case fetching global management identifiers.
 * @param managementGetUserIdentifiersUseCase Optional use case fetching identifiers for a specific user.
 */
class ManagementIdentifierListDataSource(
    private val managementGetIdentifiersUseCase: ManagementGetIdentifiersUseCase,
    private val managementGetUserIdentifiersUseCase: ManagementGetUserIdentifiersUseCase? = null
) : IdentifierListDataSource {

    override val isAddIdentifierSupported: Boolean = false

    override suspend fun getIdentifiers(query: IdentifierListQuery): AppResult<PagedResult<UserIdentifierSummary>> {
        val targetUser = query.targetUserId
        if (targetUser != null && managementGetUserIdentifiersUseCase != null) {
            val userParams = ManagementUserIdentifierListParams(
                userId = targetUser,
                pageNumber = query.pageNumber
            )
            return managementGetUserIdentifiersUseCase(userParams)
        }

        val globalParams = query.toManagementGlobalParams()
        return managementGetIdentifiersUseCase(globalParams)
    }

    private fun IdentifierListQuery.toManagementGlobalParams(): ManagementGlobalIdentifierListParams {
        val userIdsList = targetUserId?.let { listOf(it) }
            ?: userIdText?.let { listOf(it) }
            ?: emptyList()

        return ManagementGlobalIdentifierListParams(
            pageNumber = pageNumber,
            sortBy = sortBy,
            sortOrder = sortOrder,
            identifierIds = identifierIdText?.let { listOf(it) } ?: emptyList(),
            userIds = userIdsList,
            userAuthProviders = authProvidersChoices,
            excludeUserAuthProviders = excludeAuthProvidersChoices,
            identifier = identifierText ?: "",
            displayName = displayNameText ?: "",
            externalProviderEmail = externalProviderEmailText ?: "",
            isExternalProviderEmailSet = isExternalProviderEmailSetChoice,
            createdAtFrom = createdAtFromText ?: "",
            createdAtTo = createdAtToText ?: "",
            updatedAtFrom = updatedAtFromText ?: "",
            updatedAtTo = updatedAtToText ?: "",
            isUpdatedAtSet = isUpdatedAtSetChoice,
            searchQuery = searchQueryText ?: ""
        )
    }
}
