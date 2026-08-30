package io.github.mudrichenkoevgeny.kmp.feature.clientuser.di

import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.login.KtorOpenLoginApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.resetpassword.KtorResetPasswordApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.refreshtoken.KtorOpenRefreshTokenApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.registration.KtorRegistrationApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.settings.KtorOpenAuthSettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.configuration.KtorUserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.identifier.KtorOpenIdentifiersApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.session.KtorOpenSessionApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.user.KtorOpenUserApi
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.user.security.KtorOpenUserUserSecurityApi
import io.ktor.client.HttpClient
import kotlin.getValue

/**
 * Lazily constructs Ktor API clients from a shared [HttpClient].
 *
 * @param httpClient The host-configured Ktor client (typically from `CommonComponent`).
 */
internal class ClientUserNetworkModule(
    private val httpClient: HttpClient
) {
    // Auth
    val loginApi by lazy { KtorOpenLoginApi(httpClient) }
    val registrationApi by lazy { KtorRegistrationApi(httpClient) }
    val refreshTokenApi by lazy { KtorOpenRefreshTokenApi(httpClient) }
    val resetPasswordApi by lazy { KtorResetPasswordApi(httpClient) }
    val authSettingsApi by lazy { KtorOpenAuthSettingsApi(httpClient) }

    // Identifier
    val identifiersApi by lazy { KtorOpenIdentifiersApi(httpClient) }

    // Session
    val sessionApi by lazy { KtorOpenSessionApi(httpClient) }

    // User
    val userApi by lazy { KtorOpenUserApi(httpClient) }
    val userSecurityApi by lazy { KtorOpenUserUserSecurityApi(httpClient) }

    // Configuration
    val userConfigurationApi by lazy { KtorUserConfigurationApi(httpClient) }
}
