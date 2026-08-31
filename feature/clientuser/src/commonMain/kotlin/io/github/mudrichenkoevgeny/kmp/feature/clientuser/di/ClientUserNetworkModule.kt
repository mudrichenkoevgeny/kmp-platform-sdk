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
    /** API for email, phone and external logins. */
    val loginApi by lazy { KtorOpenLoginApi(httpClient) }
    /** API for user registration. */
    val registrationApi by lazy { KtorRegistrationApi(httpClient) }
    /** API for session token refreshing. */
    val refreshTokenApi by lazy { KtorOpenRefreshTokenApi(httpClient) }
    /** API for password recovery. */
    val resetPasswordApi by lazy { KtorResetPasswordApi(httpClient) }
    /** API for public authentication settings. */
    val authSettingsApi by lazy { KtorOpenAuthSettingsApi(httpClient) }

    // Identifier
    /** API for managing user identifiers (email, phone). */
    val identifiersApi by lazy { KtorOpenIdentifiersApi(httpClient) }

    // Session
    /** API for managing active user sessions. */
    val sessionApi by lazy { KtorOpenSessionApi(httpClient) }

    // User
    /** API for fetching user profile data. */
    val userApi by lazy { KtorOpenUserApi(httpClient) }
    /** API for managing user security settings (TOTP). */
    val userSecurityApi by lazy { KtorOpenUserUserSecurityApi(httpClient) }

    // Configuration
    /** API for fetching combined user configuration. */
    val userConfigurationApi by lazy { KtorUserConfigurationApi(httpClient) }
}
