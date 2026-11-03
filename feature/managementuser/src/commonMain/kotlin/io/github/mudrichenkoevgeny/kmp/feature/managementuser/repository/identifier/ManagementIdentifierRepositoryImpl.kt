package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier.ManagementIdentifierApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.globallist.ManagementGlobalIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.userlist.ManagementUserIdentifierListParams

/**
 * Implements [ManagementIdentifierRepository] by forwarding administrative operations to [ManagementIdentifierApi].
 *
 * @param managementIdentifierApi Administrative HTTP endpoints for managing user identifiers.
 */
class ManagementIdentifierRepositoryImpl(
    private val managementIdentifierApi: ManagementIdentifierApi
) : ManagementIdentifierRepository {

    override suspend fun getIdentifiers(
        params: ManagementGlobalIdentifierListParams?
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        return managementIdentifierApi.getIdentifiers(params).mapSuccess { pagedPayload ->
            pagedPayload.mapItems { userIdentifierPayload ->
                userIdentifierPayload.toUserIdentifierSummary()
            }
        }
    }

    override suspend fun getUserIdentifiers(
        params: ManagementUserIdentifierListParams
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        return managementIdentifierApi.getUserIdentifiers(params).mapSuccess { pagedPayload ->
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
