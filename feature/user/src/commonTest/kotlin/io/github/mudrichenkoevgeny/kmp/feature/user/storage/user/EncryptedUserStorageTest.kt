package io.github.mudrichenkoevgeny.kmp.feature.user.storage.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.EncryptedSettingsMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserPrivate
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

    private fun fixTime(user: UserPrivate): UserPrivate = user.copy(
        createdAt = user.createdAt.truncated(),
        lastLoginAt = user.lastLoginAt?.truncated(),
        lastActiveAt = user.lastActiveAt?.truncated()
    )

    @Test
    fun currentUser_roundTripsThroughStorage() = runTest {
        val storage = EncryptedUserStorage(EncryptedSettingsMock())
        val expectedUser = fixTime(createTestUserPrivate())

        storage.updateCurrentUser(expectedUser)
        val actualUser = storage.getCurrentUser()

        assertEquals(expectedUser, actualUser)
    }

    @Test
    fun observeCurrentUser_emitsNullThenValue() = runTest {
        val storage = EncryptedUserStorage(EncryptedSettingsMock())
        val expectedUser = fixTime(createTestUserPrivate())

        val firstValue = storage.observeCurrentUser().first()
        assertNull(firstValue)

        storage.updateCurrentUser(expectedUser)

        val secondValue = storage.observeCurrentUser().first { it != null }
        assertEquals(expectedUser, secondValue)
    }

    @Test
    fun clear_removesCurrentUser() = runTest {
        val storage = EncryptedUserStorage(EncryptedSettingsMock())

        storage.updateCurrentUser(createTestUserPrivate())
        storage.clear()

        assertNull(storage.getCurrentUser())
    }
}
