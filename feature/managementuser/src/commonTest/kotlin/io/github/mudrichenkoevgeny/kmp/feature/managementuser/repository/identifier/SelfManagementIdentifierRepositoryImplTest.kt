package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.identifier.SelfManagementIdentifiersApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.identifier.userIdentifierPrivatePayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.identifier.userIdentifierSummaryPayloadMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifierSummary
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.uuid.Uuid

@InternalApi
class SelfManagementIdentifierRepositoryImplTest {

    private val api = SelfManagementIdentifiersApiMock()
    private val repo = SelfManagementIdentifierRepositoryImpl(api)

    @Test
    fun `getUserIdentifier forwards request and maps result`() = runTest {
        val payload = userIdentifierPrivatePayloadMock()
        api.getUserIdentifierResult = AppResult.Success(payload)
        val id = UserIdentifierId(Uuid.random())

        val result = repo.getUserIdentifier(id)

        val success = assertIs<AppResult.Success<UserIdentifierPrivate>>(result)
        assertEquals(payload.toUserIdentifierPrivate(), success.data)
    }

    @Test
    fun `getUserIdentifiers forwards request and maps paged result`() = runTest {
        val payload = userIdentifierSummaryPayloadMock()
        val wire = pagedResultMock(items = listOf(payload), totalCount = 1)
        api.getUserIdentifiersResult = AppResult.Success(wire)

        val result = repo.getUserIdentifiers(
            pageNumber = 1,
            pageSize = 20,
            sortBy = null,
            sortOrder = null,
            userAuthProviders = null,
            identifiers = null
        )

        val success = assertIs<AppResult.Success<PagedResult<UserIdentifierSummary>>>(result)
        assertEquals(1, success.data.items.size)
        assertEquals(payload.toUserIdentifierSummary(), success.data.items.first())
    }

    @Test
    fun `emailChangePassword forwards request`() = runTest {
        api.emailChangePasswordResult = AppResult.Success(Unit)

        val id = UserIdentifierId(Uuid.random())
        val result = repo.emailChangePassword(id, "old-pass", "new-pass")

        assertIs<AppResult.Success<Unit>>(result)
    }

    @Test
    fun `unsupported operations return ContractViolation`() = runTest {
        val id = UserIdentifierId(Uuid.random())
        val result = repo.deleteUserIdentifier(id)

        val error = assertIs<AppResult.Error>(result)
        assertIs<CommonError.ContractViolation>(error.error)
    }
}