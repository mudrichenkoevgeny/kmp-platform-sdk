package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.ManagementIdentifierRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.globallist.ManagementGlobalIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.userlist.ManagementUserIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierSummary

/**
 * Mock implementation of [ManagementIdentifierRepository] for testing.
 */
@InternalApi
public open class ManagementIdentifierRepositoryMock : ManagementIdentifierRepository {

    public var getIdentifiersResultProvider: () -> AppResult<PagedResult<UserIdentifierSummary>> = {
        AppResult.Success(
            createTestPagedResult(
                listOf(createTestUserIdentifierSummary())
            )
        )
    }

    public var getUserIdentifiersResultProvider:
            (ManagementUserIdentifierListParams) -> AppResult<PagedResult<UserIdentifierSummary>> = { _ ->
        AppResult.Success(
            createTestPagedResult(
                listOf(createTestUserIdentifierSummary())
            )
        )
    }

    public var getIdentifierResultProvider: (String) -> AppResult<UserIdentifierPrivate> = {
        AppResult.Success(createTestUserIdentifierPrivate())
    }

    public var deleteIdentifierResultProvider: (UserId, String) -> AppResult<Unit> = { _, _ -> AppResult.Success(Unit) }
    public var deleteIdentifierPasswordResultProvider: (UserId, String) -> AppResult<Unit> =
        { _, _ -> AppResult.Success(Unit) }

    public var lastUserId: UserId? = null
    public var lastIdentifierId: String? = null
    public var lastGlobalParams: ManagementGlobalIdentifierListParams? = null
    public var lastUserParams: ManagementUserIdentifierListParams? = null

    public override suspend fun getIdentifiers(
        params: ManagementGlobalIdentifierListParams?
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        lastGlobalParams = params
        return getIdentifiersResultProvider()
    }

    public override suspend fun getUserIdentifiers(
        params: ManagementUserIdentifierListParams
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        lastUserParams = params
        return getUserIdentifiersResultProvider(params)
    }

    public override suspend fun getIdentifier(identifierId: String): AppResult<UserIdentifierPrivate> {
        lastIdentifierId = identifierId
        return getIdentifierResultProvider(identifierId)
    }

    public override suspend fun deleteIdentifier(
        userId: UserId,
        identifierId: String
    ): AppResult<Unit> {
        lastUserId = userId
        lastIdentifierId = identifierId
        return deleteIdentifierResultProvider(userId, identifierId)
    }

    public override suspend fun deleteIdentifierPassword(
        userId: UserId,
        identifierId: String
    ): AppResult<Unit> {
        lastUserId = userId
        lastIdentifierId = identifierId
        return deleteIdentifierPasswordResultProvider(userId, identifierId)
    }
}
