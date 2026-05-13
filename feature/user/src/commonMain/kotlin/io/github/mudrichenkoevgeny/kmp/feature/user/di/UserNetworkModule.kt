package io.github.mudrichenkoevgeny.kmp.feature.user.di

import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.login.KtorLoginApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.resetpassword.KtorResetPasswordApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.refreshtoken.KtorRefreshTokenApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.registration.KtorRegistrationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.settings.KtorAuthSettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.configuration.KtorUserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.identifier.KtorIdentifiersApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session.KtorSessionApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.KtorUserApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.security.KtorUserUserSecurityApi
import io.ktor.client.HttpClient
import kotlin.getValue

/**
 * Lazily constructs Ktor API clients from a shared [HttpClient].
 *
 * @param httpClient The host-configured Ktor client (typically from `CommonComponent`).
 */
internal class UserNetworkModule(
    private val httpClient: HttpClient
) {
    // Auth
    val loginApi by lazy { KtorLoginApi(httpClient) }
    val registrationApi by lazy { KtorRegistrationApi(httpClient) }
    val refreshTokenApi by lazy { KtorRefreshTokenApi(httpClient) }
    val resetPasswordApi by lazy { KtorResetPasswordApi(httpClient) }
    val authSettingsApi by lazy { KtorAuthSettingsApi(httpClient) }

    // Identifier
    val identifiersApi by lazy { KtorIdentifiersApi(httpClient) }

    // Session
    val sessionApi by lazy { KtorSessionApi(httpClient) }

    // User
    val userApi by lazy { KtorUserApi(httpClient) }
    val userSecurityApi by lazy { KtorUserUserSecurityApi(httpClient) }

    // Configuration
    val userConfigurationApi by lazy { KtorUserConfigurationApi(httpClient) }
}