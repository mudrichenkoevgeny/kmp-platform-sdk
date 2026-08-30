package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.identifier.ManagementIdentifierApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.identifier.userIdentifierPayloadMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifier
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ManagementIdentifierRepositoryImplTest {

    private val api = ManagementIdentifierApiMock()
    private val repo = ManagementIdentifierRepositoryImpl(api)

    @Test
    fun `getIdentifiers forwards request and maps paged result`() = runTest {
        val payload = userIdentifierPayloadMock()
        val wire = pagedResultMock(items = listOf(payload), totalCount = 1)
        api.getIdentifiersResult = AppResult.Success(wire)

        val result = repo.getIdentifiers(pageNumber = 1, pageSize = 20)

        val success = assertIs<AppResult.Success<PagedResult<UserIdentifier>>>(result)
        assertEquals(1, success.data.items.size)
        assertEquals(payload.toUserIdentifier(), success.data.items.first())
        assertEquals(wire.totalCount, success.data.totalCount)
    }

    @Test
    fun `getIdentifier forwards request and maps result`() = runTest {
        val payload = userIdentifierPayloadMock()
        api.getIdentifierResult = AppResult.Success(payload)

        val result = repo.getIdentifier("id-1")

        val success = assertIs<AppResult.Success<UserIdentifier>>(result)
        assertEquals(payload.toUserIdentifier(), success.data)
    }

    @Test
    fun `deleteIdentifier forwards request`() = runTest {
        api.deleteIdentifierResult = AppResult.Success(Unit)
        val userId = "550e8400-e29b-41d4-a716-446655440000".toUserIdOrThrow()

        val result = repo.deleteIdentifier(userId, "id-1")

        assertIs<AppResult.Success<Unit>>(result)
    }

    @Test
    fun `repository propagates api errors`() = runTest {
        api.getIdentifierResult = AppResult.Error(CommonError.Unknown())

        val result = repo.getIdentifier("id-1")

        assertIs<AppResult.Error>(result)
    }
}