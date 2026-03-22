package io.github.mudrichenkoevgeny.kmp.feature.user.repository.user

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.MockUserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.UserApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.user.CurrentUserResponse
import kotlin.test.Test
import kotlin.test.assertSame

class UserRepositoryImplTest {

    @Test
    fun currentUser_isSameFlowReturnedByStorageObserveCurrentUser() {
        val storage = MockUserStorage()
        val storageFlow = storage.observeCurrentUser()
        val repo = UserRepositoryImpl(storage, FakeUserApi())

        assertSame(storageFlow, repo.currentUser)
    }

    private class FakeUserApi : UserApi {
        override suspend fun getUser(): AppResult<CurrentUserResponse> = error(STUB_NOT_USED)

        override suspend fun deleteUser(): AppResult<Unit> = error(STUB_NOT_USED)
    }

    private companion object {
        private const val STUB_NOT_USED = "not used in UserRepositoryImpl"
    }
}
