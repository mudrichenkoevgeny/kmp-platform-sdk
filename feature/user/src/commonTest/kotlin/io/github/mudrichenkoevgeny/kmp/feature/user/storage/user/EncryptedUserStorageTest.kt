package io.github.mudrichenkoevgeny.kmp.feature.user.storage.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.EncryptedSettingsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Instant

@InternalApi
class EncryptedUserStorageTest {

    private fun Instant.truncated(): Instant =
        Instant.fromEpochMilliseconds(this.toEpochMilliseconds())

    private fun fixTime(user: UserDetails): UserDetails = user.copy(
        createdAt = user.createdAt.truncated(),
        lastLoginAt = user.lastLoginAt?.truncated(),
        lastActiveAt = user.lastActiveAt?.truncated()
    )

    private fun fixIdentifierTime(identifier: UserIdentifier): UserIdentifier = identifier.copy(
        createdAt = identifier.createdAt.truncated(),
        updatedAt = identifier.updatedAt?.truncated()
    )

    private fun fixSessionTime(session: UserSession): UserSession = session.copy(
        createdAt = session.createdAt.truncated(),
        updatedAt = session.updatedAt?.truncated(),
        expiresAt = session.expiresAt.truncated(),
        lastAccessedAt = session.lastAccessedAt.truncated(),
        lastReauthenticatedAt = session.lastReauthenticatedAt.truncated()
    )

    @Test
    fun currentUser_roundTripsThroughStorage() = runTest {
        val storage = EncryptedUserStorage(EncryptedSettingsMock())
        val expectedUser = fixTime(userDetailsMock())

        storage.updateCurrentUser(expectedUser)
        val actualUser = storage.getCurrentUser()

        assertEquals(expectedUser, actualUser)
    }

    @Test
    fun observeCurrentUser_emitsNullThenValue() = runTest {
        val storage = EncryptedUserStorage(EncryptedSettingsMock())
        val expectedUser = fixTime(userDetailsMock())

        val firstValue = storage.observeCurrentUser().first()
        assertNull(firstValue)

        storage.updateCurrentUser(expectedUser)

        val secondValue = storage.observeCurrentUser().first { it != null }
        assertEquals(expectedUser, secondValue)
    }

    @Test
    fun userIdentifiers_roundTrip() = runTest {
        val storage = EncryptedUserStorage(EncryptedSettingsMock())
        val identifier = fixIdentifierTime(userIdentifierMock())
        val inputPagedResult = pagedResultMock(items = listOf(identifier))
        val expectedPagedResult = inputPagedResult.copy(pageSize = 1)

        storage.updateUserIdentifiersList(inputPagedResult)
        val actualResult = storage.getUserIdentifiersList()

        assertEquals(expectedPagedResult, actualResult)
    }

    @Test
    fun userSessions_roundTrip() = runTest {
        val storage = EncryptedUserStorage(EncryptedSettingsMock())
        val session = fixSessionTime(userSessionMock())
        val inputPagedResult = pagedResultMock(items = listOf(session))
        val expectedPagedResult = inputPagedResult.copy(pageSize = 1)

        storage.updateUserSessionsList(inputPagedResult)
        val actualResult = storage.getUserSessionsList()

        assertEquals(expectedPagedResult, actualResult)
    }

    @Test
    fun clear_removesAllSlices() = runTest {
        val storage = EncryptedUserStorage(EncryptedSettingsMock())

        storage.updateCurrentUser(userDetailsMock())
        storage.updateUserIdentifiersList(pagedResultMock(items = listOf(userIdentifierMock())))
        storage.updateUserSessionsList(pagedResultMock(items = listOf(userSessionMock())))

        storage.clear()

        assertNull(storage.getCurrentUser())

        val emptyIdentifiers = storage.getUserIdentifiersList()
        assertEquals(0, emptyIdentifiers.items.size)

        val emptySessions = storage.getUserSessionsList()
        assertEquals(0, emptySessions.items.size)
    }
}