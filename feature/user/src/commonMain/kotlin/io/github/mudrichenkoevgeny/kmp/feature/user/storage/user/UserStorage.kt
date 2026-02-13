package io.github.mudrichenkoevgeny.kmp.feature.user.storage.user

import io.github.mudrichenkoevgeny.kmp.feature.user.model.session.UserSession
import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.CurrentUser
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier
import kotlinx.coroutines.flow.Flow

interface UserStorage {
    suspend fun getCurrentUser(): CurrentUser?
    fun observeCurrentUser(): Flow<CurrentUser?>
    suspend fun updateCurrentUser(currentUser: CurrentUser)
    suspend fun getUserIdentifiersList(): List<UserIdentifier>
    suspend fun updateUserIdentifiersList(userIdentifiersList: List<UserIdentifier>)
    suspend fun getUserSessionsList(): List<UserSession>
    suspend fun updateUserSessionsList(userSessionsList: List<UserSession>)
    suspend fun clear()
}