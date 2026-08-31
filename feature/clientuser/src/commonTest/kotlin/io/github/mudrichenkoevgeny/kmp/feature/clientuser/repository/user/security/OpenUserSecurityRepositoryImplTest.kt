package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.security.UserSecurityApi
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class OpenUserSecurityRepositoryImplTest {

    @Test
    fun `enableTotp should update storage and return recovery codes`() = runTest {
        val recoveryCodes = TotpRecoveryCodes(codes = listOf("code1"))
        val api = FakeUserSecurityApi(enableResult = AppResult.Success(recoveryCodesPayload(recoveryCodes)))
        val storage = FakeUserStorage().apply {
            currentUser = userDetailsMock().copy(isTotpEnabled = false)
        }
        val repository = OpenUserSecurityRepositoryImpl(api, storage)

        val result = repository.enableTotp("token", "code")

        assertIs<AppResult.Success<TotpRecoveryCodes>>(result)
        assertEquals(recoveryCodes.codes, result.data.codes)
        assertTrue(storage.currentUser?.isTotpEnabled == true)
    }

    @Test
    fun `disableTotp should update storage on success`() = runTest {
        val api = FakeUserSecurityApi(disableResult = AppResult.Success(Unit))
        val storage = FakeUserStorage().apply {
            currentUser = userDetailsMock().copy(isTotpEnabled = true)
        }
        val repository = OpenUserSecurityRepositoryImpl(api, storage)

        val result = repository.disableTotp()

        assertIs<AppResult.Success<Unit>>(result)
        assertTrue(storage.currentUser?.isTotpEnabled == false)
    }

    private fun recoveryCodesPayload(codes: TotpRecoveryCodes) = io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totprecoverycodes.TotpRecoveryCodesPayload(
        codes = codes.codes
    )

    private class FakeUserSecurityApi(
        private val enableResult: AppResult<io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totprecoverycodes.TotpRecoveryCodesPayload> = AppResult.Error(io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError.Unknown()),
        private val disableResult: AppResult<Unit> = AppResult.Success(Unit)
    ) : UserSecurityApi {
        override suspend fun setupTotp(): AppResult<io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totpsetup.TotpSetupPayload> = error("N/A")
        override suspend fun enableTotp(request: io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload): AppResult<io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totprecoverycodes.TotpRecoveryCodesPayload> = enableResult
        override suspend fun disableTotp(): AppResult<Unit> = disableResult
        override suspend fun getRecoveryCodes(): AppResult<io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totprecoverycodes.TotpRecoveryCodesPayload> = error("N/A")
        override suspend fun regenerateRecoveryCodes(): AppResult<io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totprecoverycodes.TotpRecoveryCodesPayload> = error("N/A")
    }

    private class FakeUserStorage : UserStorage {
        var currentUser: UserDetails? = null

        override suspend fun getCurrentUser(): UserDetails? = currentUser
        override fun observeCurrentUser() = error("N/A")
        override suspend fun updateCurrentUser(currentUser: UserDetails) {
            this.currentUser = currentUser
        }
        override suspend fun getUserIdentifiersList(pageNumber: Int?, pageSize: Int?, sortBy: io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues.UserIdentifierSortBy?, sortOrder: io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder?, userIds: List<String>?, userAuthProviders: List<io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider>?, identifiers: List<String>?) = error("N/A")
        override fun observeUserIdentifiersList(pageNumber: Int?, pageSize: Int?, sortBy: io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues.UserIdentifierSortBy?, sortOrder: io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder?, userIds: List<String>?, userAuthProviders: List<io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider>?, identifiers: List<String>?) = error("N/A")
        override suspend fun updateUserIdentifiersList(userIdentifiersList: io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult<io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier>) = Unit
        override suspend fun updateUserIdentifiersPayloadList(userIdentifiersList: io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult<io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload>) = Unit
        override suspend fun addUserIdentifier(userIdentifier: io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier) = Unit
        override suspend fun removeUserIdentifier(identifierId: io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId) = Unit
        override suspend fun getUserSessionsList(pageNumber: Int?, pageSize: Int?, sortBy: io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues.UserSessionSortBy?, sortOrder: io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder?, userIds: List<String>?, userRoles: List<io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole>?, identifiers: List<String>?, identifierIds: List<String>?, userAuthProviders: List<io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider>?, clientTypes: List<io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType>?, userAgents: List<String>?, ipAddresses: List<String>?, languages: List<String>?, deviceIds: List<String>?, deviceNames: List<String>?, appVersions: List<String>?, operationSystemVersions: List<String>?) = error("N/A")
        override fun observeUserSessionsList(pageNumber: Int?, pageSize: Int?, sortBy: io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues.UserSessionSortBy?, sortOrder: io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder?, userIds: List<String>?, userRoles: List<io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole>?, identifiers: List<String>?, identifierIds: List<String>?, userAuthProviders: List<io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider>?, clientTypes: List<io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType>?, userAgents: List<String>?, ipAddresses: List<String>?, languages: List<String>?, deviceIds: List<String>?, deviceNames: List<String>?, appVersions: List<String>?, operationSystemVersions: List<String>?) = error("N/A")
        override suspend fun updateUserSessionsList(userSessionsList: io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult<io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession>) = Unit
        override suspend fun updateUserSessionsPayloadList(userSessionsList: io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult<io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload>) = Unit
        override suspend fun addUserSession(userSession: io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession) = Unit
        override suspend fun removeUserSession(sessionId: io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId) = Unit
        override suspend fun removeUserSessions(sessionIds: List<io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId>) = Unit
        override suspend fun clear() = Unit
    }
}
