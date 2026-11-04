package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteAllUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.datasource.SessionListDataSource
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.datasource.SessionListQuery
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
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
class ManagementSessionListDataSource(
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

    override suspend fun revokeSession(sessionId: String, userId: String?): AppResult<Unit> {
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
                    throwable = IllegalArgumentException("userId and deleteAllUserSessionsUseCase are required for management revoke all")
                )
            )
        }
        val typedUserId = userId.toUserIdOrThrow()
        return deleteUseCase(typedUserId)
    }

    private fun SessionListQuery.toManagementGlobalParams(): ManagementGlobalSessionListParams {
        val userIdsList = targetUserId?.let { listOf(it) }
            ?: userIdText?.let { listOf(it) }
            ?: emptyList()

        return ManagementGlobalSessionListParams(
            pageNumber = pageNumber,
            sortBy = sortBy,
            sortOrder = sortOrder,
            sessionIds = sessionIdText?.let { listOf(it) } ?: emptyList(),
            userIds = userIdsList,
            userRoles = userRolesChoices,
            excludeUserRoles = excludeUserRolesChoices,
            identifier = identifierText ?: "",
            identifierIds = identifierIdText?.let { listOf(it) } ?: emptyList(),
            identifierDisplayName = identifierDisplayNameText ?: "",
            identifierAuthProviders = authProvidersChoices,
            excludeIdentifierAuthProviders = excludeAuthProvidersChoices,
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
            isOperationSystemVersionSet = isOsVersionSetChoice,
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
}
