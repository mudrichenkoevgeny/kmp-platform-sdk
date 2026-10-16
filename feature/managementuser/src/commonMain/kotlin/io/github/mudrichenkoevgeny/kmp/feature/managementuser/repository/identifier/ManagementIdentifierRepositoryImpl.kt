package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier.ManagementIdentifierApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifierSummary

/**
 * Implements [ManagementIdentifierRepository] by forwarding administrative operations to [ManagementIdentifierApi].
 *
 * @param managementIdentifierApi Administrative HTTP endpoints for managing user identifiers.
 */
class ManagementIdentifierRepositoryImpl(
    private val managementIdentifierApi: ManagementIdentifierApi
) : ManagementIdentifierRepository {

    override suspend fun getIdentifiers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        return managementIdentifierApi.getIdentifiers(
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            sortOrder = sortOrder,
            userIds = userIds,
            userAuthProviders = userAuthProviders,
            identifiers = identifiers
        ).mapSuccess { pagedPayload ->
            pagedPayload.mapItems { userIdentifierPayload ->
                userIdentifierPayload.toUserIdentifierSummary()
            }
        }
    }

    override suspend fun getUserIdentifiers(
        userId: UserId,
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        return managementIdentifierApi.getUserIdentifiers(
            userId = userId,
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            sortOrder = sortOrder,
            userAuthProviders = userAuthProviders,
            identifiers = identifiers
        ).mapSuccess { pagedPayload ->
            pagedPayload.mapItems { userIdentifierPayload ->
                userIdentifierPayload.toUserIdentifierSummary()
            }
        }
    }

    override suspend fun getIdentifier(identifierId: String): AppResult<UserIdentifierPrivate> {
        return managementIdentifierApi.getIdentifier(identifierId).mapSuccess { payload ->
            payload.toUserIdentifierPrivate()
        }
    }

    override suspend fun deleteIdentifier(userId: UserId, identifierId: String): AppResult<Unit> {
        return managementIdentifierApi.deleteIdentifier(
            userId = userId,
            identifierId = identifierId
        )
    }

    override suspend fun deleteIdentifierPassword(userId: UserId, identifierId: String): AppResult<Unit> {
        return managementIdentifierApi.deleteIdentifierPassword(
            userId = userId,
            identifierId = identifierId
        )
    }
}