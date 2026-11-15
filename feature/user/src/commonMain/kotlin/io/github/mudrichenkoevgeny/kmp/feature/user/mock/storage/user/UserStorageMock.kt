package io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Mock implementation of [UserStorage] for testing.
 */
@InternalApi
public class UserStorageMock : UserStorage {

    private val currentUserFlow = MutableStateFlow<UserPrivate?>(null)

    public var isCleared: Boolean = false
        private set

    public override suspend fun getCurrentUser(): UserPrivate? = currentUserFlow.value

    public override fun observeCurrentUser(): Flow<UserPrivate?> = currentUserFlow

    public override suspend fun updateCurrentUser(currentUser: UserPrivate) {
        currentUserFlow.value = currentUser
    }

    public override suspend fun clear() {
        currentUserFlow.value = null
        isCleared = true
    }
}
