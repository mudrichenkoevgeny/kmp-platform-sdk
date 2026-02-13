package io.github.mudrichenkoevgeny.kmp.feature.user.di

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.network.httpclient.setupCommonConfig
import io.github.mudrichenkoevgeny.kmp.feature.user.api.UserApi
import io.github.mudrichenkoevgeny.kmp.feature.user.api.auth.login.KtorLoginApi
import io.github.mudrichenkoevgeny.kmp.feature.user.api.auth.login.LoginApi
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient.setupAuthConfig
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.EncryptedAuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.EncryptedUserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.root.LoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.root.LoginRootComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByEmailUseCase
import io.ktor.client.HttpClient

class UserComponent(
    private val commonComponent: CommonComponent,
    private val baseUrl: String
) {

    private val authStorage: AuthStorage by lazy {
        EncryptedAuthStorage(commonComponent.encryptedSettings)
    }

    private val userStorage: UserStorage by lazy {
        EncryptedUserStorage(commonComponent.encryptedSettings)
    }

    val httpClient: HttpClient by lazy {
        HttpClient {
            setupCommonConfig(baseUrl, commonComponent.deviceInfo)
            setupAuthConfig(baseUrl, authStorage)
        }
    }

    val loginApi: LoginApi by lazy { KtorLoginApi(httpClient) }
    val userApi: UserApi by lazy { UserApi(httpClient) }

    val loginRepository: LoginRepository by lazy {
        LoginRepositoryImpl(loginApi)
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(userStorage)
    }

    val loginByEmailUseCase: LoginByEmailUseCase by lazy {
        LoginByEmailUseCase(
            loginRepository = loginRepository,
            authStorage = authStorage,
            userStorage = userStorage
        )
    }

    fun createLoginRootDialogComponent(
        componentContext: ComponentContext,
        onFinished: () -> Unit
    ): LoginRootComponent {
        return LoginRootComponentImpl(
            componentContext = componentContext,
            onFinished = onFinished
        )
    }

    fun clear() {
        httpClient.close()
    }
}