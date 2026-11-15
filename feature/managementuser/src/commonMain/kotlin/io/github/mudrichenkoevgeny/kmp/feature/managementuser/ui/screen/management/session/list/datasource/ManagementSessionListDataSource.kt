package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteAllUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.datasource.SessionListDataSource
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.datasource.SessionListQuery
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.globallist.ManagementGlobalSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.userlist.ManagementUserSessionListParams

/**
 * [SessionListDataSource] implementation for management sessions list screens (global or user-specific).
 *
 * @param managementGetSessionsUseCase Use case fetching global management sessions.
 * @param managementGetUserSessionsUseCase Optional use case fetching sessions for a specific user.
 * @param managementDeleteSessionUseCase Use case administratively revoking a session.
 * @param managementDeleteAllUserSessionsUseCase Optional use case revoking all sessions for a user.
 */
public class ManagementSessionListDataSource(
    private val managementGetSessionsUseCase: ManagementGetSessionsUseCase,
    private val managementGetUserSessionsUseCase: ManagementGetUserSessionsUseCase? = null,
    private val managementDeleteSessionUseCase: ManagementDeleteSessionUseCase,
    private val managementDeleteAllUserSessionsUseCase: ManagementDeleteAllUserSessionsUseCase? = null
) : SessionListDataSource {

    override val isRevokeAvailable: Boolean = true
    override val isRevokeAllOthersAvailable: Boolean
        get() = managementDeleteAllUserSessionsUseCase != null

    override suspend fun getSessions(query: SessionListQuery): AppResult<PagedResult<UserSessionSummary>> {
        val targetUser = query.targetUserId
        if (targetUser != null && managementGetUserSessionsUseCase != null) {
            val userParams = ManagementUserSessionListParams(
                userId = targetUser,
                pageNumber = query.pageNumber
            )
            return managementGetUserSessionsUseCase(userParams)
        }

        val globalParams = query.toManagementGlobalParams()
        return managementGetSessionsUseCase(globalParams)
    }

    override suspend fun revokeSession(
        sessionId: String,
        userId: String?
    ): AppResult<Unit> {
        if (userId == null) {
            return AppResult.Error(
                CommonError.ContractViolation(
                    throwable = IllegalArgumentException("userId is required for management session revocation")
                )
            )
        }
        val typedUserId = userId.toUserIdOrThrow()
        return managementDeleteSessionUseCase(typedUserId, sessionId)
    }

    override suspend fun revokeAllOtherSessions(userId: String?): AppResult<Unit> {
        val deleteUseCase = managementDeleteAllUserSessionsUseCase
        if (userId == null || deleteUseCase == null) {
            return AppResult.Error(
                CommonError.ContractViolation(
                    throwable = IllegalArgumentException(
                        "userId and deleteAllUserSessionsUseCase are required for management revoke all"
                    )
                )
            )
        }
        val typedUserId = userId.toUserIdOrThrow()
        return deleteUseCase(typedUserId)
    }

    private fun SessionListQuery.toManagementGlobalParams(): ManagementGlobalSessionListParams {
        val identity = parseIdentityParams()
        val deviceAndClient = parseDeviceAndClientParams()
        val timestampsAndSearch = parseTimestampAndSearchParams()

        return ManagementGlobalSessionListParams(
            pageNumber = pageNumber,
            sortBy = sortBy,
            sortOrder = sortOrder,
            sessionIds = identity.sessionIds,
            userIds = identity.userIds,
            userRoles = userRolesChoices,
            excludeUserRoles = excludeUserRolesChoices,
            identifier = identity.identifier,
            identifierIds = identity.identifierIds,
            identifierDisplayName = identity.identifierDisplayName,
            identifierAuthProviders = identity.authProviders,
            excludeIdentifierAuthProviders = identity.excludeAuthProviders,
            deviceId = deviceAndClient.deviceId,
            isDeviceIdSet = deviceAndClient.isDeviceIdSet,
            deviceName = deviceAndClient.deviceName,
            isDeviceNameSet = deviceAndClient.isDeviceNameSet,
            clientTypes = deviceAndClient.clientTypes,
            excludeClientTypes = deviceAndClient.excludeClientTypes,
            isClientTypeSet = deviceAndClient.isClientTypeSet,
            userAgent = deviceAndClient.userAgent,
            isUserAgentSet = deviceAndClient.isUserAgentSet,
            ipAddress = deviceAndClient.ipAddress,
            isIpAddressSet = deviceAndClient.isIpAddressSet,
            language = deviceAndClient.language,
            isLanguageSet = deviceAndClient.isLanguageSet,
            appVersion = deviceAndClient.appVersion,
            isAppVersionSet = deviceAndClient.isAppVersionSet,
            osVersion = deviceAndClient.osVersion,
            isOperationSystemVersionSet = deviceAndClient.isOsVersionSet,
            expiresAtFrom = timestampsAndSearch.expiresAtFrom,
            expiresAtTo = timestampsAndSearch.expiresAtTo,
            lastAccessedAtFrom = timestampsAndSearch.lastAccessedAtFrom,
            lastAccessedAtTo = timestampsAndSearch.lastAccessedAtTo,
            lastReauthenticatedAtFrom = timestampsAndSearch.lastReauthenticatedAtFrom,
            lastReauthenticatedAtTo = timestampsAndSearch.lastReauthenticatedAtTo,
            createdAtFrom = timestampsAndSearch.createdAtFrom,
            createdAtTo = timestampsAndSearch.createdAtTo,
            updatedAtFrom = timestampsAndSearch.updatedAtFrom,
            updatedAtTo = timestampsAndSearch.updatedAtTo,
            isUpdatedAtSet = timestampsAndSearch.isUpdatedAtSet,
            searchQuery = timestampsAndSearch.searchQuery
        )
    }

    private fun SessionListQuery.parseIdentityParams(): SessionIdentityFilterParams {
        val userIdsList = targetUserId?.let { listOf(it) }
            ?: userIdText?.let { listOf(it) }
            ?: emptyList()

        return SessionIdentityFilterParams(
            sessionIds = sessionIdText?.let { listOf(it) } ?: emptyList(),
            userIds = userIdsList,
            identifier = identifierText ?: "",
            identifierIds = identifierIdText?.let { listOf(it) } ?: emptyList(),
            identifierDisplayName = identifierDisplayNameText ?: "",
            authProviders = authProvidersChoices,
            excludeAuthProviders = excludeAuthProvidersChoices
        )
    }

    private fun SessionListQuery.parseDeviceAndClientParams(): SessionDeviceAndClientFilterParams =
        SessionDeviceAndClientFilterParams(
            deviceId = deviceIdText ?: "",
            isDeviceIdSet = isDeviceIdSetChoice,
            deviceName = deviceNameText ?: "",
            isDeviceNameSet = isDeviceNameSetChoice,
            clientTypes = clientTypesChoices,
            excludeClientTypes = excludeClientTypesChoices,
            isClientTypeSet = isClientTypeSetChoice,
            userAgent = userAgentText ?: "",
            isUserAgentSet = isUserAgentSetChoice,
            ipAddress = ipAddressText ?: "",
            isIpAddressSet = isIpAddressSetChoice,
            language = languageText ?: "",
            isLanguageSet = isLanguageSetChoice,
            appVersion = appVersionText ?: "",
            isAppVersionSet = isAppVersionSetChoice,
            osVersion = osVersionText ?: "",
            isOsVersionSet = isOsVersionSetChoice
        )

    private fun SessionListQuery.parseTimestampAndSearchParams(): SessionTimestampAndSearchFilterParams =
        SessionTimestampAndSearchFilterParams(
            expiresAtFrom = expiresAtFromText ?: "",
            expiresAtTo = expiresAtToText ?: "",
            lastAccessedAtFrom = lastAccessedAtFromText ?: "",
            lastAccessedAtTo = lastAccessedAtToText ?: "",
            lastReauthenticatedAtFrom = lastReauthenticatedAtFromText ?: "",
            lastReauthenticatedAtTo = lastReauthenticatedAtToText ?: "",
            createdAtFrom = createdAtFromText ?: "",
            createdAtTo = createdAtToText ?: "",
            updatedAtFrom = updatedAtFromText ?: "",
            updatedAtTo = updatedAtToText ?: "",
            isUpdatedAtSet = isUpdatedAtSetChoice,
            searchQuery = searchQueryText ?: ""
        )
}

private data class SessionIdentityFilterParams(
    val sessionIds: List<String>,
    val userIds: List<String>,
    val identifier: String,
    val identifierIds: List<String>,
    val identifierDisplayName: String,
    val authProviders: List<UserAuthProvider>,
    val excludeAuthProviders: List<UserAuthProvider>
)

private data class SessionDeviceAndClientFilterParams(
    val deviceId: String,
    val isDeviceIdSet: Boolean?,
    val deviceName: String,
    val isDeviceNameSet: Boolean?,
    val clientTypes: List<ClientType>,
    val excludeClientTypes: List<ClientType>,
    val isClientTypeSet: Boolean?,
    val userAgent: String,
    val isUserAgentSet: Boolean?,
    val ipAddress: String,
    val isIpAddressSet: Boolean?,
    val language: String,
    val isLanguageSet: Boolean?,
    val appVersion: String,
    val isAppVersionSet: Boolean?,
    val osVersion: String,
    val isOsVersionSet: Boolean?
)

private data class SessionTimestampAndSearchFilterParams(
    val expiresAtFrom: String,
    val expiresAtTo: String,
    val lastAccessedAtFrom: String,
    val lastAccessedAtTo: String,
    val lastReauthenticatedAtFrom: String,
    val lastReauthenticatedAtTo: String,
    val createdAtFrom: String,
    val createdAtTo: String,
    val updatedAtFrom: String,
    val updatedAtTo: String,
    val isUpdatedAtSet: Boolean?,
    val searchQuery: String
)
