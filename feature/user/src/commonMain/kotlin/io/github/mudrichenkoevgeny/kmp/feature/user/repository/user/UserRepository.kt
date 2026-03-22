package io.github.mudrichenkoevgeny.kmp.feature.user.repository.user

import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.CurrentUser
import kotlinx.coroutines.flow.Flow

/**
 * Read-side access to the signed-in user snapshot as observed from local storage (and any future
 * network-backed refresh paths).
 */
interface UserRepository {
    /** [Flow] of the cached [CurrentUser], or `null` when none is stored. */
    val currentUser: Flow<CurrentUser?>

//    suspend fun getUser(): AppResult<CurrentUser>
//    suspend fun deleteUser(): AppResult<Unit>
//    suspend fun getUserSettings(): AppResult<UserSettings>
}