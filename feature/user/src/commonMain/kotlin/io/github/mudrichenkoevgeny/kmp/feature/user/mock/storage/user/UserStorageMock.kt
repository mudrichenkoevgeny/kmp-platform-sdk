package io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserPrivate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@InternalApi
class UserStorageMock : UserStorage {

    private val currentUserFlow = MutableStateFlow<UserPrivate?>(null)

    var isCleared = false
        private set

    override suspend fun getCurrentUser(): UserPrivate? = currentUserFlow.value

    override fun observeCurrentUser(): Flow<UserPrivate?> = currentUserFlow

    override suspend fun updateCurrentUser(currentUser: UserPrivate) {
        currentUserFlow.value = currentUser
    }

    override suspend fun clear() {
        currentUserFlow.value = null
        isCleared = true
    }
}
