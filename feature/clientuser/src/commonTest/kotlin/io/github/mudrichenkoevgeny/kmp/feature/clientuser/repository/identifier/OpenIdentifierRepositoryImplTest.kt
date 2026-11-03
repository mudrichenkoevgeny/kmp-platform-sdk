package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.network.api.identifier.OpenIdentifiersApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.confirmation.ConfirmationRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.model.identifier.createTestUserIdentifierSummaryPayload
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class OpenIdentifierRepositoryImplTest {

    @Test
    fun `getUserIdentifiers should return success from api`() = runTest {
        val payload = createTestUserIdentifierSummaryPayload()
        val paged = createTestPagedResult(listOf(payload))
        val api = OpenIdentifiersApiMock().apply {
            getUserIdentifiersResult = AppResult.Success(paged)
        }
        val repository = OpenIdentifierRepositoryImpl(api, ConfirmationRepositoryMock())

        val result = repository.getUserIdentifiers()

        assertIs<AppResult.Success<PagedResult<UserIdentifierSummary>>>(result)
        assertEquals(1, result.data.items.size)
        assertEquals(payload.identifier, result.data.items.first().identifier)
    }

    @Test
    fun `deleteUserIdentifier should return success from api`() = runTest {
        val id = UserIdentifierId.generate()
        val api = OpenIdentifiersApiMock().apply {
            deleteUserIdentifierResult = AppResult.Success(Unit)
        }
        val repository = OpenIdentifierRepositoryImpl(api, ConfirmationRepositoryMock())

        val result = repository.deleteUserIdentifier(id)

        assertIs<AppResult.Success<Unit>>(result)
    }
}
