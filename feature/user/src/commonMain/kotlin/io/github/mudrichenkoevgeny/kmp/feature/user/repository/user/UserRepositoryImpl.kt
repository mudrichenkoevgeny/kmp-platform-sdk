package io.github.mudrichenkoevgeny.kmp.feature.user.repository.user

import io.github.mudrichenkoevgeny.kmp.feature.user.model.user.CurrentUser
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    userStorage: UserStorage
) : UserRepository {

    override val currentUser: Flow<CurrentUser?> = userStorage.observeCurrentUser()
}