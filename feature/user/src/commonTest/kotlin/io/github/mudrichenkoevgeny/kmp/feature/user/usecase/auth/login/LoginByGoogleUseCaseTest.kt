package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.PublicAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.AccessToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.RefreshToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.SessionToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Instant

@InternalApi
class LoginByGoogleUseCaseTest {

    private val sessionToken = SessionToken(
        accessToken = AccessToken("access"),
        refreshToken = RefreshToken("refresh"),
        expiresAt = Instant.fromEpochMilliseconds(1000)
    )

    private val authDataMock = AuthData(
        sessionToken = sessionToken,
        userDetails = userDetailsMock()
    )

    @Test
    fun `should complete full flow on success`() = runTest {
        val authService = FakeGoogleAuthService(AppResult.Success("google_token"))
        val loginRepository = FakeLoginRepository(AppResult.Success(authDataMock))
        val authStorage = FakeAuthStorage()
        val userStorage = FakeUserStorage()
        val useCase = LoginByGoogleUseCase(authService, loginRepository, authStorage, userStorage)

        val result = useCase.execute()

        assertIs<AppResult.Success<AuthData>>(result)
        assertEquals(authDataMock, result.data)
        
        assertEquals(authDataMock.sessionToken.accessToken, authStorage.lastAccessToken)
        assertEquals(authDataMock.userDetails, userStorage.lastUser)
    }

    @Test
    fun `should abort when google auth fails`() = runTest {
        val error = CommonError.Unknown()
        val authService = FakeGoogleAuthService(AppResult.Error(error))
        val loginRepository = FakeLoginRepository(AppResult.Success(authDataMock))
        val authStorage = FakeAuthStorage()
        val userStorage = FakeUserStorage()
        val useCase = LoginByGoogleUseCase(authService, loginRepository, authStorage, userStorage)

        val result = useCase.execute()

        assertIs<AppResult.Error>(result)
        assertEquals(error, result.error)
        
        assertFalse(loginRepository.wasCalled)
        assertEquals(authStorage.lastAccessToken, null)
    }

    @Test
    fun `should abort when repository exchange fails`() = runTest {
        val error = CommonError.Network(Exception("api_fail"))
        val authService = FakeGoogleAuthService(AppResult.Success("google_token"))
        val loginRepository = FakeLoginRepository(AppResult.Error(error))
        val authStorage = FakeAuthStorage()
        val userStorage = FakeUserStorage()
        val useCase = LoginByGoogleUseCase(authService, loginRepository, authStorage, userStorage)

        val result = useCase.execute()

        assertIs<AppResult.Error>(result)
        assertEquals(error, result.error)
        
        assertTrue(authStorage.lastAccessToken == null)
    }

    private class FakeGoogleAuthService(private val result: AppResult<String>) : GoogleAuthService {
        override suspend fun signIn(): AppResult<String> = result
        override suspend fun signOut(): AppResult<Unit> = AppResult.Success(Unit)
    }

    private class FakeLoginRepository(private val result: AppResult<AuthData>) : LoginRepository {
        var wasCalled = false
        override suspend fun loginByEmail(email: String, password: String) = error("N/A")
        override suspend fun loginByPhone(phoneNumber: String, confirmationCode: String) = error("N/A")
        override suspend fun loginByExternalAuthProvider(authProvider: UserAuthProvider, token: String): AppResult<AuthData> {
            wasCalled = true
            return result
        }
        override suspend fun loginByTotp(mfaToken: String, code: String) = error("N/A")
        override suspend fun loginByTotpRecoveryCode(mfaToken: String, code: String) = error("N/A")
        override suspend fun sendLoginConfirmationToPhone(phoneNumber: String) = error("N/A")
        override fun getRemainingLoginConfirmationDelayInSeconds(phoneNumber: String): Int = 0
    }

    private class FakeAuthStorage : AuthStorage {
        var lastAccessToken: AccessToken? = null
        override val accessTokenFlow: StateFlow<String?> get() = error("N/A")
        override suspend fun getAccessToken() = error("N/A")
        override suspend fun getRefreshToken() = error("N/A")
        override suspend fun getExpiresAt() = 0L
        override suspend fun updateTokens(accessToken: AccessToken, refreshToken: RefreshToken, expiresAt: Instant) {
            lastAccessToken = accessToken
        }
        override suspend fun clearTokens() = Unit
        override suspend fun getPublicAuthSettings(): PublicAuthSettings? = null
        override suspend fun updatePublicAuthSettings(publicAuthSettings: PublicAuthSettings) = Unit
        override suspend fun clearPublicAuthSettings() = Unit
        override suspend fun getManagementAuthSettings(): ManagementAuthSettings? = null
        override suspend fun updateManagementAuthSettings(managementAuthSettings: ManagementAuthSettings) = Unit
        override suspend fun clearManagementAuthSettings() = Unit
    }

    private class FakeUserStorage : UserStorage {
        var lastUser: UserDetails? = null
        override suspend fun getCurrentUser() = error("N/A")
        override fun observeCurrentUser() = error("N/A")
        override suspend fun updateCurrentUser(currentUser: UserDetails) {
            lastUser = currentUser
        }
        override suspend fun getUserIdentifiersList(
            pageNumber: Int?,
            pageSize: Int?,
            sortBy: UserSortValues.UserIdentifierSortBy?,
            sortOrder: SortOrder?,
            userIds: List<String>?,
            userAuthProviders: List<UserAuthProvider>?,
            identifiers: List<String>?
        ): PagedResult<UserIdentifier> = error("N/A")

        override fun observeUserIdentifiersList(
            pageNumber: Int?,
            pageSize: Int?,
            sortBy: UserSortValues.UserIdentifierSortBy?,
            sortOrder: SortOrder?,
            userIds: List<String>?,
            userAuthProviders: List<UserAuthProvider>?,
            identifiers: List<String>?
        ): Flow<PagedResult<UserIdentifier>> = error("N/A")

        override suspend fun updateUserIdentifiersList(userIdentifiersList: PagedResult<UserIdentifier>) = Unit
        override suspend fun updateUserIdentifiersPayloadList(userIdentifiersList: PagedResult<UserIdentifierPayload>) = Unit
        override suspend fun addUserIdentifier(userIdentifier: UserIdentifier) = Unit
        override suspend fun removeUserIdentifier(identifierId: UserIdentifierId) = Unit
        
        override suspend fun getUserSessionsList(
            pageNumber: Int?,
            pageSize: Int?,
            sortBy: UserSortValues.UserSessionSortBy?,
            sortOrder: SortOrder?,
            userIds: List<String>?,
            userRoles: List<UserRole>?,
            identifiers: List<String>?,
            identifierIds: List<String>?,
            userAuthProviders: List<UserAuthProvider>?,
            clientTypes: List<ClientType>?,
            userAgents: List<String>?,
            ipAddresses: List<String>?,
            languages: List<String>?,
            deviceIds: List<String>?,
            deviceNames: List<String>?,
            appVersions: List<String>?,
            operationSystemVersions: List<String>?
        ): PagedResult<UserSession> = error("N/A")

        override fun observeUserSessionsList(
            pageNumber: Int?,
            pageSize: Int?,
            sortBy: UserSortValues.UserSessionSortBy?,
            sortOrder: SortOrder?,
            userIds: List<String>?,
            userRoles: List<UserRole>?,
            identifiers: List<String>?,
            identifierIds: List<String>?,
            userAuthProviders: List<UserAuthProvider>?,
            clientTypes: List<ClientType>?,
            userAgents: List<String>?,
            ipAddresses: List<String>?,
            languages: List<String>?,
            deviceIds: List<String>?,
            deviceNames: List<String>?,
            appVersions: List<String>?,
            operationSystemVersions: List<String>?
        ): Flow<PagedResult<UserSession>> = error("N/A")

        override suspend fun updateUserSessionsList(userSessionsList: PagedResult<UserSession>) = Unit
        override suspend fun updateUserSessionsPayloadList(userSessionsList: PagedResult<UserSessionPayload>) = Unit
        override suspend fun addUserSession(userSession: UserSession) = Unit
        override suspend fun removeUserSession(sessionId: UserSessionId) = Unit
        override suspend fun removeUserSessions(sessionIds: List<UserSessionId>) = Unit
        override suspend fun clear() = Unit
    }
}
