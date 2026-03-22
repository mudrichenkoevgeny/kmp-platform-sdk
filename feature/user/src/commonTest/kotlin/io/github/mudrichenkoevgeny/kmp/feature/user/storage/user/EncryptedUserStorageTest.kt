package io.github.mudrichenkoevgeny.kmp.feature.user.storage.user

import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.MockEncryptedSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.model.user.mockCurrentUser
import io.github.mudrichenkoevgeny.kmp.feature.user.model.session.UserSession
import io.github.mudrichenkoevgeny.kmp.feature.user.model.session.UserSessionId
import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.UserId
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Clock

class EncryptedUserStorageTest {

    @Test
    fun currentUser_roundTripsThroughStorage() = runTest {
        val storage = EncryptedUserStorage(MockEncryptedSettings())
        val user = mockCurrentUser()

        storage.updateCurrentUser(user)

        assertEquals(user, storage.getCurrentUser())
    }

    @Test
    fun observeCurrentUser_emitsNullThenValue() = runTest {
        val storage = EncryptedUserStorage(MockEncryptedSettings())
        val flow = storage.observeCurrentUser()
        assertNull(flow.first())

        val user = mockCurrentUser()
        storage.updateCurrentUser(user)

        assertEquals(user, flow.first())
    }

    @Test
    fun userIdentifiers_roundTrip() = runTest {
        val storage = EncryptedUserStorage(MockEncryptedSettings())
        val identifiers = listOf(sampleIdentifier())

        storage.updateUserIdentifiersList(identifiers)

        assertContentEquals(identifiers, storage.getUserIdentifiersList())
    }

    @Test
    fun userSessions_roundTrip() = runTest {
        val storage = EncryptedUserStorage(MockEncryptedSettings())
        val sessions = listOf(sampleSession())

        storage.updateUserSessionsList(sessions)

        assertContentEquals(sessions, storage.getUserSessionsList())
    }

    @Test
    fun clear_removesAllSlices() = runTest {
        val storage = EncryptedUserStorage(MockEncryptedSettings())
        storage.updateCurrentUser(mockCurrentUser())
        storage.updateUserIdentifiersList(listOf(sampleIdentifier()))
        storage.updateUserSessionsList(listOf(sampleSession()))

        storage.clear()

        assertNull(storage.getCurrentUser())
        assertContentEquals(emptyList(), storage.getUserIdentifiersList())
        assertContentEquals(emptyList(), storage.getUserSessionsList())
    }

    private fun sampleIdentifier(): UserIdentifier {
        val now = Clock.System.now()
        return UserIdentifier(
            id = UserIdentifierId.generate(),
            userId = UserId.generate(),
            userAuthProvider = UserAuthProvider.EMAIL,
            identifier = "user@example.com",
            createdAt = now,
            updatedAt = null
        )
    }

    private fun sampleSession(): UserSession {
        return UserSession(
            id = UserSessionId.generate(),
            identifierId = UserIdentifierId.generate(),
            identifierAuthProvider = UserAuthProvider.EMAIL,
            expiresAt = null,
            clientType = null,
            userAgent = null,
            ipAddress = null,
            deviceName = null,
            createdAt = null,
            lastAccessedAt = null
        )
    }
}
