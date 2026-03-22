package io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user

import io.github.mudrichenkoevgeny.kmp.feature.user.model.session.UserSession
import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.CurrentUser
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * In-memory [UserStorage] for tests and previews; no persistence.
 */
class MockUserStorage : UserStorage {

    private val currentUserFlow = MutableStateFlow<CurrentUser?>(null)
    private val userIdentifiers = mutableListOf<UserIdentifier>()
    private val userSessions = mutableListOf<UserSession>()

    override fun observeCurrentUser(): Flow<CurrentUser?> = currentUserFlow

    override suspend fun getCurrentUser(): CurrentUser? = currentUserFlow.value

    override suspend fun updateCurrentUser(currentUser: CurrentUser) {
        currentUserFlow.value = currentUser
    }

    override suspend fun getUserIdentifiersList(): List<UserIdentifier> = userIdentifiers.toList()

    override suspend fun updateUserIdentifiersList(userIdentifiersList: List<UserIdentifier>) {
        userIdentifiers.clear()
        userIdentifiers.addAll(userIdentifiersList)
    }

    override suspend fun getUserSessionsList(): List<UserSession> = userSessions.toList()

    override suspend fun updateUserSessionsList(userSessionsList: List<UserSession>) {
        userSessions.clear()
        userSessions.addAll(userSessionsList)
    }

    override suspend fun clear() {
        currentUserFlow.value = null
        userIdentifiers.clear()
        userSessions.clear()
    }
}
