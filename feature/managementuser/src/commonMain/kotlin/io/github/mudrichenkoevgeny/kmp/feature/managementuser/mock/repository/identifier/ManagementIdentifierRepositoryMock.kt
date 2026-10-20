package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.ManagementIdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierSummary

@InternalApi
open class ManagementIdentifierRepositoryMock : ManagementIdentifierRepository {

    var getIdentifiersResultProvider: () -> AppResult<PagedResult<UserIdentifierSummary>> = {
        AppResult.Success(
            createTestPagedResult(
                listOf(createTestUserIdentifierSummary())
            )
        )
    }

    var getUserIdentifiersResultProvider: (UserId) -> AppResult<PagedResult<UserIdentifierSummary>> = { _ ->
        AppResult.Success(
            createTestPagedResult(
                listOf(createTestUserIdentifierSummary())
            )
        )
    }

    var getIdentifierResultProvider: (String) -> AppResult<UserIdentifierPrivate> = {
        AppResult.Success(createTestUserIdentifierPrivate())
    }

    var deleteIdentifierResultProvider: (UserId, String) -> AppResult<Unit> = { _, _ -> AppResult.Success(Unit) }
    var deleteIdentifierPasswordResultProvider: (UserId, String) -> AppResult<Unit> = { _, _ -> AppResult.Success(Unit) }

    var lastUserId: UserId? = null
    var lastIdentifierId: String? = null
    var lastIdentifiers: List<String>? = null
    var lastSortBy: UserSortValues.UserIdentifierSortBy? = null

    override suspend fun getIdentifiers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        lastSortBy = sortBy
        lastIdentifiers = identifiers
        return getIdentifiersResultProvider()
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
        lastUserId = userId
        lastSortBy = sortBy
        lastIdentifiers = identifiers
        return getUserIdentifiersResultProvider(userId)
    }

    override suspend fun getIdentifier(identifierId: String): AppResult<UserIdentifierPrivate> {
        lastIdentifierId = identifierId
        return getIdentifierResultProvider(identifierId)
    }

    override suspend fun deleteIdentifier(userId: UserId, identifierId: String): AppResult<Unit> {
        lastUserId = userId
        lastIdentifierId = identifierId
        return deleteIdentifierResultProvider(userId, identifierId)
    }

    override suspend fun deleteIdentifierPassword(userId: UserId, identifierId: String): AppResult<Unit> {
        lastUserId = userId
        lastIdentifierId = identifierId
        return deleteIdentifierPasswordResultProvider(userId, identifierId)
    }
}