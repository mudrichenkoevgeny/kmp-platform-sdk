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

@InternalApi
open class ManagementIdentifierRepositoryMock : ManagementIdentifierRepository {

    var getIdentifiersResultProvider: () -> AppResult<PagedResult<UserIdentifierSummary>> = {
        AppResult.Success(
            createTestPagedResult(
                listOf(createTestUserIdentifierSummary())
            )
        )
    }

    var getUserIdentifiersResultProvider: (ManagementUserIdentifierListParams) -> AppResult<PagedResult<UserIdentifierSummary>> = { _ ->
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
    var lastGlobalParams: ManagementGlobalIdentifierListParams? = null
    var lastUserParams: ManagementUserIdentifierListParams? = null

    override suspend fun getIdentifiers(
        params: ManagementGlobalIdentifierListParams?
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        lastGlobalParams = params
        return getIdentifiersResultProvider()
    }

    override suspend fun getUserIdentifiers(
        params: ManagementUserIdentifierListParams
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        lastUserParams = params
        return getUserIdentifiersResultProvider(params)
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
