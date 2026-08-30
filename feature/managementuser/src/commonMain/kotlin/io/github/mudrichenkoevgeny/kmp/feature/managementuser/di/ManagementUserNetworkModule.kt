package io.github.mudrichenkoevgeny.kmp.feature.managementuser.di

import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.login.KtorSelfManagementLoginApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.refreshtoken.KtorSelfManagementRefreshTokenApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.resetpassword.KtorSelfManagementResetPasswordApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.settings.KtorManagementAuthSettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.configuration.KtorManagementUserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier.KtorManagementIdentifierApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier.KtorSelfManagementIdentifiersApi
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
    val loginApi by lazy { KtorSelfManagementLoginApi(httpClient) }
    val refreshTokenApi by lazy { KtorSelfManagementRefreshTokenApi(httpClient) }
    val resetPasswordApi by lazy { KtorSelfManagementResetPasswordApi(httpClient) }
    val authSettingsApi by lazy { KtorManagementAuthSettingsApi(httpClient) }

    // Identifier
    val identifiersApi by lazy { KtorSelfManagementIdentifiersApi(httpClient) }
    val managementIdentifiersApi by lazy { KtorManagementIdentifierApi(httpClient) }

    // Session
    val sessionApi by lazy { KtorSelfManagementSessionApi(httpClient) }
    val managementSessionApi by lazy { KtorManagementSessionApi(httpClient) }

    // User
    val userApi by lazy { KtorSelfManagementUserApi(httpClient) }
    val managementUserApi by lazy { KtorManagementUserApi(httpClient) }
    val userSecurityApi by lazy { KtorUserSelfManagementUserSecurityApi(httpClient) }
    val managementUserSecurityApi by lazy { KtorManagementUserSecurityApi(httpClient) }

    // Configuration
    val userConfigurationApi by lazy { KtorManagementUserConfigurationApi(httpClient) }
}