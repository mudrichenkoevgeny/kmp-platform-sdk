package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.identifier.userIdentifierPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.identifier.OpenIdentifiersApi
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class OpenIdentifierRepositoryImplTest {

    private fun pagedPayload(items: List<UserIdentifierPayload>) = PagedResult(
        items = items,
        totalCount = items.size.toLong(),
        pageNumber = 1,
        pageSize = 20,
        totalPages = 1
    )

    @Test
    fun `getUserIdentifiers should update storage on success`() = runTest {
        val payload = userIdentifierPayloadMock()
        val paged = pagedPayload(listOf(payload))
        val api = FakeIdentifiersApi(identifiersResult = AppResult.Success(paged))
        val storage = FakeUserStorage()
        val repository = OpenIdentifierRepositoryImpl(api, FakeConfirmationRepository(), storage)

        val result = repository.getUserIdentifiers(null, null, null, null, null, null)

        assertIs<AppResult.Success<PagedResult<UserIdentifier>>>(result)
        assertEquals(1, result.data.items.size)
        assertEquals(payload.identifier, result.data.items.first().identifier)
    }

    @Test
    fun `deleteUserIdentifier should remove from storage on success`() = runTest {
        val id = UserIdentifierId.generate()
        val api = FakeIdentifiersApi(deleteResult = AppResult.Success(Unit))
        val storage = FakeUserStorage()
        val repository = OpenIdentifierRepositoryImpl(api, FakeConfirmationRepository(), storage)

        val result = repository.deleteUserIdentifier(id)

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(id, storage.lastRemovedIdentifierId)
    }

    private class FakeIdentifiersApi(
        private val identifiersResult: AppResult<PagedResult<UserIdentifierPayload>> = AppResult.Success(PagedResult(emptyList(), 0, 1, 20, 0)),
        private val deleteResult: AppResult<Unit> = AppResult.Success(Unit)
    ) : OpenIdentifiersApi {
        override suspend fun getUserIdentifier(userIdentifierId: UserIdentifierId): AppResult<UserIdentifierPayload> = error("N/A")
        override suspend fun getUserIdentifiers(pageNumber: Int?, pageSize: Int?, sortBy: UserSortValues.UserIdentifierSortBy?, sortOrder: SortOrder?, userAuthProviders: List<UserAuthProvider>?, identifiers: List<String>?): AppResult<PagedResult<UserIdentifierPayload>> = identifiersResult
        override suspend fun deleteUserIdentifier(identifierId: UserIdentifierId): AppResult<Unit> = deleteResult
        override suspend fun addUserIdentifierEmail(request: io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierEmailRequest): AppResult<UserIdentifierPayload> = error("N/A")
        override suspend fun addUserIdentifierPhone(request: io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierPhoneRequest): AppResult<UserIdentifierPayload> = error("N/A")
        override suspend fun addUserIdentifierExternalAuthProvider(request: io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierExternalAuthProviderRequest): AppResult<UserIdentifierPayload> = error("N/A")
        override suspend fun sendAddEmailIdentifierConfirmation(request: io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest): AppResult<io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload> = error("N/A")
        override suspend fun sendAddPhoneIdentifierConfirmation(request: io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest): AppResult<io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload> = error("N/A")
        override suspend fun emailChangePassword(request: io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.EmailPasswordChangeRequest): AppResult<Unit> = error("N/A")
    }

    private class FakeConfirmationRepository : ConfirmationRepository {
        override suspend fun <T> executeWithTimer(type: io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType, identifier: String, action: suspend () -> AppResult<T>): AppResult<T> = action()
        override fun getRemainingDelay(type: io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType, identifier: String): Int = 0
    }

    private class FakeUserStorage : UserStorage {
        var lastUpdatedIdentifiersPayload: PagedResult<UserIdentifierPayload>? = null
        var lastRemovedIdentifierId: UserIdentifierId? = null

        override suspend fun getCurrentUser() = error("N/A")
        override fun observeCurrentUser() = error("N/A")
        override suspend fun updateCurrentUser(currentUser: io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails) = Unit
        override suspend fun getUserIdentifiersList(pageNumber: Int?, pageSize: Int?, sortBy: UserSortValues.UserIdentifierSortBy?, sortOrder: SortOrder?, userIds: List<String>?, userAuthProviders: List<UserAuthProvider>?, identifiers: List<String>?): PagedResult<UserIdentifier> = PagedResult(emptyList(), 0, 1, 20, 0)
        override fun observeUserIdentifiersList(pageNumber: Int?, pageSize: Int?, sortBy: UserSortValues.UserIdentifierSortBy?, sortOrder: SortOrder?, userIds: List<String>?, userAuthProviders: List<UserAuthProvider>?, identifiers: List<String>?) = error("N/A")
        override suspend fun updateUserIdentifiersList(userIdentifiersList: PagedResult<UserIdentifier>) = Unit
        override suspend fun updateUserIdentifiersPayloadList(userIdentifiersList: PagedResult<UserIdentifierPayload>) {
            lastUpdatedIdentifiersPayload = userIdentifiersList
        }
        override suspend fun addUserIdentifier(userIdentifier: UserIdentifier) = Unit
        override suspend fun removeUserIdentifier(identifierId: UserIdentifierId) {
            lastRemovedIdentifierId = identifierId
        }
        override suspend fun getUserSessionsList(pageNumber: Int?, pageSize: Int?, sortBy: UserSortValues.UserSessionSortBy?, sortOrder: SortOrder?, userIds: List<String>?, userRoles: List<io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole>?, identifiers: List<String>?, identifierIds: List<String>?, userAuthProviders: List<UserAuthProvider>?, clientTypes: List<io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType>?, userAgents: List<String>?, ipAddresses: List<String>?, languages: List<String>?, deviceIds: List<String>?, deviceNames: List<String>?, appVersions: List<String>?, operationSystemVersions: List<String>?) = error("N/A")
        override fun observeUserSessionsList(pageNumber: Int?, pageSize: Int?, sortBy: UserSortValues.UserSessionSortBy?, sortOrder: SortOrder?, userIds: List<String>?, userRoles: List<io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole>?, identifiers: List<String>?, identifierIds: List<String>?, userAuthProviders: List<UserAuthProvider>?, clientTypes: List<io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType>?, userAgents: List<String>?, ipAddresses: List<String>?, languages: List<String>?, deviceIds: List<String>?, deviceNames: List<String>?, appVersions: List<String>?, operationSystemVersions: List<String>?) = error("N/A")
        override suspend fun updateUserSessionsList(userSessionsList: io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult<io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession>) = Unit
        override suspend fun updateUserSessionsPayloadList(userSessionsList: io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult<io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload>) = Unit
        override suspend fun addUserSession(userSession: io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession) = Unit
        override suspend fun removeUserSession(sessionId: io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId) = Unit
        override suspend fun removeUserSessions(sessionIds: List<io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId>) = Unit
        override suspend fun clear() = Unit
    }
}
