package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.network.api.identifier.OpenIdentifiersApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.confirmation.ConfirmationRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.model.identifier.createTestUserIdentifierSummaryPayloadList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class OpenIdentifierRepositoryImplTest {

    @Test
    fun `getUserIdentifiers should update storage on success`() = runTest {
        val payloadList = createTestUserIdentifierSummaryPayloadList()
        val paged = createTestPagedResult(payloadList)
        val api = OpenIdentifiersApiMock().apply {
            getUserIdentifiersResult = AppResult.Success(paged)
        }
        val storage = UserStorageMock()
        val repository = OpenIdentifierRepositoryImpl(api, ConfirmationRepositoryMock(), storage)

        val result = repository.getUserIdentifiers(null, null, null, null, null, null)

        assertIs<AppResult.Success<PagedResult<UserIdentifierSummary>>>(result)
        assertEquals(payloadList.size, result.data.items.size)
        assertEquals(payloadList.first().identifier, result.data.items.first().identifier)
    }

    @Test
    fun `deleteUserIdentifier should remove from storage on success`() = runTest {
        val id = UserIdentifierId.generate()
        val api = OpenIdentifiersApiMock().apply {
            deleteUserIdentifierResult = AppResult.Success(Unit)
        }
        val storage = UserStorageMock()
        val repository = OpenIdentifierRepositoryImpl(api, ConfirmationRepositoryMock(), storage)

        val result = repository.deleteUserIdentifier(id)

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(id, storage.lastRemovedIdentifierId)
    }
}