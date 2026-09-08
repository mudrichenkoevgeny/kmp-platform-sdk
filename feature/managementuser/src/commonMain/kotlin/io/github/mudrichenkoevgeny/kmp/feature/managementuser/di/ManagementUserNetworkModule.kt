package io.github.mudrichenkoevgeny.kmp.feature.managementuser.di

import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.login.KtorSelfManagementLoginApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.refreshtoken.KtorSelfManagementRefreshTokenApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.resetpassword.KtorSelfManagementResetPasswordApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.settings.KtorManagementAuthSettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.configuration.KtorManagementUserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.globalsettings.KtorManagementGlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier.KtorManagementIdentifierApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier.KtorSelfManagementIdentifiersApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.security.settings.KtorManagementSecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.session.KtorManagementSessionApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.session.KtorSelfManagementSessionApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.KtorManagementUserApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.KtorSelfManagementUserApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.security.KtorManagementUserSecurityApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.security.KtorUserSelfManagementUserSecurityApi
import io.ktor.client.HttpClient
import kotlin.getValue

/**
 * Lazily constructs Ktor API clients from a shared [HttpClient].
 *
 * @param httpClient The host-configured Ktor client (typically from `CommonComponent`).
 */
internal class ManagementUserNetworkModule(
    private val httpClient: HttpClient
) {
    // Auth
    /** API for self-management login. */
    val loginApi by lazy { KtorSelfManagementLoginApi(httpClient) }
    /** API for management session refresh. */
    val refreshTokenApi by lazy { KtorSelfManagementRefreshTokenApi(httpClient) }
    /** API for management password reset. */
    val resetPasswordApi by lazy { KtorSelfManagementResetPasswordApi(httpClient) }
    /** API for administrative auth settings. */
    val authSettingsApi by lazy { KtorManagementAuthSettingsApi(httpClient) }

    // Identifier
    /** API for managing current manager identifiers. */
    val identifiersApi by lazy { KtorSelfManagementIdentifiersApi(httpClient) }
    /** Administrative API for managing any user identifiers. */
    val managementIdentifiersApi by lazy { KtorManagementIdentifierApi(httpClient) }

    // Session
    /** API for managing current manager sessions. */
    val sessionApi by lazy { KtorSelfManagementSessionApi(httpClient) }
    /** Administrative API for managing any user sessions. */
    val managementSessionApi by lazy { KtorManagementSessionApi(httpClient) }

    // User
    /** API for current manager profile. */
    val userApi by lazy { KtorSelfManagementUserApi(httpClient) }
    /** Administrative API for managing any user profiles. */
    val managementUserApi by lazy { KtorManagementUserApi(httpClient) }
    /** API for current manager security settings. */
    val userSecurityApi by lazy { KtorUserSelfManagementUserSecurityApi(httpClient) }
    /** Administrative API for managing any user security. */
    val managementUserSecurityApi by lazy { KtorManagementUserSecurityApi(httpClient) }

    // Configuration
    /** API for management user configuration. */
    val userConfigurationApi by lazy { KtorManagementUserConfigurationApi(httpClient) }

    // Settings
    /** Administrative API for global settings. */
    val globalSettingsApi by lazy { KtorManagementGlobalSettingsApi(httpClient) }
    /** Administrative API for security settings. */
    val securitySettingsApi by lazy { KtorManagementSecuritySettingsApi(httpClient) }
}