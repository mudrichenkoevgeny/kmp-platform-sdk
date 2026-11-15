package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.network.httprequest.parameters
import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.globallist.ManagementGlobalIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.userlist.ManagementUserIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.users.identifiers.ManagementIdentifiersRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/** [ManagementIdentifierApi] backed by [HttpClient]. */
public class KtorManagementIdentifierApi(
    private val client: HttpClient
) : ManagementIdentifierApi {

    override suspend fun getIdentifiers(
        params: ManagementGlobalIdentifierListParams?
    ): AppResult<PagedResult<UserIdentifierSummaryPayload>> = client.callResult {
        get(ManagementIdentifiersRoutes.GET_IDENTIFIERS) {
            parameters(params)
        }
    }

    override suspend fun getUserIdentifiers(
        params: ManagementUserIdentifierListParams
    ): AppResult<PagedResult<UserIdentifierSummaryPayload>> = client.callResult {
        get(
            ManagementIdentifiersRoutes.GET_USER_IDENTIFIERS
                .replace(
                    "{${UserApiPaths.USER_ID}}",
                    params.userId
                )
        ) {
            parameters(params)
        }
    }

    override suspend fun getIdentifier(
        identifierId: String
    ): AppResult<UserIdentifierPrivatePayload> = client.callResult {
        get(ManagementIdentifiersRoutes.GET_IDENTIFIER) {
            parameter(UserApiPaths.USER_IDENTIFIER_ID, identifierId)
        }
    }

    override suspend fun deleteIdentifier(
        userId: UserId,
        identifierId: String
    ): AppResult<Unit> = client.callResult {
        delete(ManagementIdentifiersRoutes.DELETE_IDENTIFIER) {
            parameter(UserApiPaths.USER_ID, userId.value)
            parameter(UserApiPaths.USER_IDENTIFIER_ID, identifierId)
        }
    }

    override suspend fun deleteIdentifierPassword(
        userId: UserId,
        identifierId: String
    ): AppResult<Unit> = client.callResult {
        delete(ManagementIdentifiersRoutes.DELETE_IDENTIFIER_PASSWORD) {
            parameter(UserApiPaths.USER_ID, userId.value)
            parameter(UserApiPaths.USER_IDENTIFIER_ID, identifierId)
        }
    }
}
