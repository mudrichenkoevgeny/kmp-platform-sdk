package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.toAuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.MockAuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.MockUserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireAuthDataResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class LoginByEmailUseCaseTest {

    @Test
    fun execute_persistsTokensAndUser_whenLoginSucceeds() = runTest {
        val authData = wireAuthDataResponse().toAuthData()
        val loginRepository = FakeLoginRepository().apply { result = AppResult.Success(authData) }
        val authStorage = MockAuthStorage()
        val userStorage = MockUserStorage()
        val useCase = LoginByEmailUseCase(loginRepository, authStorage, userStorage)

        val loginResult = useCase.execute(TEST_EMAIL, TEST_PASSWORD)

        assertIs<AppResult.Success<AuthData>>(loginResult)
        assertEquals(TEST_EMAIL, loginRepository.lastEmail)
        assertEquals(TEST_PASSWORD, loginRepository.lastPassword)
        assertEquals(authData.sessionToken.accessToken, authStorage.getAccessToken())
        assertEquals(authData.sessionToken.refreshToken, authStorage.getRefreshToken())
        assertEquals(authData.sessionToken.expiresAt, authStorage.getExpiresAt())
        assertEquals(authData.currentUser, userStorage.getCurrentUser())
    }

    @Test
    fun execute_doesNotPersist_whenLoginFails() = runTest {
        val err = CommonError.Unknown(isRetryable = NOT_RETRYABLE)
        val loginRepository = FakeLoginRepository().apply { result = AppResult.Error(err) }
        val authStorage = MockAuthStorage()
        val userStorage = MockUserStorage()
        val useCase = LoginByEmailUseCase(loginRepository, authStorage, userStorage)

        val loginResult = useCase.execute(TEST_EMAIL, TEST_PASSWORD)

        assertIs<AppResult.Error>(loginResult)
        assertNull(authStorage.getAccessToken())
        assertNull(authStorage.getRefreshToken())
        assertNull(userStorage.getCurrentUser())
    }

    private class FakeLoginRepository : LoginRepository {
        var result: AppResult<AuthData> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))
        var lastEmail: String? = null
        var lastPassword: String? = null

        override suspend fun loginByEmail(email: String, password: String): AppResult<AuthData> {
            lastEmail = email
            lastPassword = password
            return result
        }

        override suspend fun loginByPhone(
            phoneNumber: String,
            confirmationCode: String
        ): AppResult<AuthData> = error(STUB_NOT_USED)

        override suspend fun loginByExternalAuthProvider(
            authProvider: UserAuthProvider,
            token: String
        ): AppResult<AuthData> = error(STUB_NOT_USED)

        override suspend fun sendLoginConfirmationToPhone(
            phoneNumber: String
        ): AppResult<SendConfirmationData> = error(STUB_NOT_USED)

        override fun getRemainingLoginConfirmationDelayInSeconds(phoneNumber: String): Int = error(STUB_NOT_USED)
    }

    private companion object {
        private const val TEST_EMAIL = "a@b.c"
        private const val TEST_PASSWORD = "pw"
        private const val NOT_RETRYABLE = false
        private const val STUB_NOT_USED = "not used"
    }
}
