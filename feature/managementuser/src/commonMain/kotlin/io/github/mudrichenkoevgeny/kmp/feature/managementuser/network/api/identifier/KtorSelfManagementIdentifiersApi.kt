package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.network.httprequest.parameters
import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.list.SelfIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPrivatePayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierSummaryPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.EmailPasswordChangeRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.self.user.identifier.SelfManagementIdentifierRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [SelfManagementIdentifiersApi] backed by [HttpClient]. */
public class KtorSelfManagementIdentifiersApi(
    private val client: HttpClient
) : SelfManagementIdentifiersApi {

    override suspend fun getUserIdentifier(
        userIdentifierId: UserIdentifierId
    ): AppResult<UserIdentifierPrivatePayload> = client.callResult {
        get(
            SelfManagementIdentifierRoutes.GET_IDENTIFIER
                .replace(
                    "{${UserApiPaths.USER_IDENTIFIER_ID}}",
                    userIdentifierId.asHexDashString()
                )
        )
    }

    override suspend fun getUserIdentifiers(
        params: SelfIdentifierListParams?
    ): AppResult<PagedResult<UserIdentifierSummaryPayload>> = client.callResult {
        get(SelfManagementIdentifierRoutes.GET_IDENTIFIERS) {
            parameters(params)
        }
    }

    override suspend fun emailChangePassword(
        userIdentifierId: UserIdentifierId,
        request: EmailPasswordChangeRequest
    ): AppResult<Unit> = client.callResult {
        post(
            SelfManagementIdentifierRoutes.IDENTIFIER_EMAIL_CHANGE_PASSWORD
                .replace(
                    "{${UserApiPaths.USER_IDENTIFIER_ID}}",
                    userIdentifierId.asHexDashString()
                )
        ) {
            setBody(request)
        }
    }
}
