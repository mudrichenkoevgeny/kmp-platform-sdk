package io.github.mudrichenkoevgeny.kmp.feature.user.repository.user

import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.CurrentUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val currentUser: Flow<CurrentUser?>

//    suspend fun getUser(): AppResult<CurrentUser>
//    suspend fun deleteUser(): AppResult<Unit>
//    suspend fun getUserSettings(): AppResult<UserSettings>
}